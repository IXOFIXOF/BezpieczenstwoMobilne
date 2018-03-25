package pl.edu.amu.zbsm.s421486.zbsm_421486.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by s421486 on 24.03.2018.
 */

public class DataService {

    public static final String TEXT = "tekst";
    public static final String KEY = "klucz";


    private final Context context;

    public DataService(Context context) {
        this.context = context;
    }

    public void saveSettings(String key, String value)
    {
        SharedPreferences sharePref = context.getSharedPreferences("zbsm.tekst", Context.MODE_PRIVATE );
        SharedPreferences.Editor edit = sharePref.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public String loadSettings(String key)
    {
        SharedPreferences sharePref = context.getSharedPreferences("zbsm.tekst", Context.MODE_PRIVATE );
        return sharePref.getString(key, null);
    }

}
