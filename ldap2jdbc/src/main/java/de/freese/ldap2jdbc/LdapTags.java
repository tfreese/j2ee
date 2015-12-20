// Erzeugt: 12.05.2015
package de.freese.ldap2jdbc;

/**
 * @author Thomas Freese
 * @see com.sun.jndi.ldap.Ber
 * @see com.sun.jndi.ldap.LdapClient
 */
public interface LdapTags
{
    /**
     *
     */
    public static final int ASN_BOOLEAN = 0x01;

    /**
     *
     */
    public static final int ASN_CONSTRUCTOR = 0x20;
    //
    // /**
    // *
    // */
    // public static final int PRINTABLE_STRING = 0x13;
    //
    // /**
    // *
    // */
    // public static final int SEQUENCE = 0x10;
    //
    // /**
    // *
    // */
    // public static final int SET = 0x11;
    //
    // /**
    // *
    // */
    // public static final int T61_STRING = 0x14;
    //
    // /**
    // *
    // */
    // public static final int TAGGED = 0x80;
    //
    // /**
    // *
    // */
    // public static final int UNIVERSAL_STRING = 0x1C;
    //
    // /**
    // *
    // */
    // public static final int UTC_TIME = 0x17;
    //
    // /**
    // *
    // */
    // public static final int UTF8_STRING = 0x0C;
    //
    // /**
    // *
    // */
    // public static final int VIDEOTEX_STRING = 0x15;
    //
    // /**
    // *
    // */
    // public static final int VISIBLE_STRING = 0x1A;

    /**
     *
     */
    public static final int ASN_CONTEXT = 0x80;

    /**
     *
     */
    public static final int ASN_ENUMERATED = 0x0A;

    /**
     *
     */
    public static final int ASN_INTEGER = 0x02;

    /**
     *
     */
    public static final int ASN_OCTET_STRING = 0x04;

    /**
     *
     */
    public static final int ASN_SEQUENCE = 0x10;

    /**
     *
     */
    public static final int LDAP_ABANDON_REQUEST = 0x50;

    /**
     *
     */
    public static final int LDAP_BIND_REQUEST = 0x60;

    /**
     *
     */
    public static final int LDAP_BIND_RESPONSE = 0x61;

    /**
     *
     */
    public static final int LDAP_OPERATIONS_ERROR = 1;

    /**
     *
     */
    public static final int LDAP_SEARCH_REQUEST = 0x63;

    /**
     *
     */
    public static final int LDAP_SEARCH_RESPONSE = 0x64;

    /**
     *
     */
    public static final int LDAP_SUCCESS = 0;

    /**
     *
     */
    public static final int LDAP_UNBIND_REQUEST = 0x42;

    // /**
    // *
    // */
    // public static final int APPLICATION = 0x40;
    //
    // /**
    // *
    // */
    // public static final int BIT_STRING = 0x03;
    //
    // /**
    // *
    // */
    // public static final int BMP_STRING = 0x1E;
    //
    // /**
    // *
    // */
    // public static final int BOOLEAN = 0x01;
    //
    // /**
    // *
    // */
    // public static final int CONSTRUCTED = 0x20;

    //
    // /**
    // *
    // */
    // public static final int EXTERNAL = 0x08;
    //
    // /**
    // *
    // */
    // public static final int GENERAL_STRING = 0x1B;
    //
    // /**
    // *
    // */
    // public static final int GENERALIZED_TIME = 0x18;
    //
    // /**
    // *
    // */
    // public static final int GRAPHIC_STRING = 0x19;
    //
    // /**
    // *
    // */
    // public static final int IA5_STRING = 0x16;
    //
    // /**
    // *
    // */
    // public static final int INTEGER = 0x02;
    //
    // /**
    // *
    // */
    // public static final int NULL = 0x05;
    //
    // /**
    // *
    // */
    // public static final int NUMERIC_STRING = 0x12;
    //
    // /**
    // *
    // */
    // public static final int OBJECT_IDENTIFIER = 0x06;
}
