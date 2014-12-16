package app.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.hsqldb.lib.StringConverter;

/**
 * An unsecure "salting" hasher to hide user's name
 */
public class HashService {
    private static final String salt = "6g0rdzoUdiHD1aRo68LBPSjADwmcGr5XB9jxbsKcFBJD9iVxeBKLolY1c9nJGfUeLdV5MWDinc89cOsHP8eE2b+vCkE9x3U";
    
    public static String HashUsername(String name) {
        byte[] hash = DigestUtils.md5(HashService.salt + name);
        return StringConverter.byteArrayToHexString(hash);
    }
}
