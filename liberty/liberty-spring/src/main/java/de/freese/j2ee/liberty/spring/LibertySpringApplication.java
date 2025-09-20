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
    static void main(final String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(final SpringApplicationBuilder builder) {
        return builder
                .sources(LibertySpringApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(true)
                .registerShutdownHook(true);

        // .listeners(new ApplicationPidFileWriter("spring-boot-web.pid"))
        // .web(false)
    }

    /**
     * POM:<br>
     * <pre>{@code
     * <packaging>war<packaging>
     * }</pre>
     * Exclude Tomcat from spring-boot-starter-web and set explicitly to provide.<br>
     * All other JEE-Jars are provided.
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return configureApplication(application);
    }
}
