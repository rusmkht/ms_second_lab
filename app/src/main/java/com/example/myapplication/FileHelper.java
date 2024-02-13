package com.example.myapplication;

import android.content.Context;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileHelper {

    private static final String ALGORITHM = "AES";

    public static void writeToFile(Context context, String data, String filename, String secretKey) {
        FileOutputStream fos = null;
        try {
            SecretKey key = generateKey(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());

            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(Base64.encode(encryptedData, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFromFile(Context context, String filename, String secretKey) {
        FileInputStream fis = null;
        try {
            SecretKey key = generateKey(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            fis = context.openFileInput(filename);
            byte[] encryptedData = new byte[fis.available()];
            fis.read(encryptedData);

            byte[] decryptedData = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static SecretKey generateKey(String secretKey) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return new SecretKeySpec(factory.generateSecret(new PBEKeySpec(secretKey.toCharArray())).getEncoded(), ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
