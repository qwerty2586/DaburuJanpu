package cz.zcu.qwerty2.daburujanpu.net;


import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;

public class NetService implements Runnable {

    private final CommandQueue commandQueue;
    private CommandQueue resultQueue;
    public int myId = 0;

    private OutputStream outputStream;
    private InputStream inputStream;
    private Socket outputSocket,inputSocket;
    private ReceivingThread receivingThread =  new ReceivingThread();
    private boolean connected = false;


    private class ReceivingThread implements Runnable {
        @Override
        public void run() {
            byte[] size_buffer =  new byte[2];
            int len = 0;
            while (true) {
                try {
                    len = inputStream.read(size_buffer, 0, size_buffer.length);
                    if (len<0) {
                        connected=false;
                        return;
                    }
                    System.out.print(String.format("%02x%02x", (int) size_buffer[0],size_buffer[1]));
                    int size = (size_buffer[0] & 0xFF) * 256 + (size_buffer[1] & 0xFF);
                    final byte[] buffer = new byte[size];
                    len = inputStream.read(buffer, 0, buffer.length);
                    System.out.println(": "+new String(buffer));
                    publishResult(Command.fromString(new String(buffer)));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void publishResult (final Command command) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                resultQueue.add(command);
            }
        });
    }

    public NetService(CommandQueue commandQueue, CommandQueue resultQueue) {
        this.commandQueue = commandQueue;
        this.resultQueue = resultQueue;


    }

    private boolean connectToServer(String host,int port) {
        try {
            outputSocket = new Socket(GamePreferences.getPrefServerAddress(),GamePreferences.getPrefPort());
            outputStream = outputSocket.getOutputStream();
            byte[] buffer = "00000000".getBytes();
            outputStream.write(buffer);
            outputStream.flush();
            InputStream tempInputStream = outputSocket.getInputStream();
            int count = tempInputStream.read(buffer,0,8);
            if (count==8) {
                if (buffer[0]=='1') {
                    int tempID = Integer.valueOf(new String(buffer).substring(1));
                    inputSocket = new Socket(GamePreferences.getPrefServerAddress(),GamePreferences.getPrefPort());
                    OutputStream tempOutputStream = inputSocket.getOutputStream();
                    buffer[0] = '2';
                    tempOutputStream.write(buffer);
                    tempOutputStream.flush();
                    inputStream = inputSocket.getInputStream();
                    count = inputStream.read(buffer);
                    if (count == 8 && buffer[0]=='3') {
                        new Thread(receivingThread).start();
                        connected =true;
                        myId = Integer.valueOf(new String(buffer).substring(1));
                        return true;
                    } else return false;
                } else return false;
            } else return false;



        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                synchronized (commandQueue) {
                    commandQueue.wait();
                }
            } catch (InterruptedException ie) {
                break;
            }
            while (!commandQueue.isEmpty()) {
                Command command = commandQueue.remove();
                if (command.code == Command.CONNECT_TO_SERVER) {
                    boolean result = connectToServer(GamePreferences.getPrefServerAddress(),GamePreferences.getPrefPort());
                    if (result) publishResult(new Command(Command.RESULT_CONNECT).addArg(Command.SUCCESS));
                    else  publishResult(new Command(Command.RESULT_CONNECT).addArg(Command.FAIL));
                } else {
                    if (connected) {
                        byte[] buffer = command.toString().getBytes();
                        byte[] size_buffer = new byte[2];
                        int size = buffer.length;
                        size_buffer[0] = (byte)(size / 256);
                        size_buffer[1] = (byte)(size % 256);
                        try {
                            outputStream.write(size_buffer,0,size_buffer.length);
                            outputStream.write(buffer,0,buffer.length);
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }

            }
        }
    }
}