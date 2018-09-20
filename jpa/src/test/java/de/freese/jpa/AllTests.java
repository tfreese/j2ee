/**
 *
 */
package de.freese.jpa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Thomas Freese
 */
@RunWith(Suite.class)
@SuiteClasses(
{
        TestHibernate.class,
        TestJPA.class,
        TestNativeHSQL.class,
        TestJOOQLPojoGenerator.class,
        TestJOOQL.class,
        TestQuerydslPojoGenerator.class,
        TestQueryDSL.class
})

public class AllTests
{
    /**
     * Erstellt ein neues {@link AllTests} Object.
     */
    public AllTests()
    {
        super();
    }
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
