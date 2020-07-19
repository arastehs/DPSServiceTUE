package com.dev.common.security;

import org.apache.log4j.Logger;


public abstract class HashingStrategy {
    private static final Logger log = Logger.getLogger(HashingStrategy.class);

    public byte[] initialize( Object... objects)
    {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {

            if (object == null)
                sb.append("$");
            else
                sb.append(object.toString()).append("$");
        }
        return sb.toString().getBytes();
    }
    public abstract String createMac( Object... objects) throws Exception;

}
