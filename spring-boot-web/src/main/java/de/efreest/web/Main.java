// Erzeugt: 10.06.2015
package de.efreest.web;

import javax.faces.webapp.FacesServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import com.sun.faces.config.ConfigureListener;

/**
 * @author Thomas Freese
 */
@SpringBootApplication
public class Main extends SpringBootServletInitializer // implements ServletContextAware
{
    /**
     * @param args String[]
     */
    public static void main(final String[] args)
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
        ctx.registerShutdownHook();
    }

    /**
     * @see org.springframework.boot.context.web.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
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
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.setName("FacesServlet");

        return servletRegistrationBean;
    }

    /**
     * @return {@link ServletListenerRegistrationBean}
     */
    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener()
    {
        return new ServletListenerRegistrationBean<>(new ConfigureListener());
    }

    // @Bean
    // public static ViewScope viewScope()
    // {
    // return new ViewScope();
    // }
    //
    // /**
    // * Allows the use of @Scope("view") on Spring @Component, @Service and @Controller
    // * beans
    // */
    // @Bean
    // public static CustomScopeConfigurer scopeConfigurer()
    // {
    // CustomScopeConfigurer configurer = new CustomScopeConfigurer();
    // Map<String, Object> hashMap = new HashMap<>();
    // hashMap.put("view", viewScope());
    // configurer.setScopes(hashMap);
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
