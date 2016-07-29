// Erzeugt: 10.06.2015
package de.freese.web;

import javax.faces.webapp.FacesServlet;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import com.sun.faces.config.ConfigureListener;

/**
 * @author Thomas Freese
 */
@SpringBootApplication
// @Configuration
// @EnableAutoConfiguration(exclude = // Spring MVC ausschalten, dann gehen die Endpoints aber nicht mehr.
// {
// WebMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class
// })
// @ComponentScan("de.freese.web.controller")
// @PropertySource("classpath:application.properties") // Default, wird automatisch geladen
public class Main extends SpringBootServletInitializer // implements ServletContextAware
{
    /**
     * @param args String[]
     */
    public static void main(final String[] args)
    {
        // ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
        // ctx.registerShutdownHook();

        // SpringApplication app = new SpringApplication(Main.class);
        // app.setBannerMode(Banner.Mode.OFF);
        // app.setRegisterShutdownHook(true);
        //
        // ConfigurableApplicationContext ctx = app.run(args);
        //
        // @formatter:off
        new SpringApplicationBuilder(Main.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(true)
                .registerShutdownHook(true)
                .run(args);
        // @formatter:on
    }

    /**
     * @see org.springframework.boot.web.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application)
    {
        return application.sources(new Class<?>[]
        {
                Main.class, Initializer.class
        });
    }

    /**
     * @return {@link ServletRegistrationBean}
     */
    @Bean
    public ServletRegistrationBean facesServletRegistration()
    {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new FacesServlet(), "*.xhtml");
        servletRegistrationBean.setName("Faces Servlet");
        servletRegistrationBean.setLoadOnStartup(1);

        return servletRegistrationBean;
    }

    /**
     * web.xml Listener
     *
     * @return {@link ServletListenerRegistrationBean}
     */
    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener()
    {
        return new ServletListenerRegistrationBean<>(new ConfigureListener());
    }

    /**
     * web.xml Listener<br>
     * Verhindert Meldungen wie FacesRequestAttributes#registerDestructionCallback - Could not register destruction callback ...
     *
     * @return {@link ServletListenerRegistrationBean}
     */
    @Bean
    public ServletListenerRegistrationBean<RequestContextListener> requestContextListener()
    {
        return new ServletListenerRegistrationBean<>(new RequestContextListener());
    }

    // /**
    // *
    // * @return EmbeddedServletContainerFactory
    // */
    // @Bean
    // public EmbeddedServletContainerFactory embeddedServletContainerFactory()
    // {
    // JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
    // factory.addServerCustomizers(server ->
    // {
    //// QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
    // ThreadPool threadPool = server.getThreadPool();
    //
    // if (threadPool instanceof QueuedThreadPool)
    // {
    // ((QueuedThreadPool) threadPool).setMinThreads(4);
    // ((QueuedThreadPool) threadPool).setMaxThreads(4);
    // }
    // else if (threadPool instanceof ExecutorThreadPool)
    // {
    // }
    // });
    //
    // return factory;
    // }
    // /**
    // * Allows the use of @Scope("view") on Spring @Component, @Service and @Controller
    // * beans
    // */
    // @Bean
    // public static CustomScopeConfigurer scopeConfigurer()
    // {
    // CustomScopeConfigurer configurer = new CustomScopeConfigurer();
    // Map<String, Object> hashMap = new HashMap<>();
    // hashMap.put("view", new ViewScope());
    // configurer.setScopes(hashMap);
    //
    // return configurer;
    // }
    //
    // @Bean
    // public ViewResolver getViewResolver()
    // {
    // InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    // resolver.setPrefix("/templates/");
    // resolver.setSuffix(".xhtml");
    // return resolver;
    // }
}
