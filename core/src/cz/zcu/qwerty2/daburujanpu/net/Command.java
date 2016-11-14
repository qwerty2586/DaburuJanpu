package cz.zcu.qwerty2.daburujanpu.net;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command {
    public static final char DEFAULT_DELIMITER = ';';
    public static final int CONNECT_TO_SERVER = -1;
    public static final int RESULT_CONNECT = -2;


    public static final int PING = 1;
    public static final int PONG = 2;
    public static final int SW_DISCONNECT = 3;
    public static final int SET_PLAYER_NAME = 5;
    public static final int RESULT_PLAYER_NAME = 6;
    public static final int REQUEST_LOBBY_LIST = 7;
    public static final int LOBBY_LIST = 8;
    public static final int CREATE_LOBBY = 9;
    public static final int RESULT_CREATE_LOBBY = 10;
    public static final int JOIN_LOBBY = 11;
    public static final int RESULT_JOIN_LOBBY = 12;
    public static final int REQUEST_LOBBY_INFO = 13;
    public static final int LOBBY_INFO = 14;
    public static final int LEAVE_LOBBY = 15;
    public static final int SEND_CHAT = 17;
    public static final int RECEIVE_CHAT = 18;
    public static final int SET_READY = 19;
    public static final int CHANGE_COLOR = 20;

    public static final int  CHAT_MAX_LENGTH = 16 * 1024;


    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    public int code;
    public ArrayList<String> args = new ArrayList<String>();

    public Command(int code) {
        this.code = code;
    }

    public Command addArg(String arg) {
        args.add(arg);
        return this;
    }

    public Command addArg(int arg) {
        return addArg(Integer.toString(arg));
    }

    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(code);
        for (String arg : args
                ) {
            stringBuilder.append(DEFAULT_DELIMITER);
            stringBuilder.append(arg);
        }
        return stringBuilder.toString();
    }

    public static Command fromString(String s) {
        String[] strArray = s.split("" + DEFAULT_DELIMITER);
        List<String> list = Arrays.asList(strArray);

        int code = Integer.valueOf(list.get(0));
        Command r = new Command(code);
        r.args = new ArrayList<String>(list);
        r.args.remove(0);
        return r;
    }

    ;


}
