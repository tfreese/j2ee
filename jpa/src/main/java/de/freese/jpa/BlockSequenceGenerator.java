/**
 * Created: 11.07.2018
 */

package de.freese.jpa;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.query.NativeQuery;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * Hibernate SequenceGenerator, der ganze Bloecke von Sequence IDs holt.
 *
 * @author Thomas Freese
 */
public class BlockSequenceGenerator implements IdentifierGenerator, Configurable
{
    // /**
    // *
    // */
    // private static final Logger LOGGER = LoggerFactory.getLogger(BlockSequenceGenerator.class);

    /**
     *
     */
    private String sequenceName = null;

    /**
     *
     */
    private int blockSize = 0;

    /**
    *
    */
    private final Queue<Long> idQueue = new LinkedList<>();

    /**
     * Erstellt ein neues {@link BlockSequenceGenerator} Object.
     */
    public BlockSequenceGenerator()
    {
        super();
    }

    /**
     * @see org.hibernate.id.Configurable#configure(org.hibernate.type.Type, java.util.Properties, org.hibernate.service.ServiceRegistry)
     */
    @Override
    public void configure(final Type type, final Properties params, final ServiceRegistry serviceRegistry) throws MappingException
    {
        this.sequenceName = ConfigurationHelper.getString("sequenceName", params, null);
        this.blockSize = ConfigurationHelper.getInt("blockSize", params, 1);

        if (this.sequenceName == null || this.sequenceName.trim().isEmpty())
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
        // String query = String.format("select %s from %s", session.getEntityPersister(object.getClass().getName(), object).getIdentifierPropertyName(),
        // object.getClass().getSimpleName());
        //
        // Stream ids = session.createQuery(query).stream();

        synchronized (this.idQueue)
        {
            if (this.idQueue.isEmpty())
            {
                try
                {
                    NativeQuery<BigInteger> nativeQuery = session.createNativeQuery("call next value for " + this.sequenceName, BigInteger.class);

                    // // Das hier ist natürlich Blödsinn !!!
                    // // Man braucht für Blockweises Laden eine entsprechende DB-Funktion !
                    for (int i = 0; i < this.blockSize; i++)
                    {
                        long id = nativeQuery.getSingleResult().longValue();

                        this.idQueue.offer(id);
                    }
                }
                catch (Exception ex)
                {
                    throw ex;
                }

                // try (Statement statement = session.connection().createStatement())
                // {
                // // Das hier ist natürlich Blödsinn !!!
                // // Man braucht für Blockweises Laden eine entsprechende DB-Funktion !
                // for (int i = 0; i < this.blockSize; i++)
                // {
                // try (ResultSet resultSet = statement.executeQuery("call next value for " + this.sequenceName))
                // {
                // long id = resultSet.getLong(1);
                //
                // this.idQueue.offer(id);
                // }
                // }
                // }
                // catch (SQLException sex)
                // {
                // // LOGGER.error(null, sex);
                // throw new HibernateException(sex);
                //
                // // return 0;
                // }
            }

            Long id = this.idQueue.poll();

            return id;
        }
    }
}
