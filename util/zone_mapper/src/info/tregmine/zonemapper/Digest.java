package info.tregmine.zonemapper;

import java.security.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * A wrapper for the java security package message digest, that adds some
 * convenience methods.
 */
public class Digest
{
    private MessageDigest md = null;
    private static final char[] hexChars = {
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F' };

    /**
     * Constructs a new Digest object.
     *
     * @param digest The name of a digest algorithm, as defined by the java security package.
     * @throws java.security.NoSuchAlgorithmException
     */
    public Digest(String digest)
    throws NoSuchAlgorithmException
    {
        md = MessageDigest.getInstance(digest);
    }

    public byte[] hashFile(File file)
    throws FileNotFoundException, IOException
    {
        InputStream is = new FileInputStream(file);

        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = is.read(buffer)) > 0) {
            md.update(buffer, 0, read);
        }

        byte[] hash = md.digest();

        is.close();

        return hash;
    }

    /**
     * Calculate the byte digest of a String-array.
     *
     * @param dataToHash
     * @return
     */
    public byte[] hashAsBytes(String[] dataToHash)
    {
        StringBuffer sb = new StringBuffer();
        for (String entry : dataToHash) {
            sb.append(entry);
        }
        md.update(sb.toString().getBytes(), 0, dataToHash.length);
        return md.digest();
    }

    /**
     * Calculate the byte digest of a byte-array.
     *
     * @param dataToHash
     * @return
     */
    public byte[] hashAsBytes(byte[] dataToHash)
    {
        md.update(dataToHash, 0, dataToHash.length);
        return md.digest();
    }

    /**
     * Calculate the byte digest of a String.
     *
     * @param dataToHash
     * @return
     */
    public byte[] hashAsBytes(String dataToHash)
    {
        return hashAsBytes(dataToHash.getBytes());
    }

    /**
     * Calculate the hexadecimal string digest of a String.
     *
     * @param dataToHash
     * @return
     */
    public String hashAsString(String dataToHash)
    {
        return hexStringFromBytes(hashAsBytes(dataToHash.getBytes()));
    }

    /**
     * Calculate the hexadecimal string digest of a byte-array.
     *
     * @param dataToHash
     * @return
     */
    public String hashAsString(byte[] dataToHash)
    {
        return hexStringFromBytes(hashAsBytes(dataToHash));
    }

    /**
     * Convert a byte-array to the hexadecimal string representation.
     *
     * @param b
     * @return
     */
    public static String hexStringFromBytes(byte[] b)
    {
        String hex = "";
        for (int i = 0; i < b.length; i++) {
            hex = hex + hexChars[(b[i] >> 4) & 0xF] + hexChars[b[i] & 0xF];
        }

        return hex;
    }
}
