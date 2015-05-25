// Created: 16.01.2010
/**
 * 16.01.2010
 */
package de.freese.ldap2jdbc.server;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.freese.ldap2jdbc.BerDecoder;
import de.freese.ldap2jdbc.BerEncoder;
import de.freese.ldap2jdbc.LdapTags;
import de.freese.littlemina.core.IoHandler;
import de.freese.littlemina.core.buffer.IoBuffer;
import de.freese.littlemina.core.session.IoSession;

/**
 * {@link IoHandler} für das LDAP-Protokoll.
 *
 * @author Thomas Freese
 */
public class LdapHandler implements IoHandler
{
    /**
     * The end of line character sequence used by most IETF protocols. That is a carriage return followed by a newline: "\r\n" (NETASCII_EOL)
     */
    private static final byte[] CRLF = new byte[]
            {
        0x0D, 0x0A
            };

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LdapHandler.class);

    /**
     *
     */
    private final Charset charset;

    /**
     *
     */
    private final CharsetDecoder decoder;

    /**
     *
     */
    private final CharsetEncoder encoder;

    /**
     * Erstellt ein neues {@link LdapHandler} Object.
     */
    public LdapHandler()
    {
        super();

        // this.charset = Charset.forName("ISO-8859-1");
        this.charset = Charset.forName("US-ASCII");
        this.decoder = this.charset.newDecoder();
        this.encoder = this.charset.newEncoder();
    }

    /**
     * @see de.freese.littlemina.core.IoHandler#messageReceived(de.freese.littlemina.core.session.IoSession)
     */
    @Override
    public void messageReceived(final IoSession session) throws Exception
    {
        LOGGER.debug(session.toString());

        IoBuffer inputBuffer = session.getBuffer();
        // String request = inputBuffer.getString(this.decoder);
        // String request = inputBuffer.getHexDump();

        // if (StringUtils.isBlank(request))
        // {
        // // Hack für das richtige schliessen ohne etwas zu schreiben.
        // session.write(null);
        //
        // session.close();
        //
        // return;
        // }

        // LOGGER.debug(request);

        byte[] data = inputBuffer.array();
        BerDecoder bd = new BerDecoder(data);
        BerEncoder be = new BerEncoder(64);

        bd.parseSeq(null);
        int msgID = bd.parseInt();
        int request = bd.parseSeq(null);

        LOGGER.info("MsgID={}, Request={}/0x{}", msgID, request, Integer.toHexString(request));

        if (request == LdapTags.LDAP_BIND_REQUEST)
        {
            // https://www.ietf.org/rfc/rfc1777.txt
            int ldapVersion = bd.parseInt();
            String dn = bd.parseString(true);
            String passwort = bd.parseStringWithTag(LdapTags.ASN_CONTEXT, false, null);

            LOGGER.info("Ldap-Version={}, DB={}, Passwort={}", ldapVersion, dn, passwort);

            be.beginSeq(LdapTags.ASN_SEQUENCE | LdapTags.ASN_CONSTRUCTOR); // BindResponse 0x01
            be.encodeInt(msgID);
            be.beginSeq(LdapTags.LDAP_BIND_RESPONSE); // BindResponse
            be.encodeInt(LdapTags.LDAP_SUCCESS, LdapTags.ASN_ENUMERATED);
            be.encodeOctetString("cn=ldap,ou=users,dc=freese,dc=de".getBytes(), LdapTags.ASN_OCTET_STRING); // LDAPDN
            be.encodeString("", true); // ErrorMessage
            be.endSeq();
            be.endSeq();
        }
        else if ((request == LdapTags.LDAP_UNBIND_REQUEST) || (request == LdapTags.LDAP_ABANDON_REQUEST))
        {
            // Abbruch
        }
        else
        {
            // Alles andere ist nicht imlementiert -> Fehlermeldung
            be.beginSeq(LdapTags.ASN_SEQUENCE | LdapTags.ASN_CONSTRUCTOR);
            be.encodeInt(msgID);
            be.beginSeq(LdapTags.LDAP_SEARCH_RESPONSE); // Response
            be.encodeInt(LdapTags.LDAP_OPERATIONS_ERROR, LdapTags.ASN_ENUMERATED);
            be.encodeOctetString("cn=ldap,ou=users,dc=freese,dc=de".getBytes(), LdapTags.ASN_OCTET_STRING); // LDAPDN
            be.encodeString("not implemented", true); // ErrorMessage
            be.endSeq();
            be.endSeq();
        }

        if (be.getDataLen() > 0)
        {
            IoBuffer buffer = IoBuffer.allocate(64);
            buffer.put(be.getTrimmedBuf());
            buffer.flip();
            session.write(buffer);
        }
        else
        {
            session.write(null);
            session.close();
        }

        be = null;
        bd = null;
    }

    /**
     * @see de.freese.littlemina.core.IoHandler#sessionClosed(de.freese.littlemina.core.session.IoSession)
     */
    @Override
    public void sessionClosed(final IoSession session) throws Exception
    {
        LOGGER.info(session.toString());
    }

    /**
     * @see de.freese.littlemina.core.IoHandler#sessionOpened(de.freese.littlemina.core.session.IoSession)
     */
    @Override
    public void sessionOpened(final IoSession session) throws Exception
    {
        LOGGER.info(session.toString());

        // IoBuffer buffer = IoBuffer.allocate(64);
        //
        // String response = "* OK IMAP4rev1 server ready";
        // buffer.putString(response, this.encoder);
        // buffer.put(CRLF);
        // buffer.flip();
        //
        // session.write(buffer);
    }
}
