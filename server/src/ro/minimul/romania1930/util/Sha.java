package ro.minimul.romania1930.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 * SHA message digest utility methods.
 * 
 * @author Paul Nechifor
 */
public class Sha {
    private static final MessageDigest SHA256;
    private static final MessageDigest SHA1;

    static {
        MessageDigest sha256 = null;
        MessageDigest sha1 = null;
        
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        
        SHA256 = sha256;
        SHA1 = sha1;
    }

    private Sha() {
    }

    public static byte[] get256(byte[] bytes) {
        SHA256.update(bytes, 0, bytes.length);
        return SHA256.digest();
    }
    
    public static String get256Hex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(get256(bytes));
    }
    
    public static String get256Base64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(get256(bytes));
    }

    public static byte[] get1(byte[] bytes) {
        SHA1.update(bytes, 0, bytes.length);
        return SHA1.digest();
    }
    
    public static String get1Hex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(get1(bytes));
    }
    
    public static String get1Base64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(get1(bytes));
    }
}