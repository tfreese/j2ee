// Created: 11.07.2018
package de.freese.jpa;

import java.io.Serial;
import java.lang.reflect.Member;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Properties;
import java.util.Queue;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IncrementGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate SequenceGenerator, der ganze Blöcke von Sequence IDs holt.<br>
 * Das hier ist natürlich Blödsinn!<br>
 * Man braucht für Blockweises Laden eine entsprechende DB-Funktion!
 *
 * @author Thomas Freese
 * @see IncrementGenerator
 */
public class BlockSequenceGenerator implements IdentifierGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockSequenceGenerator.class);

    @Serial
    private static final long serialVersionUID = -8510962789727550315L;

    private final transient Queue<Long> idQueue = new LinkedList<>();
    private final String sequenceName;
    private int blockSize;

    public BlockSequenceGenerator(final BlockSequence config, final Member annotatedMember) {
        super();

        sequenceName = Objects.requireNonNull(config.name(), "sequenceName required");

        if (config.blockSize() < 1) {
            throw new IllegalArgumentException("blockSize < 1: " + blockSize);
        }

        blockSize = config.blockSize();
    }

    @Override
    public void configure(final GeneratorCreationContext creationContext, final Properties parameters) throws MappingException {
        IdentifierGenerator.super.configure(creationContext, parameters);

        // sequenceName = ConfigurationHelper.getString("sequenceName", parameters, (String) null);
        // blockSize = ConfigurationHelper.getInt("blockSize", parameters, 1);
        //
        // if (sequenceName == null || sequenceName.isBlank()) {
        //     throw new MappingException("sequenceName required");
        // }
        //
        // if (blockSize < 1) {
        //     throw new MappingException("blockSize >= 1 required");
        // }
    }

    @Override
    public synchronized Object generate(final SharedSessionContractImplementor session, final Object object) throws HibernateException {
        LOGGER.debug("Retrieve next {} IDs from Sequence '{}'", blockSize, sequenceName);

        synchronized (idQueue) {
            if (idQueue.isEmpty()) {
                session.doWork(connection -> {
                    try (Statement statement = connection.createStatement()) {
                        for (int i = 0; i < blockSize; i++) {
                            try (ResultSet resultSet = statement.executeQuery("call next value for " + sequenceName)) {
                                resultSet.next();
                                idQueue.offer(resultSet.getLong(1));
                            }
                        }
                    }
                });
            }

            return idQueue.poll();
        }
    }
}
