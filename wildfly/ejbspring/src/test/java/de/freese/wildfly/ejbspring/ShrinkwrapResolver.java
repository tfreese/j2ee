// Erzeugt: 15.12.2015
package de.freese.wildfly.ejbspring;

import java.io.File;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * <pre>
 * &lt;!-- For managing and wrapping of maven dependencies used by the application under test --&gt;
 * &lt;dependency&gt;
 * 	&lt;groupId&gt;org.jboss.shrinkwrap.resolver&lt;/groupId&gt;
 * 	&lt;artifactId&gt;shrinkwrap-resolver-impl-maven&lt;/artifactId&gt;
 * 	&lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * </pre>
 * <p>
 *
 * @author Thomas Freese (AuVi)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShrinkwrapResolver
{
    /**
     *
     */
    public ShrinkwrapResolver()
    {
        super();
    }

    /**
     *
     */
    @Test
    public void test01CompileAndRuntimeDependencies()
    {
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies().resolve().withTransitivity().asFile();

        Assert.assertNotNull(libs);

        // Sollte org.slf4j:slf4j-api und 6 Libs von Spring sein.
        Assert.assertTrue(libs.length == 7);
        Assert.assertTrue(libs[0].getName().startsWith("slf4j-api"));
    }

    /**
     *
     */
    @Test
    public void test02Slf4jDependency()
    {
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.slf4j:slf4j-simple").withTransitivity().asFile();

        Assert.assertNotNull(libs);

        // Sollte nur org.slf4j:slf4j-simple und org.slf4j:slf4j-api sein.
        Assert.assertTrue(libs.length == 2);
        Assert.assertTrue(libs[0].getName().startsWith("slf4j-simple"));
        Assert.assertTrue(libs[1].getName().startsWith("slf4j-api"));
    }
}
