package cloudsession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

/**
 * <a href="http://localhost:8088/session">session-demo</a>
 *
 * @author Thomas Freese
 */
@ServletComponentScan
@SpringBootApplication
public class CloudSessionStarter {
    static void main(final String[] args) {
        SpringApplication.run(CloudSessionStarter.class, args);
    }

    //    /**
    //     * Without @ServletComponentScan, with WebServlet-Annotation.
    //     */
    //    @Bean
    //    public Servlet session() {
    //        return new NonStickySessionServlet();
    //    }
    //
    //    /**
    //     * Without @ServletComponentScan, without WebServlet-Annotation.
    //     */
    //    @Bean
    //    public ServletRegistrationBean<HttpServlet> nonStickySessionServlet() {
    //        ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
    //        servRegBean.setServlet(new NonStickySessionServlet());
    //        servRegBean.addUrlMappings("/session");
    //        servRegBean.setLoadOnStartup(1);
    //
    //        return servRegBean;
    //    }
    //
    //    /**
    //     * Without @ServletComponentScan, without WebServlet-Annotation with programmatic Configuration.
    //     */
    //    @Configuration
    //    public class ConfigureWeb implements ServletContextInitializer {
    //        @Override
    //        public void onStartup(final ServletContext servletContext) throws ServletException {
    //            registerServlet(servletContext);
    //        }
    //
    //        private void registerServlet(final ServletContext servletContext) {
    //            ServletRegistration.Dynamic serviceServlet = servletContext.addServlet("NonStickySessionServlet", new NonStickySessionServlet());
    //
    //            serviceServlet.addMapping("/session");
    //            serviceServlet.setLoadOnStartup(1);
    //        }
    //    }
}
