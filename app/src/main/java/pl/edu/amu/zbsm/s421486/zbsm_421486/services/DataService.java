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
    public static String PlainPassword;
    private Context context;


    public void SetCihperSkip( int Skip )
    {
        CipherSkip = Skip;
    }
    public DataService(Context context) {
        this.context = context;
    }

    private char[] cipherAlphabet;

    private int lowerLimit;
    private int upperLimit;

    public DataService() {

    }
    public void Init()
    {
        PlainPassword = new String("");
        this.lowerLimit = 32;
        this.upperLimit = 126;
        this.cipherAlphabet = new char[upperLimit - lowerLimit + 1];
        // Grab all the ASCII characters between space and ~, inclusive
        for (int i = lowerLimit; i <= upperLimit; i++) {
            cipherAlphabet[i - lowerLimit] = (char) i;
        }
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

    public String cipher(String text, String key) {
        StringBuilder builder = new StringBuilder(text.length());
        int keyIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int pos = (int) c;
            if (pos < lowerLimit || pos > upperLimit) {
                builder.append(c);
            } else {
                char k = key.charAt(keyIndex);
                pos = getCharacterPosition(c);
                int pos2 = getCharacterPosition(k);
                int sum = (pos + pos2) % cipherAlphabet.length;
                builder.append(getCharacter(sum));
                keyIndex = ++keyIndex % key.length();
            }
        }
        return builder.toString();
    }

    public String uncipher(String cipher, String key) {
        StringBuilder builder = new StringBuilder(cipher.length());
        int keyIndex = 0;
        for (int i = 0; i < cipher.length(); i++) {
            char c = cipher.charAt(i);
            int pos = (int) c;
            if (pos < lowerLimit || pos > upperLimit) {
                builder.append(c);
            } else {
                char k = key.charAt(keyIndex);
                pos = getCharacterPosition(c);
                int pos2 = getCharacterPosition(k);
                int sum = pos - pos2;
                while (sum < 0) {
                    sum += cipherAlphabet.length;
                }
                sum = sum % cipherAlphabet.length;
                builder.append(getCharacter(sum));
                keyIndex = ++keyIndex % key.length();
            }
        }
        return builder.toString();
    }

    private int getCharacterPosition(char c) {
        for (int i = 0; i < cipherAlphabet.length; i++) {
            if (c == cipherAlphabet[i]) {
                return i;
            }
        }

        return -1;
    }

    private char getCharacter(int index) {
        if (index >= 0 && index < cipherAlphabet.length) {
            return cipherAlphabet[index];
        } else {
            return '?';
        }
    }

}
