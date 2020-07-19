package com.dev.common.security;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Strategy extends HashingStrategy {
    private static final Logger log = Logger.getLogger(MD5Strategy.class);

    private String key= "MD5Strategy";


    @Override
    public  String createMac( Object... objects) throws Exception  {
        if(key == null)
            throw new Exception("null key in creating mac");
        String mac = "";
        try {


            byte[] bytes = initialize(objects);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);

            StringBuilder result= new StringBuilder();
            for(byte b:digest)
            {
                result.append(String.format("%02x", b&0xff));
            }
            mac = result.toString();

        } catch (NoSuchAlgorithmException ex) {

            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return mac;
    }

}
