package com.elcom.plus.mecall.config;

import com.elcom.plus.mecall.config.db.LoadConfig;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
//        appContext.register(AppConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher", new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // UTF8 Charactor Filter.
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);

        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");

        try {
            LoadConfig lo = new LoadConfig(); // Load data từ file config và log.xml
            lo.checkConfig();
            lo.loadConfig();

            DOMConfigurator.configureAndWatch(Config.pathFileConfigLog); // Cấu hình file Log

            System.out.println("Khoi dong web napas thanh cong!");
        } catch (Throwable e) {
            System.out.println(e);
        }
    }
}
