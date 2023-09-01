// Created: 26.06.2018
package de.freese.j2ee.liberty.config.jta;

import java.io.Serial;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

import com.ibm.tx.jta.TransactionManagerFactory;
import com.ibm.tx.jta.UserTransactionFactory;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

/**
 * @author Thomas Freese
 * <a href="https://hibernate.atlassian.net/browse/HHH-10388">Hibernate Bug</a>
 */
public class WebSphereLibertyJtaPlatform extends AbstractJtaPlatform {
    @Serial
    private static final long serialVersionUID = 8169121135033680852L;

    public WebSphereLibertyJtaPlatform() {
        super();
    }

    @Override
    protected TransactionManager locateTransactionManager() {
        return TransactionManagerFactory.getTransactionManager();
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return UserTransactionFactory.getUserTransaction();
    }
}
