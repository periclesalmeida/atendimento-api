package br.com.periclesalmeida.atendimento.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		if ("/oauth/token".equalsIgnoreCase(httpServletRequest.getRequestURI())
				&& "refresh_token".equals(httpServletRequest.getParameter("grant_type"))
				&& httpServletRequest.getCookies() != null) {

			for (Cookie cookie : httpServletRequest.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue();
					httpServletRequest = new MyServletRequestWrapper(httpServletRequest, refreshToken);
				}
			}
		}

		chain.doFilter(httpServletRequest, response);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	static class MyServletRequestWrapper extends HttpServletRequestWrapper {
		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			HashMap<String, String[]> map = new HashMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			return map;
		}
	}

}
