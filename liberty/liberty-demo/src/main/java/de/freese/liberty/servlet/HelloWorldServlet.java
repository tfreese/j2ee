package de.freese.liberty.servlet;

import java.io.IOException;
import java.io.Serial;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 * @since 07.10.26
 */
@WebServlet("/servlet")
public class HelloWorldServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldServlet.class);

    @Serial
    private static final long serialVersionUID = -2172344298125591532L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        LOGGER.info("Hello World Servlet: {}", req.getRequestURI());

        resp.setContentType("text/plain");
        resp.getWriter().write("Hello World Servlet: " + req.getRequestURI());
    }
}
