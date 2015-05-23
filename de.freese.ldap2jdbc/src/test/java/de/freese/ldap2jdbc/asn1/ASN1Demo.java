/**
 * Created on 11.05.2015 17:57:51
 */
package de.freese.ldap2jdbc.asn1;

import java.util.List;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;

/**
 * @author Thomas Freese
 * @see "http://en.wikipedia.org/wiki/Abstract_Syntax_Notation_One"
 * @see "http://en.wikipedia.org/wiki/X.690"
 */
public class ASN1Demo
{
    /**
     * @param args String[]
     * @throws Exception Falls was schief geht.
     */
    public static void main(final String[] args) throws Exception
    {
        byte[] code = new byte[]
        {
                0x30, 0x13, 0x02, 0x01, 0x05, 0x16, 0x0e, 0x41, 0x6e, 0x79, 0x62, 0x6f, 0x64, 0x79, 0x20, 0x74, 0x68, 0x65, 0x72, 0x65, 0x3f
        };

        com.sun.jndi.ldap.BerEncoder be = new com.sun.jndi.ldap.BerEncoder(64);
        be.beginSeq(MyBERTags.SEQUENCE);
        be.encodeInt(5);
        be.encodeString("Anybody there?", true);
        be.endSeq();
        byte[] buf = be.getTrimmedBuf();
        System.out.println(Hex.encodeHex(buf));

        com.sun.jndi.ldap.BerDecoder bd = new com.sun.jndi.ldap.BerDecoder(buf, 0, buf.length);
        bd.parseSeq(null);
        System.out.println(bd.parseInt());
        System.out.println(bd.parseString(true));

        MyBERDecoder decoder = new MyBERDecoder();
        List<Object> values = decoder.decode(code);
        System.out.println(values);

        ASN1Sequence aSN1Sequence = null;

        try (ASN1InputStream dis = new ASN1InputStream(code))
        {
            aSN1Sequence = (ASN1Sequence) dis.readObject();
        }

        System.out.println(aSN1Sequence);
        System.out.println(aSN1Sequence.getObjectAt(0));
        System.out.println(aSN1Sequence.getObjectAt(1));

        DERUTF8String derString = new DERUTF8String(aSN1Sequence.getObjectAt(1).toString());
        System.out.println(Hex.encodeHex(derString.getEncoded()));
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(derString);
        System.out.println(new DERSequence(v));
    }
}
