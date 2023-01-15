package cloudsession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author Thomas Freese
 */
@ServletComponentScan
@SpringBootApplication
public final class CloudSessionStarter
{
    public static void main(String[] args)
    {
        SpringApplication.run(CloudSessionStarter.class, args);
    }

    //    /**
    //     * Hiermit dann ohne @ServletComponentScan.
    //     */
    //    @Bean
    //    public ServletRegistrationBean<HttpServlet> nonStickySessionServlet()
    //    {
    //        ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
    //        servRegBean.setServlet(new NonStickySessionServlet());
    //        servRegBean.addUrlMappings("/session");
    //        servRegBean.setLoadOnStartup(1);
    //
    //        return servRegBean;
    //    }

    private CloudSessionStarter()
    {
        super();
    }
}
