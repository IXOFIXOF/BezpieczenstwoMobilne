package pl.edu.amu.zbsm.s421486.zbsm_421486.services;

import android.content.Context;
import android.content.SharedPreferences;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.security.SecureRandom;

public class DataService {

    public static final String TEXT = "tekst";
    public static final String KEY = "klucz";
    public static int CipherSkip = 0;

    private final Context context;

    public void SetCihperSkip( int Skip )
    {
        CipherSkip = Skip;
    }
    public DataService(Context context) {
        this.context = context;
    }

    public final String GetMd5Hash( String PasswordToHash )
    {
        if( PasswordToHash.toString().isEmpty() )
        {
            return PasswordToHash;
        }
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(PasswordToHash.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            PasswordToHash = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return PasswordToHash;
    }
    public final String MakeCipher( String OriginalMessage )
    {

        CipherSkip %= 26;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < OriginalMessage.length(); i++) {
            result.append((char) ((OriginalMessage.charAt(i) % 97 + CipherSkip) % 26 + 97));
        }

        return result.toString();

    }
    public final String GetCipher( String CipherMessage )
    {
        CipherSkip %= 26;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < CipherMessage.length(); i++) {
            result.append((char) ((CipherMessage.charAt(i) % 97 - CipherSkip) % 26 + 97));
        }

        return result.toString();
    }
    public void saveSettings(String key, String value ) throws NoSuchAlgorithmException
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
