/**
 * Created: 25.05.2015
 */
package de.freese.ldap2jdbc;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("restriction")
public class BerEncoder
{
    /**
     *
     */
    private final com.sun.jndi.ldap.BerEncoder encoder;

    /**
     * Erstellt ein neues {@link BerEncoder} Object.
     */
    public BerEncoder()
    {
        super();

        this.encoder = new com.sun.jndi.ldap.BerEncoder();
    }

    /**
     * Erstellt ein neues {@link BerEncoder} Object.
     *
     * @param bufsize int
     */
    public BerEncoder(final int bufsize)
    {
        super();

        this.encoder = new com.sun.jndi.ldap.BerEncoder(bufsize);
    }

    /**
     * @param tag int
     *
     * @see com.sun.jndi.ldap.BerEncoder#beginSeq(int)
     */
    public void beginSeq(final int tag)
    {
        this.encoder.beginSeq(tag);
    }

    /**
     * @param b boolean
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeBoolean(boolean)
     */
    public void encodeBoolean(final boolean b)
    {
        this.encoder.encodeBoolean(b);
    }

    /**
     * @param b   boolean
     * @param tag int
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeBoolean(boolean,int)
     */
    public void encodeBoolean(final boolean b, final int tag)
    {
        this.encoder.encodeBoolean(b, tag);
    }

    /**
     * @param b int
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeByte(int)
     */
    public void encodeByte(final int b)
    {
        this.encoder.encodeByte(b);
    }

    /**
     * @param i int
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeInt(int)
     */
    public void encodeInt(final int i)
    {
        this.encoder.encodeInt(i);
    }

    /**
     * @param i   int
     * @param tag int
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeInt(int,int)
     */
    public void encodeInt(final int i, final int tag)
    {
        this.encoder.encodeInt(i, tag);
    }

    /**
     * @param tb  byte[]
     * @param tag int
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeOctetString(byte[],int)
     */
    public void encodeOctetString(final byte[] tb, final int tag)
    {
        try
        {
            this.encoder.encodeOctetString(tb, tag);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param tb       byte[]
     * @param tag      int
     * @param tboffset int
     * @param length   int
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeOctetString(byte[],int,int,int)
     */
    public void encodeOctetString(final byte[] tb, final int tag, final int tboffset, final int length)
    {
        try
        {
            this.encoder.encodeOctetString(tb, tag, tboffset, length);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param str        String
     * @param encodeUTF8 boolean
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeString(String,boolean)
     */
    public void encodeString(final String str, final boolean encodeUTF8)
    {
        try
        {
            this.encoder.encodeString(str, encodeUTF8);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param str        String
     * @param tag        int
     * @param encodeUTF8 boolean
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeString(String,int,boolean)
     */
    public void encodeString(final String str, final int tag, final boolean encodeUTF8)
    {
        try
        {
            this.encoder.encodeString(str, tag, encodeUTF8);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param strs       String[]
     * @param encodeUTF8 boolean
     *
     * @see com.sun.jndi.ldap.BerEncoder#encodeStringArray(String[],boolean)
     */
    public void encodeStringArray(final String[] strs, final boolean encodeUTF8)
    {
        try
        {
            this.encoder.encodeStringArray(strs, encodeUTF8);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @throws EncoderException Falls was schief geht.
     * @see com.sun.jndi.ldap.BerEncoder#endSeq()
     */
    public void endSeq()
    {
        try
        {
            this.encoder.endSeq();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return byte[]
     *
     * @see com.sun.jndi.ldap.BerEncoder#getBuf()
     */
    public byte[] getBuf()
    {
        return this.encoder.getBuf();
    }

    /**
     * @return int
     *
     * @see com.sun.jndi.ldap.BerEncoder#getDataLen()
     */
    public int getDataLen()
    {
        return this.encoder.getDataLen();
    }

    /**
     * @return byte[]
     *
     * @see com.sun.jndi.ldap.BerEncoder#getTrimmedBuf()
     */
    public byte[] getTrimmedBuf()
    {
        return this.encoder.getTrimmedBuf();
    }

    /**
     * @see com.sun.jndi.ldap.BerEncoder#reset()
     */
    public void reset()
    {
        this.encoder.reset();
    }
}
