// // Erzeugt: 12.05.2015
// package de.freese.ldap2jdbc.asn1;
//
// import java.io.ByteArrayInputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.ByteBuffer;
// import java.nio.charset.Charset;
// import java.util.ArrayList;
// import java.util.List;
// import de.freese.ldap2jdbc.LdapTags;
//
// /**
// * @author Thomas Freese (AuVi)
// * @see org.bouncycastle.asn1.ASN1InputStream
// */
// public class MyBERDecoder
// {
// /**
// *
// */
// public MyBERDecoder()
// {
// super();
// }
//
// /**
// * @param input byte[]
// * @return {@link List}
// * @throws IOException Falls was schief geht.
// */
// public List<Object> decode(final byte[] input) throws IOException
// {
// List<Object> values = null;
//
// try (ByteArrayInputStream bais = new ByteArrayInputStream(input))
// {
// values = decode(bais);
// }
//
// return values;
// }
//
// /**
// * @param input {@link InputStream}
// * @return {@link List}
// * @throws IOException Falls was schief geht.
// */
// public List<Object> decode(final InputStream input) throws IOException
// {
// int tag = readTagNumber(input);
// int size = input.read(); // Anzahl bytes zu lesen
//
// List<Object> values = new ArrayList<>();
//
// while (tag != -1)
// {
// switch (tag)
// {
// case LdapTags.SEQUENCE:
// // wir machen alles als Sequence
// break;
// case LdapTags.INTEGER:
// values.add(decodeInteger(input));
// break;
// case LdapTags.IA5_STRING:
// values.add(decodeString(input, Charset.forName("US-ASCII")));
// break;
// case LdapTags.UTF8_STRING:
// values.add(decodeString(input, Charset.forName("UTF-8")));
// break;
// default:
// throw new RuntimeException("unknown tag " + tag + " encountered");
// }
//
// tag = readTagNumber(input);
// }
//
// return values;
// }
//
// /**
// * @param input {@link InputStream}
// * @return Integer
// * @throws IOException Falls was schief geht.
// */
// private Integer decodeInteger(final InputStream input) throws IOException
// {
// int length = input.read();
// Integer value = null;
//
// if (length == 1)
// {
// value = input.read();
//
// // if (value == MyBERTags.NULL)
// // {
// // value = null;
// // }
// }
// else
// {
// byte[] bytes = new byte[length];
// input.read(bytes);
//
// value = ByteBuffer.wrap(bytes).getInt();
// }
//
// return value;
// }
//
// /**
// * @param input {@link InputStream}
// * @param charset {@link Charset}
// * @return String
// * @throws IOException Falls was schief geht.
// */
// private String decodeString(final InputStream input, final Charset charset) throws IOException
// {
// int length = input.read();
// byte[] bytes = new byte[length];
// input.read(bytes);
//
// return new String(bytes, charset);
// }
//
// /**
// * @param input {@link InputStream}
// * @return int
// * @throws IOException Falls was schief geht.
// */
// private int readTagNumber(final InputStream input) throws IOException
// {
// int tag = input.read();
//
// if (tag == -1)
// {
// return -1;
// }
//
// int tagNo = tag & 0x1F;
// //
// // with tagged object tag number is bottom 5 bits, or stored at the start of the content
// //
// if (tagNo == 0x1F)
// {
// tagNo = 0;
//
// int b = input.read();
//
// // X.690-0207 8.1.2.4.2
// // "c) bits 7 to 1 of the first subsequent octet shall not all be zero."
// if ((b & 0x7F) == 0) // Note: -1 will pass
// {
// throw new RuntimeException("corrupted stream - invalid high tag number found");
// }
//
// while ((b >= 0) && ((b & 0x80) != 0))
// {
// tagNo |= (b & 0x7F);
// tagNo <<= 7;
// b = input.read();
// }
//
// if (b < 0)
// {
// throw new RuntimeException("EOF found inside tag value.");
// }
//
// tagNo |= (b & 0x7F);
// }
//
// return tagNo;
// }
// }
