package org.erickzarat.academiccontrol.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by erick on 5/06/16.
 */
public class EncryptHelper {

    private static EncryptHelper instance = null;
    protected EncryptHelper() {
        // Exists only to defeat instantiation.
    }
    public static EncryptHelper getInstance() {
        if(instance == null) {
            instance = new EncryptHelper();
        }
        return instance;
    }

    public String MD5Encrypt(String pass){
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());

            byte byteData[] = md.digest();


            for (int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

            System.out.println("Digest(in hex format):: " + sb.toString());
        } catch (NullPointerException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return sb.toString();
    }


}
