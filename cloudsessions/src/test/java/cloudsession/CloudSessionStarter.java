package cloudsession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * <a href="http://localhost:8088/session">session-demo</a>
 *
 * @author Thomas Freese
 */
@ServletComponentScan
@SpringBootApplication
public class CloudSessionStarter {
    public static void main(String[] args) {
        SpringApplication.run(CloudSessionStarter.class, args);
    }

    //    /**
    //     * Ohne @ServletComponentScan, mit WebServlet-Annotation.
    //     */
    //    @Bean
    //    public Servlet session() {
    //        return new NonStickySessionServlet();
    //    }
    //
    //    /**
    //     * Ohne @ServletComponentScan, ohne WebServlet-Annotation.
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
    //
    //    /**
    //     * Ohne @ServletComponentScan, ohne WebServlet-Annotation mit programmatischer Configuration.
    //     */
    //    @Configuration
    //    public class ConfigureWeb implements ServletContextInitializer {
    //
    //        @Override
    //        public void onStartup(ServletContext servletContext) throws ServletException {
    //            registerServlet(servletContext);
    //        }
    //
    //        private void registerServlet(ServletContext servletContext) {
    //            ServletRegistration.Dynamic serviceServlet = servletContext.addServlet("NonStickySessionServlet", new NonStickySessionServlet());
    //
    //            serviceServlet.addMapping("/session");
    //            serviceServlet.setLoadOnStartup(1);
    //        }
    //    }
}
