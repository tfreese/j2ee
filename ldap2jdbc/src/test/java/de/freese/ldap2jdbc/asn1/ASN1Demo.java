/**
 * Created on 11.05.2015 17:57:51
 */
package de.freese.ldap2jdbc.asn1;

import de.freese.ldap2jdbc.BerDecoder;

/**
 * @author Thomas Freese
 * @see "http://en.wikipedia.org/wiki/Abstract_Syntax_Notation_One"
 * @see "http://en.wikipedia.org/wiki/X.690"
 * @see "https://tools.ietf.org/html/rfc4511#section-4.2"
 * @see "http://www.geocities.co.jp/SiliconValley-SanJose/3377/asn1JS.html"
 * @see "https://lapo.it/asn1js"
 * @see "http://www.ietf.org/rfc/rfc2251.txt"
 * @see "https://www.ietf.org/rfc/rfc1777.txt"
 * @see "http://www.vijaymukhi.com/vmis/berldap.htm"
 * @see "https://technet.microsoft.com/en-us/library/cc755809(v=ws.10).aspx#w2k3tr_adsrh_how_kgcq"
 * @see com.sun.jndi.ldap.LdapClient
 */
public class ASN1Demo
{
    /**
     * @throws Exception Falls was schief geht.
     * @see com.sun.jndi.ldap.LdapClient#authenticate
     * @see com.sun.jndi.ldap.LdapClient#ldapBind(String, byte[], javax.naming.ldap.Control[], String, boolean)
     */
    private static void ldapAuthenticate() throws Exception
    {
        // none: 30 0C 02 01 01 60 07 02 01 03 04 00 80 00
        // simple: 30 2F 02 01 01 60 2A 02 01 03 04 20 63 6E 3D 6C 64 61 70 2C 6F 75 3D 75 73 65 72 73 2C 64 63 3D 66 72 65 65 73 65 2C 64 63 3D 64 65 80 03 2E
        // 2E 2E
        // byte[] request = new byte[]
        // {
        // 0x30, 0x0C, 0x02, 0x01, 0x01, 0x60, 0x07, 0x02, 0x01, 0x03, 0x04, 0x00, (byte) 0x80, 0x00
        // };
        byte[] request =
                new byte[]
                {
                        0x30,
                        0x2F,
                        0x02,
                        0x01,
                        0x01,
                        0x60,
                        0x2A,
                        0x02,
                        0x01,
                        0x03,
                        0x04,
                        0x20,
                        0x63,
                        0x6E,
                        0x3D,
                        0x6C,
                        0x64,
                        0x61,
                        0x70,
                        0x2C,
                        0x6F,
                        0x75,
                        0x3D,
                        0x75,
                        0x73,
                        0x65,
                        0x72,
                        0x73,
                        0x2C,
                        0x64,
                        0x63,
                        0x3D,
                        0x66,
                        0x72,
                        0x65,
                        0x65,
                        0x73,
                        0x65,
                        0x2C,
                        0x64,
                        0x63,
                        0x3D,
                        0x64,
                        0x65,
                        (byte) 0x80,
                        0x03,
                        0x2E,
                        0x2E,
                        0x2E
                };

        BerDecoder bd = new BerDecoder(request);
        System.out.println("Sequence: " + bd.parseSeq(null));
        System.out.println("MSG-ID: " + bd.parseInt());
        // System.out.println(new String(bd.parseOctetString(com.sun.jndi.ldap.LdapClient.LDAP_REQ_BIND, null)));
        // System.out.println(new String(bd.parseOctetString(0x60, null)));

        // Sequence LDAP_REQ_BIND (0x60)
        System.out.println("Sequence: " + bd.parseSeq(null));
        System.out.println("LDAP-Version: " + bd.parseInt());
        System.out.println("DN: " + bd.parseString(true));
        System.out.println("Passwort: " + bd.parseStringWithTag(0x80, false, null));

        System.out.println("Bytes left: " + bd.bytesLeft());

        System.out.println();

        // 2. Request 30 06 02 01 02 50 01 01
        request = new byte[]
        {
                0x30, 0x06, 0x02, 0x01, 0x02, 0x50, 0x01, 0x01
        };
        bd = new BerDecoder(request);
        System.out.println("Sequence: " + bd.parseSeq(null));
        System.out.println("MSG-ID: " + bd.parseInt());
        System.out.println("Sequence: " + bd.parseSeq(null));
        // System.out.println("???: " + bd.parseBoolean());
        // System.out.println("???: " + bd.parseString(true));
        // System.out.println("???: " + bd.parseStringWithTag(0x80, false, null));
        // System.out.println("???: " + bd.parseOctetString(0x80, null));
        System.out.println("Bytes left: " + bd.bytesLeft());
    }

    /**
     * @param args String[]
     * @throws Exception Falls was schief geht.
     */
    public static void main(final String[] args) throws Exception
    {
        ldapAuthenticate();
        System.out.println();

        // 30 63 02 01 02 63 41 04 1E 6F 75 3D 61 64 64 72 65 73 73 62 6F 6F 6B 2C 64 63 3D 66 72 65 65 73 65 2C 64 63 3D 64 65 0A 01 01 0A 01 03 02 01 00 02 01
        // 00 01 01 00 87 0B 6F 62 6A 65 63 74 63 6C 61 73 73 30 03 04 01 2A A0 1B 30 19 04 17 32 2E 31 36 2E 38 34 30 2E 31 2E 31 31 33 37 33 30 2E 33 2E 34 2E
        // 32
        byte[] request = new byte[]
                {
                0x61, 0x42, 0x41
                };
        BerDecoder bd = new BerDecoder(request);
        int seq = bd.parseSeq(null);
        System.out.printf("Sequence: %d/%x %n", seq, seq);
        System.out.println("Bytes left: " + bd.bytesLeft());
        System.out.println("String: " + bd.parseString(true));
    }
}
