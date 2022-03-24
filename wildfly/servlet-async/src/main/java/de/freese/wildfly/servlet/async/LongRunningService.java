// Created on 13.12.2015 13:32:50
package de.freese.wildfly.servlet.async;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.servlet.AsyncContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Stateless
public class LongRunningService
{
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /**
     * The use of {@link Asynchronous} causes this EJB method to be executed asynchronously, by a different thread from a dedicated, container managed thread
     * pool.
     *
     * @param asyncContext the context for a suspended Servlet request that this EJB will complete later.
     */
    @Asynchronous
    public void readData(final AsyncContext asyncContext)
    {
        try
        {
            // This is just to simulate a long-running operation for demonstration purposes.
            Thread.sleep(5000L);

            try (PrintWriter writer = asyncContext.getResponse().getWriter())
            {
                writer.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }

            asyncContext.complete();
        }
        catch (Exception ex)
        {
            this.logger.error(null, ex);
        }
    }
}
