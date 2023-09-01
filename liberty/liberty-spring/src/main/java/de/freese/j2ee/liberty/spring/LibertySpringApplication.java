package de.freese.j2ee.liberty.spring;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Thomas Freese
 */
@SpringBootApplication
public class LibertySpringApplication extends SpringBootServletInitializer {
    public static void main(final String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(final SpringApplicationBuilder builder) {
        //@formatter:off
        return builder
            .sources(LibertySpringApplication.class)
            .bannerMode(Banner.Mode.OFF)
            .headless(true)
            .registerShutdownHook(true);
        //@formatter:on
        // .listeners(new ApplicationPidFileWriter("spring-boot-web.pid"))
        // .web(false)
    }

    /**
     * POM:<br>
     * &lt;packaging>&gt;war&lt;/packaging&gt;<<br>
     * Tomcat aus spring-boot-starter-web excludieren und explizit auf provided setzen.<br>
     * Alle anderen J2EE-Jars auf provided setzen.
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return configureApplication(application);
    }
}
