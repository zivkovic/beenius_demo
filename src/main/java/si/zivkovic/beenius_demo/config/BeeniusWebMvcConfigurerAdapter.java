package si.zivkovic.beenius_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import si.zivkovic.beenius_demo.interceptor.RequestCounterInterceptor;

@Component
public class BeeniusWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	private RequestCounterInterceptor requestCounterInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestCounterInterceptor);
	}

}
