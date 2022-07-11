package web.custom;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashPassword {
    public static String run(String password) {
        String hashPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytePassword = messageDigest.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : bytePassword){
                sb.append(String.format("%02x",b));
            }
            hashPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashPassword;

    }
}
