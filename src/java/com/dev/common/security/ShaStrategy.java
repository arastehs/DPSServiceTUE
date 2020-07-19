package com.dev.common.security;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ShaStrategy extends HashingStrategy {
    private static Logger log = Logger.getLogger(ShaStrategy.class);

    private String key = "c2a7da53b983";

    @Override
    public String createMac(Object... objects) throws Exception {
        String mac = "";
        if (key == null) {
            log.error("invalid key for creating mac");
            throw new Exception("null key in creating mac");
        }
        try {

            byte[] rnd = new byte[key.length() / 2];
            for (int i = 0; i < key.length(); i += 2) {
                rnd[i / 2] = Integer.decode("0x" + key.substring(i, i + 2)).byteValue();
            }


            byte[] bytes = this.initialize(objects);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(bytes);

            digest[0] ^= rnd[0];
            digest[4] ^= rnd[1];
            digest[6] &= rnd[2];
            digest[10] ^= rnd[3];
            digest[12] ^= rnd[4];
            digest[19] ^= rnd[5];

            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                //result.append(String.format("%02x"));
                result.append(String.format("%02x", b & 0xff));
            }
            mac = result.toString();

        } catch (NoSuchAlgorithmException ex) {

            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return mac;
    }

}
