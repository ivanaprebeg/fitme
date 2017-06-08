package hr.fitme.config;

import javax.servlet.Filter;

import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { new DelegatingFilterProxy("springSecurityFilterChain") };
    }
	
//	@Override
//	protected Class[] getRootConfigClasses() {
//		return new Class[] { WebConfig.class };
//	}
	
	@Override
	protected Class[] getRootConfigClasses() {
		return new Class[] {WebConfig.class, WebSecurityConfig.class};
	}
	
	@Override
	protected Class[] getServletConfigClasses() {
		return new Class[] {WebConfig.class, WebSecurityConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}