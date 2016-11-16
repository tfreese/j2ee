/**
 * Created: 25.05.2015
 */
package de.freese.ldap2jdbc;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("restriction")
public class BerDecoder
{
    /**
     *
     */
    private final com.sun.jndi.ldap.BerDecoder decoder;

    /**
     * Erstellt ein neues {@link BerDecoder} Object.
     *
     * @param buffer byte[]
     */
    public BerDecoder(final byte[] buffer)
    {
        super();

        this.decoder = new com.sun.jndi.ldap.BerDecoder(buffer, 0, buffer.length);
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#bytesLeft()
     */
    public int bytesLeft()
    {
        return this.decoder.bytesLeft();
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#getParsePosition()
     */
    public int getParsePosition()
    {
        return this.decoder.getParsePosition();
    }

    /**
     * @return boolean
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseBoolean()
     */
    public boolean parseBoolean()
    {
        try
        {
            return this.decoder.parseBoolean();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseByte()
     */
    public int parseByte()
    {
        try
        {
            return this.decoder.parseByte();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseEnumeration()
     */
    public int parseEnumeration()
    {
        try
        {
            return this.decoder.parseEnumeration();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseInt()
     */
    public int parseInt()
    {
        try
        {
            return this.decoder.parseInt();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseLength()
     */
    public int parseLength()
    {
        try
        {
            return this.decoder.parseLength();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param tag  int
     * @param rlen int[]
     *
     * @return byte[]
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseOctetString(int,int[])
     */
    public byte[] parseOctetString(final int tag, final int[] rlen)
    {
        try
        {
            return this.decoder.parseOctetString(tag, rlen);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param rlen int[]
     *
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseSeq(int[])
     */
    public int parseSeq(final int[] rlen)
    {

        try
        {
            return this.decoder.parseSeq(rlen);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param decodeUTF8 boolean
     *
     * @return String
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseString(boolean)
     */
    public String parseString(final boolean decodeUTF8)
    {
        try
        {
            return this.decoder.parseString(decodeUTF8);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param tag        int
     * @param decodeUTF8 boolean
     * @param rlen       int[]
     *
     * @return String
     *
     * @see com.sun.jndi.ldap.BerDecoder#parseStringWithTag(int, boolean, int[])
     */
    public String parseStringWithTag(final int tag, final boolean decodeUTF8, final int[] rlen)
    {
        try
        {
            return this.decoder.parseStringWithTag(tag, decodeUTF8, rlen);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerDecoder#peekByte()
     */
    public int peekByte()
    {
        try
        {
            return this.decoder.peekByte();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @see com.sun.jndi.ldap.BerDecoder#reset()
     */
    public void reset()
    {
        this.decoder.reset();
    }
}
