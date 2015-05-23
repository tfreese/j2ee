/**
 * Created on 11.05.2015 17:57:51
 */
package de.freese.ldap2jdbc.asn1;

/**
 * @author Thomas Freese
 * @see "http://en.wikipedia.org/wiki/Abstract_Syntax_Notation_One"
 * @see "http://en.wikipedia.org/wiki/X.690"
 * @see "https://tools.ietf.org/html/rfc4511#section-4.2"
 * @see "http://www.geocities.co.jp/SiliconValley-SanJose/3377/asn1JS.html"
 * @see "https://lapo.it/asn1js"
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

        com.sun.jndi.ldap.BerDecoder bd = new com.sun.jndi.ldap.BerDecoder(request, 0, request.length);
        bd.parseSeq(null);
        System.out.println("MSG-ID: " + bd.parseInt());
        // System.out.println(new String(bd.parseOctetString(com.sun.jndi.ldap.LdapClient.LDAP_REQ_BIND, null)));
        // System.out.println(new String(bd.parseOctetString(0x60, null)));

        // Sequence LDAP_REQ_BIND (0x60)
        bd.parseSeq(null);
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
        bd = new com.sun.jndi.ldap.BerDecoder(request, 0, request.length);
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

        // byte[] code = new byte[]
        // {
        // 0x30, 0x13, 0x02, 0x01, 0x05, 0x16, 0x0e, 0x41, 0x6e, 0x79, 0x62, 0x6f, 0x64, 0x79, 0x20, 0x74, 0x68, 0x65, 0x72, 0x65, 0x3f
        // };
        //
        // com.sun.jndi.ldap.BerEncoder be = new com.sun.jndi.ldap.BerEncoder(64);
        // be.beginSeq(MyBERTags.SEQUENCE);
        // be.encodeInt(5);
        // be.encodeString("Anybody there?", true);
        // be.endSeq();
        // byte[] buf = be.getTrimmedBuf();
        // System.out.println(Hex.encodeHex(buf));
        //
        // com.sun.jndi.ldap.BerDecoder bd = new com.sun.jndi.ldap.BerDecoder(buf, 0, buf.length);
        // bd.parseSeq(null);
        // System.out.println(bd.parseInt());
        // System.out.println(bd.parseString(true));
        //
        // MyBERDecoder decoder = new MyBERDecoder();
        // List<Object> values = decoder.decode(code);
        // System.out.println(values);
        //
        // ASN1Sequence aSN1Sequence = null;
        //
        // try (ASN1InputStream dis = new ASN1InputStream(code))
        // {
        // aSN1Sequence = (ASN1Sequence) dis.readObject();
        // }
        //
        // System.out.println(aSN1Sequence);
        // System.out.println(aSN1Sequence.getObjectAt(0));
        // System.out.println(aSN1Sequence.getObjectAt(1));
        //
        // DERUTF8String derString = new DERUTF8String(aSN1Sequence.getObjectAt(1).toString());
        // System.out.println(Hex.encodeHex(derString.getEncoded()));
        // ASN1EncodableVector v = new ASN1EncodableVector();
        // v.add(derString);
        // System.out.println(new DERSequence(v));
    }
}
