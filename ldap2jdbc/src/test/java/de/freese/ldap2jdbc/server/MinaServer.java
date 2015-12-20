package de.freese.ldap2jdbc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * @author Thomas Freese
 */
public class MinaServer
{
    /**
     * @param args String[]
     * @throws IOException Falls was schief geht.
     */
    public static void main(final String[] args) throws IOException
    {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        NioProcessor processor = new NioProcessor(executorService);

        final NioSocketAcceptor acceptor = new NioSocketAcceptor(executorService, processor);

        // Charset charset = Charset.forName("US-ASCII");
        // Charset charset = Charset.forName("ISO-8859-1");

        // Prepare the configuration

        acceptor.getFilterChain().addLast("executor", new ExecutorFilter(executorService));
        // acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        // acceptor.getFilterChain().addLast("codec",
        // new ProtocolCodecFilter(new HttpCodecFactory(charset)));

        // Attach the business logic to the server
        acceptor.setHandler(new MinaHandler());

        // Configurate the buffer size and the idle time
        acceptor.getSessionConfig().setReadBufferSize(2048);
        // acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        // Bind
        acceptor.bind(new InetSocketAddress(3899));

        System.out.println("Listening on port 3899");

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            /**
             * @see java.lang.Thread#run()
             */
            @Override
            public void run()
            {
                System.out.println("Shutdown");

                acceptor.unbind();
                executorService.shutdown();

                while (!executorService.isTerminated())
                {
                    try
                    {
                        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                    }
                    catch (InterruptedException ex)
                    {
                        // Ignore
                    }
                }
            }
        });
    }
}
