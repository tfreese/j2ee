/**
 * Created: 28.12.2011
 */

package de.freese.ldap2jdbc.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.freese.ldap2jdbc.BerDecoder;
import de.freese.ldap2jdbc.BerEncoder;
import de.freese.ldap2jdbc.LdapTags;

/**
 * @author Thomas Freese
 */
public class MinaHandler implements IoHandler
{
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Erstellt ein neues {@link MinaHandler} Object.
     */
    public MinaHandler()
    {
        super();
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#exceptionCaught(org.apache.mina.core.session.IoSession, java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception
    {
        getLogger().error(null, cause);
    }

    /**
     * @return {@link Logger}
     */
    private Logger getLogger()
    {
        return this.logger;
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#inputClosed(org.apache.mina.core.session.IoSession)
     */
    @Override
    public void inputClosed(final IoSession session) throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(session.toString());
        }
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#messageReceived(org.apache.mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public void messageReceived(final IoSession session, final Object message) throws Exception
    {
        System.out.println(message);

        IoBuffer buffer = (IoBuffer) message;

        byte[] data = buffer.array();
        BerDecoder bd = new BerDecoder(data);
        BerEncoder be = new BerEncoder(64);

        bd.parseSeq(null);
        int msgID = bd.parseInt();
        int request = bd.parseSeq(null);

        if (request == LdapTags.LDAP_BIND_REQUEST)
        {
            int ldapVersion = bd.parseInt();
            String dn = bd.parseString(true);
            String passwort = bd.parseStringWithTag(LdapTags.ASN_CONTEXT, false, null);

            getLogger().debug("Ldap-Version={}, DB={}, Passwort={}", ldapVersion, dn, passwort);

            be.beginSeq(LdapTags.ASN_SEQUENCE | LdapTags.ASN_CONSTRUCTOR); // BindResponse 0x01
            be.encodeInt(msgID);
            be.beginSeq(LdapTags.LDAP_BIND_RESPONSE); // BindResponse
            be.encodeInt(LdapTags.LDAP_SUCCESS, LdapTags.ASN_ENUMERATED);
            be.encodeOctetString("cn=ldap,ou=users,dc=freese,dc=de".getBytes(), LdapTags.ASN_OCTET_STRING); // LDAPDN
            be.encodeString("", true); // ErrorMessage
            be.endSeq();
            be.endSeq();
        }
        else if (request == LdapTags.LDAP_ABANDON_REQUEST)
        {
            // Abbruch
        }

        if (be.getDataLen() > 0)
        {
            IoBuffer buf = IoBuffer.allocate(256);
            buf.setAutoExpand(true);
            buf.put(be.getTrimmedBuf());
            buf.flip();

            session.write(buf);
        }
        else
        {
            // session.write(null).addListener(IoFutureListener.CLOSE);
            session.close(true);
        }
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#messageSent(org.apache.mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public void messageSent(final IoSession session, final Object message) throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(message.toString());
        }
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#sessionClosed(org.apache.mina.core.session.IoSession)
     */
    @Override
    public void sessionClosed(final IoSession session) throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(session.toString());
        }
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#sessionCreated(org.apache.mina.core.session.IoSession)
     */
    @Override
    public void sessionCreated(final IoSession session) throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(session.toString());
        }
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#sessionIdle(org.apache.mina.core.session.IoSession, org.apache.mina.core.session.IdleStatus)
     */
    @Override
    public void sessionIdle(final IoSession session, final IdleStatus status) throws Exception
    {
        getLogger().info(null);

        // Close the connection if reader is idle.
        if (status == IdleStatus.READER_IDLE)
        {
            getLogger().info("close session: {}", session.toString());
            session.close(true);
        }
    }

    /**
     * @see org.apache.mina.core.service.IoHandler#sessionOpened(org.apache.mina.core.session.IoSession)
     */
    @Override
    public void sessionOpened(final IoSession session) throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(session.toString());
        }
    }
}
