package cz.zcu.qwerty2.daburujanpu.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;



public class GamePreferences {
    private static final String PREFS = "daburu_janpu";
    private static final String PREF_PLAYER_NAME = "player_name";
    private static final String PREF_SERVER_ADDRESS = "server_address";
    private static final String PREF_PORT = "server_port";

    private static final String DEFAULT_PLAYER_NAME = "player";
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 1234;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;



    static protected Preferences getPrefs()
    {
        return Gdx.app.getPreferences( PREFS );
    }

    public static String getPrefPlayerName () {
        return getPrefs().getString(PREF_PLAYER_NAME,DEFAULT_PLAYER_NAME);
    }

    public static void setPrefPlayerName (String name) {
        getPrefs().putString(PREF_PLAYER_NAME,name);
        getPrefs().flush();
    }

    public static String getPrefServerAddress () {
        return getPrefs().getString(PREF_SERVER_ADDRESS,DEFAULT_SERVER_ADDRESS);
    }

    public static void setPrefServerAddress (String address) {
        getPrefs().putString(PREF_SERVER_ADDRESS,address);
        getPrefs().flush();
    }

    public static int getPrefPort () {
        return getPrefs().getInteger(PREF_PORT,DEFAULT_PORT);
    }

    public static void setPrefPort (int port) {
        getPrefs().putInteger(PREF_PORT,port);
        getPrefs().flush();
    }


    public static int stringToPort(String s) {
        try {
            int port = Integer.valueOf(s);
            if (port>=MIN_PORT && port<=MAX_PORT) {
                return port;
            } else {
                return DEFAULT_PORT;
            }
        } catch (NumberFormatException e) {
            return DEFAULT_PORT;
        }
    }

}
