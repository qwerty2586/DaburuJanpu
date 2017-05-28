package cz.zcu.qwerty2.daburujanpu.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class GamePreferences {
    private static final String PREFS = "daburu_janpu";
    private static final String PREF_PLAYER_NAME = "player_name";
    private static final String PREF_SERVER_ADDRESS = "server_address";
    private static final String PREF_PORT = "server_port";
    private static final String PREF_RECONNECT_ID = "reconnect_id";
    private static final String PREF_COLOR = "color";
    private static final String PREF_CTYPE = "control_type";

    private static final String DEFAULT_PLAYER_NAME = "player";
    private static final String DEFAULT_SERVER_ADDRESS = "alice-rabbit.pilsfree.net";
    public static final int DEFAULT_RECONNECT_ID = -1;
    public  static final int TOTAL_CONTROL_TYPES = 3;

    private static final int DEFAULT_PORT = 5001;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int DEFAULT_COLOR = 0;
    private static final int DEFAULT_CTYPE = 0;




    static protected Preferences getPrefs()
    {
        return Gdx.app.getPreferences( PREFS );
    }

    public static String getPrefPlayerName () {
        return getPrefs().getString(PREF_PLAYER_NAME,DEFAULT_PLAYER_NAME);
    }

    public static void setPrefPlayerName (String name) {
        Preferences prefs = getPrefs();
        prefs.putString(PREF_PLAYER_NAME,name);
        prefs.flush();
    }

    public static String getPrefServerAddress () {
        return getPrefs().getString(PREF_SERVER_ADDRESS,DEFAULT_SERVER_ADDRESS);
    }

    public static void setPrefServerAddress (String address) {
        Preferences prefs = getPrefs();
        prefs.putString(PREF_SERVER_ADDRESS,address);
        prefs.flush();
    }

    public static int getPrefPort () {
        return getPrefs().getInteger(PREF_PORT,DEFAULT_PORT);
    }

    public static void setPrefPort (int port) {
        Preferences prefs = getPrefs();
        prefs.putInteger(PREF_PORT,port);
        prefs.flush();
    }

    public static void setReconnectId (int id) {
        Preferences prefs = getPrefs();
        prefs.putInteger(PREF_RECONNECT_ID,id);
        prefs.flush();
    }

    public static int getReconnectId () {
        return getPrefs().getInteger(PREF_RECONNECT_ID,DEFAULT_RECONNECT_ID);
    }

    public static void setColor (int color) {
        Preferences prefs = getPrefs();
        prefs.putInteger(PREF_COLOR,color);
        prefs.flush();
    }
    public static int getColor () {
        return getPrefs().getInteger(PREF_COLOR,DEFAULT_COLOR);
    }

    public static void setCType (int ctype) {
        Preferences prefs = getPrefs();
        prefs.putInteger(PREF_CTYPE,ctype);
        prefs.flush();
    }
    public static int getCType () {
        return getPrefs().getInteger(PREF_CTYPE, DEFAULT_CTYPE);
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
