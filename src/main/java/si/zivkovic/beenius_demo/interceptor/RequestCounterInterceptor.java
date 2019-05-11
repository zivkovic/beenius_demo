package si.zivkovic.beenius_demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestCounterInterceptor implements HandlerInterceptor {

	private static final int FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000;
	private static final String REQUEST_COUNTER_FILE = "output/request_counter.txt";

	private final Map<String, Map<String, Integer>> requestCounterMap = new HashMap<>();
	private long lastSavedToFileTime = System.currentTimeMillis();

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		final String requestURI = httpServletRequest.getRequestURI();
		final String requestMethod = httpServletRequest.getMethod();

		if(!requestCounterMap.containsKey(requestURI)) {
			requestCounterMap.put(requestURI, new HashMap<>());
		}
		final Map<String, Integer> innerRequestCounterMap = requestCounterMap.get(requestURI);
		if(!innerRequestCounterMap.containsKey(requestMethod)) {
			innerRequestCounterMap.put(requestMethod, 1);
			return;
		}
		innerRequestCounterMap.put(requestMethod, innerRequestCounterMap.get(requestMethod) + 1);

		if(lastSavedToFileTime + FIVE_MINUTES_IN_MILLIS < System.currentTimeMillis()) {
			saveRequestCounterDataToFile();
		}
	}

	private void saveRequestCounterDataToFile() {
		try(final BufferedWriter bw = new BufferedWriter(new FileWriter(REQUEST_COUNTER_FILE))) {
			for(final Map.Entry<String, Map<String, Integer>> outerMapEntry: requestCounterMap.entrySet()) {
				bw.write(outerMapEntry.getKey() + ":");
				bw.newLine();
				for(final Map.Entry<String, Integer> innerMapEntry: outerMapEntry.getValue().entrySet()) {
					bw.write("\t" + innerMapEntry.getKey() + ": " + innerMapEntry.getValue());
					bw.newLine();
				}
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lastSavedToFileTime = System.currentTimeMillis();
	}

}
