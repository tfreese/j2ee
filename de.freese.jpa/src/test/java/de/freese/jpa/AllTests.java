/**
 *
 */
package de.freese.jpa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import de.freese.jpa.jdbc.TestJOOQL;
import de.freese.jpa.jdbc.TestNativeHSQL;
import de.freese.jpa.jdbc.TestQueryDSL;

/**
 * @author Thomas Freese
 */
@RunWith(Suite.class)
@SuiteClasses(
{
            TestHibernate.class, TestJPA.class, TestNativeHSQL.class, TestJOOQL.class, TestQueryDSL.class
})
public class AllTests
{
    // /**
    // * In der Methode werden alle Testklassen registriert die durch JUnit aufgerufen werden sollen.
    // *
    // * @return {@link Test}
    // */
    // public static Test suite()
    // {
    // TestSuite suite = new TestSuite("de.freese.jdbc");
    //
    // suite.addTest(new JUnit4TestAdapter(TestAbstractJdbcDao.class));
    //
    // return suite;
    // }
}
