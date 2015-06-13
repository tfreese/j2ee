/**
 * Created: 13.06.2015
 */

package de.freese.spring.ldap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author Thomas Freese
 */
public class Main
{
    /**
     * @param args String[]
     */
    public static void main(final String[] args)
    {
        // try (AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class))
        // {
        // ctx.registerShutdownHook();
        // }

        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        ctx.registerShutdownHook();
    }

    /**
     * Erstellt ein neues {@link Main} Object.
     */
    public Main()
    {
        super();
    }
}
