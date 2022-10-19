//// Created: 26.06.2018
// package de.freese.j2ee.liberty.config.jta;
//
// import jakarta.transaction.UserTransaction;
//
// /**
// * @see <a href="https://hibernate.atlassian.net/browse/HHH-10388">Hibernate Bug</a>
// */
// * @author Thomas Freese
// */
// public class WebSphereLibertyJtaPlatform extends AbstractJtaPlatform
// {
// /**
// * Erstellt ein neues {@link WebSphereLibertyJtaPlatform} Object.
// */
// public WebSphereLibertyJtaPlatform()
// {
// super();
// }
//
// @Override
// protected TransactionManager locateTransactionManager()
// {
// return TransactionManagerFactory.getTransactionManager();
// }
//
// @Override
// protected UserTransaction locateUserTransaction()
// {
// return UserTransactionFactory.getUserTransaction();
// }
// }
