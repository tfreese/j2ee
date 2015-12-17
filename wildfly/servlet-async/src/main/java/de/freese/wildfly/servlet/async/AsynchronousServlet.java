/**
 * Created on 13.12.2015 13:31:50
 */
package de.freese.wildfly.servlet.async;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A simple asynchronous servlet taking advantage of features added in 3.0.
 * </p>
 * <p>
 * The servlet is registered and mapped to /AsynchronousServlet using the {@link WebServlet} annotation. The {@link LongRunningService} is injected by CDI.
 * </p>
 * <p>
 * It shows how to detach the execution of a long-running task from the request processing thread, so the thread is free to serve other client requests. The
 * long-running tasks are executed using a dedicated thread pool and create the client response asynchronously using the {@link AsyncContext}.
 * </p>
 * <p>
 * A long-running task in this context does not refer to a computation intensive task executed on the same machine but could for example be contacting a
 * third-party service that has limited resources or only allows for a limited number of concurrent connections. Moving the calls to this service into a
 * separate and smaller sized thread pool ensures that less threads will be busy interacting with the long-running service and that more requests can be served
 * that do not depend on this service.
 * </p>
 *
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@SuppressWarnings("serial")
@WebServlet(value = "/AsynchronousServlet", asyncSupported = true)
public class AsynchronousServlet extends HttpServlet
{
    /**
     *
     */
    @Inject
    private LongRunningService longRunningService = null;

    /**
     * Erstellt ein neues {@link AsynchronousServlet} Object.
     */
    public AsynchronousServlet()
    {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
    {
        // Here the request is put in asynchronous mode
        AsyncContext asyncContext = req.startAsync();

        // This method will return immediately when invoked,
        // the actual execution will run in a separate thread.
        this.longRunningService.readData(asyncContext);
    }
}
