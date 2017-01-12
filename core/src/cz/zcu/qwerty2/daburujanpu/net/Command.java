package cz.zcu.qwerty2.daburujanpu.net;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command {
    public static final char DEFAULT_DELIMITER = ';';
    public static final int CONNECT_TO_SERVER = -1;
    public static final int RESULT_CONNECT = -2;
    public static final int HW_DISCONNECTED = -3;


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
    public static final int SHOW_SERVER_STATISTICS = 21;
    public static final int SERVER_STATISTICS = 22;

    public static final int GAME_STARTED = 30;
    public static final int READY_FOR_GAME_INFO = 31;
    public static final int GAME_INFO = 32;
    public static final int ROUND_LOADED = 33;
    public static final int ROUND_START = 34;
    public static final int MY_UPDATE = 35;
    public static final int LEAVE_GAME = 36;
    public static final int GAME_INFO_SEED = 37;
    public static final int GAME_NEW_ROUND = 38;
    public static final int GAME_RESULT = 39;


    public static final int GAME_RECONNECT_ASK = 50;
    public static final int GAME_RECONNECT_ASK_RESULT = 51;
    public static final int GAME_RECONNECT_DO = 52;
    public static final int GAME_RECONNECT_DO_RESULT = 53;


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

    public int argInt(int index) {
        return Integer.valueOf(args.get(index));
    }

    public float argFloat(int index) {
        return Float.valueOf(args.get(index));
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
