package cz.zcu.qwerty2.daburujanpu.net;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;

public class NetService implements Runnable {
    CommandQueue queue;

    private OutputStream outputStream;
    Socket outputSocket;

    public NetService(CommandQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("NetService Started");

        while (true) {
            try {
                synchronized (queue) {
                    queue.wait();
                }
            } catch (InterruptedException ie) {
                break;
            }
            while (!queue.isEmpty()) {
                NetCommand command = queue.remove();
                if (command.command == NetCommand.CONNECT_TO_SERVER) {
                    try {
                        outputSocket = new Socket(GamePreferences.getPrefServerAddress(),GamePreferences.getPrefPort());
                        outputStream = outputSocket.getOutputStream();
                        outputStream.write(new String("o0000000").getBytes());
                        outputStream.flush();
                        InputStream tempInputSocket = outputSocket.getInputStream();
                        //TODO


                    } catch (IOException e) {

                    }
                    System.out.println("netserv - " + System.currentTimeMillis());
                }
            }
        }
    }
}
