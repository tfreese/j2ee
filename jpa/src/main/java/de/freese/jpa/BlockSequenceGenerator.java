// Created: 11.07.2018
package de.freese.jpa;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate SequenceGenerator, der ganze Blöcke von Sequence IDs holt.<br>
 * Das hier ist natürlich Blödsinn ! <br>
 * Man braucht für Blockweises Laden eine entsprechende DB-Funktion !
 *
 * @author Thomas Freese
 */
public class BlockSequenceGenerator implements IdentifierGenerator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockSequenceGenerator.class);

    private final Queue<Long> idQueue = new LinkedList<>();

    private int blockSize;

    private String sequenceName;

    @Override
    public void configure(final Type type, final Properties params, final ServiceRegistry serviceRegistry) throws MappingException
    {
        this.sequenceName = ConfigurationHelper.getString("sequenceName", params, (String) null);
        this.blockSize = ConfigurationHelper.getInt("blockSize", params, 1);

        if ((this.sequenceName == null) || this.sequenceName.isBlank())
        {
            throw new MappingException("sequenceName required");
        }

        if (this.blockSize < 1)
        {
            throw new MappingException("blockSize >= 1 required");
        }
    }

    /**
     * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.spi.SharedSessionContractImplementor, java.lang.Object)
     */
    @Override
    public Serializable generate(final SharedSessionContractImplementor session, final Object object) throws HibernateException
    {
        LOGGER.debug("Retrieve next {} IDs from Sequence '{}'", this.blockSize, this.sequenceName);

        // String query = String.format("select %s from %s", session.getEntityPersister(object.getClass().getName(), object).getIdentifierPropertyName(),
        // object.getClass().getSimpleName());
        //
        // Stream ids = session.createQuery(query).stream();

        synchronized (this.idQueue)
        {
            if (this.idQueue.isEmpty())
            {
                // try
                // {
                // NativeQuery<Long> nativeQuery = session.createNativeQuery("call next value for " + this.sequenceName, Long.class);
                //
                // for (int i = 0; i < this.blockSize; i++)
                // {
                // long id = nativeQuery.getSingleResult().longValue();
                //
                // this.idQueue.offer(id);
                // }
                // }
                // catch (Exception ex)
                // {
                // throw ex;
                // }

                session.doWork(connection ->
                {
                    try (Statement statement = connection.createStatement())
                    {
                        for (int i = 0; i < this.blockSize; i++)
                        {
                            try (ResultSet resultSet = statement.executeQuery("call next value for " + this.sequenceName))
                            {
                                resultSet.next();
                                long id = resultSet.getLong(1);

                                this.idQueue.offer(id);
                            }
                        }
                    }
                });
            }

            return this.idQueue.poll();
        }
    }
}
