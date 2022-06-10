package com.ericsson.isf.iva.jwt.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component("ResponseFilters")
@Configuration
@Order(1)
public class ResponseFilter extends GenericFilterBean{
	
	@Autowired
	private JoseResponseGenerator joseResponse;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String path = servletRequest.getServletPath();	
		//chain.doFilter(request,response);
		if( path.matches("/internal/parseSignedAndEncrypted")
				|| path.matches("/actuator/refresh")
				|| path.matches("/restart")) {
			chain.doFilter(request,response);
		}
		else {
			JoseHttpResponseWrapper joseHttpResponse= new JoseHttpResponseWrapper((HttpServletResponse) response);
			
			chain.doFilter(request, joseHttpResponse);

			String servletResponse = joseHttpResponse.getCaptureAsString();
			
			if(servletResponse != null) {
				String encrypted =joseResponse.generateSignedResponse(servletResponse);
				response.setContentLength(encrypted.length());
				response.getWriter().print(encrypted);
			}
		}
	}

}
