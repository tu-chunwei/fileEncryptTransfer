
package com.tu.security.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

public class SecurityUtil {

    private final int    keySize;
    private final int    iterationCount;
    private final Cipher cipher;

    public SecurityUtil( int keySize ) {
        this( keySize, 1000 );
    }

    public SecurityUtil( int keySize, int iterationCount ) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
        } catch ( NoSuchAlgorithmException | NoSuchPaddingException e ) {
            throw fail( e );
        }
    }

    public String encrypt( String salt, String iv, String passphrase, String plaintext ) {
        try {
            SecretKey key = generateKey( salt, passphrase );
            byte[] encrypted = doFinal( Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes( "UTF-8" ) );
            return base64( encrypted );
        } catch ( UnsupportedEncodingException e ) {
            throw fail( e );
        }
    }

    public String decrypt( String salt, String iv, String passphrase, String ciphertext ) {
        try {
            SecretKey key = generateKey( salt, passphrase );
            byte[] decrypted = doFinal( Cipher.DECRYPT_MODE, key, iv, base64( ciphertext ) );
            return new String( decrypted, "UTF-8" );
        } catch ( UnsupportedEncodingException e ) {
            throw fail( e );
        }
    }

    public byte[] doFinal( int encryptMode, SecretKey key, String iv, byte[] bytes ) {
        try {
            cipher.init( encryptMode, key, new IvParameterSpec( hex( iv ) ) );
            return cipher.doFinal( bytes );
        } catch ( InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e ) {
            throw fail( e );
        }
    }

    public SecretKey generateKey( String salt, String passphrase ) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );
            KeySpec spec = new PBEKeySpec( passphrase.toCharArray(), hex( salt ), iterationCount, keySize );
            SecretKey key = new SecretKeySpec( factory.generateSecret( spec ).getEncoded(), "AES" );
            return key;
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw fail( e );
        }
    }

    public static String random( int length ) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex( salt );
    }

    public static String base64( byte[] bytes ) {
        return Base64.encodeBase64String( bytes );
    }

    public static byte[] base64( String str ) {
        return Base64.decodeBase64( str );
    }

    public static String hex( byte[] bytes ) {
        return Hex.encodeHexString( bytes );
    }

    public static byte[] hex( String str ) {
        try {
            return Hex.decodeHex( str.toCharArray() );
        } catch ( DecoderException e ) {
            throw new IllegalStateException( e );
        }
    }

    public IllegalStateException fail( Exception e ) {
        return new IllegalStateException( e );
    }
    public static boolean hasValue( String value ) {
        return !StringUtils.isEmpty( value );
    }

    public static int getInt( String s ) {

        if ( StringUtils.isNumeric(s) && hasValue( s ) ) {
            return Integer.parseInt( s );
        }

        return 0;
    }
    
	public static int[] convert( String indices ) {
        String[] indexes = indices.split( "," );

        int[] ints = new int[indexes.length];

        for ( int i = 0; i < indexes.length; i++ ) {
            String s = getNum( indexes[i] );
            ints[i] = getInt( s );
        }
        return ints;
    }

    public static String getNum( String s ) {
        switch ( s ) {
            case "a":
                return "0";
            case "b":
                return "1";
            case "c":
                return "2";
            case "d":
                return "3";
            case "e":
                return "4";
            case "f":
                return "5";
        }
        return s;
    }
}
