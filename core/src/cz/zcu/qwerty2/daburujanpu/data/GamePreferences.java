package cz.zcu.qwerty2.daburujanpu.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by qwerty on 19. 10. 2016.
 */

public class GamePreferences {
    private static final String PREFS = "daburu_janpu";
    private static final String PREF_PLAYER_NAME = "player_name";
    private static final String PREF_SERVER_ADDRESS = "server_address";

    private static final String DEFAULT_PLAYER_NAME = "player";
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";


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

}
