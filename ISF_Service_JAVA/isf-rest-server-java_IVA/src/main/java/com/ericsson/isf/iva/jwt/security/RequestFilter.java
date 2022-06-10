package com.ericsson.isf.iva.jwt.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.ericsson.isf.iva.dao.IvaManagementDAO;
import com.ericsson.isf.iva.exception.ApplicationException;
import com.ericsson.isf.iva.model.JwtUser;
import com.ericsson.isf.iva.profiles.configuration.AppConfig;
import com.ericsson.isf.iva.util.AppConstants;

@Component("RequestFilters")
@Configuration
@Order(2)
public class RequestFilter extends GenericFilterBean {

	@Autowired
	private IvaManagementDAO ivaManagementDAO;

	private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

	@Autowired
	AppConfig appconfig;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		servletResponse.setHeader("Access-Control-Allow-Origin", "*");
		servletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		servletResponse.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, X-Auth-Token,MarketArea ,Signum, Role");
		String xHeader = servletRequest.getHeader("X-Auth-Token");
		String signum = servletRequest.getHeader("Signum");
		String path = servletRequest.getServletPath();

		try {

			if (path.matches("/actuator/refresh") || path.matches("/restart")
					|| path.matches("/internal/parseSignedAndEncrypted") || path.matches("/ivaManagement/testIVA")) {
				chain.doFilter(request, response);
			} else if (appconfig.isSecurityEnabled()) {
				validateXAuthToken(xHeader, servletRequest, servletResponse, chain);
				validateSignum(signum, servletRequest, response, chain);
				chain.doFilter(request,response);
			} else {
				validateSignum(signum, servletRequest, response, chain);
				chain.doFilter(request,response);
			}

		} catch (ApplicationException e) {
			LOG.info("Error : " + e.getMessage());
			servletResponse.setStatus(e.getErrorCode());
			servletResponse.getWriter().write(e.getMessage());
			return;
		} 
		catch (Exception e) {
			LOG.error("Error : " + e.getMessage());
			servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			servletResponse.getWriter().write("Error in ISF IVA, please contact ISF support");
			return;
		}

	}

	private void validateXAuthToken(String xHeader, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		JwtUser jwtUser = null;
		String token = "";
		String path = servletRequest.getServletPath();
		if (StringUtils.isEmpty(xHeader)) {

			if (path.contains("/internal/parseSignedAndEncrypted")) {
				chain.doFilter(servletRequest, servletResponse);
			}

			else {
				if (xHeader == null) {
					LOG.info("Invalid Authorization header.");
					throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
							AppConstants.INVALID_AUTHORIZATION_HEADER);
				}
				if (xHeader.equalsIgnoreCase("")) {
					LOG.info(String.format(AppConstants.PROVIDE_VALID, "Token"));
					throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
							String.format(AppConstants.PROVIDE_VALID, "Token"));
				}

			}

		} else {

			if (xHeader.startsWith("Bearer ")) {
				JwtValidator validator = new JwtValidator();

				token = xHeader.split(StringUtils.SPACE)[1];
				try {
					jwtUser = validator.validate(token);
				} catch (Exception e) {
					LOG.info("Token Is Not Correct !!");
					throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED, "Token Is Not Correct !!");
				}
				if (jwtUser != null) {

					String apiName = getAPIFromRequest(servletRequest);
					Map<String, Object> data = ivaManagementDAO.getAndValidateSource(jwtUser.getOwnerSignum(), token,
							apiName);
					if (data == null) {
						throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
								"Token Got Expired!! or Disabled token is Used to consume the API or Token and API mapping is not correct");
					}

				} else {
					LOG.info(AppConstants.INVALID_AUTHORIZATION_HEADER);
					throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
							AppConstants.INVALID_AUTHORIZATION_HEADER);
				}
			} else {
				LOG.info(String.format(AppConstants.INVALID_AUTHORIZATION_HEADER, "Token"));
				throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
						String.format(AppConstants.PROVIDE_VALID, "Token"));
			}
		}

	}

	private void validateSignum(String signum, HttpServletRequest servletRequest, ServletResponse response,
			FilterChain chain) throws JSONException, IOException, ServletException {
		if (StringUtils.isBlank(signum)) {
			throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
					String.format(AppConstants.PROVIDE_VALID, "signum"));
		} else {

			boolean isEmployeeExistInIsfTable = ivaManagementDAO.isEmployeeExist(signum);
			if (isEmployeeExistInIsfTable) {
				LOG.info(" user Exist in isEmployeeExistInIsfTable: ==== {}", isEmployeeExistInIsfTable);
			} else {
				throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
						String.format(AppConstants.PROVIDE_VALID, "signum"));
			}
		}
//		String apiName = getAPIFromRequest(servletRequest);
//		if (StringUtils.equalsIgnoreCase("/ivaManagement/updateBookingDetailsStatus", apiName)
//				|| StringUtils.equalsIgnoreCase("/ivaManagement/addStepDetailsForFlowChart", apiName)) {
//			MyRequestWrapper myRequestWrapper = new MyRequestWrapper((HttpServletRequest) servletRequest);
//			String body = myRequestWrapper.getBody();
//			JSONObject jsonObj = new JSONObject(StringUtils.isEmpty(body) ? body : body.replaceAll("\\s", ""));
//			String wOID = (String) jsonObj.get("wOID");
//			boolean isSignumExist = ivaManagementDAO.isHeaderSignumExist(wOID, signum);
//			if (!isSignumExist) {
//				throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,
//						String.format(AppConstants.PROVIDE_VALID, "signum"));
//			}
//			chain.doFilter(myRequestWrapper, response);
//	} else {
//		String values=servletRequest.getParameter("serverBotModel");
//			chain.doFilter(servletRequest, response);
//		}
	}

	public static char ASCIIToChar(final int ascii) {
		return (char) ascii;
	}

	public String decrypt(String encrypted) {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		// Decoding URl
		return new String(decoder.decode(encrypted));

	}

	private String getAPIFromRequest(HttpServletRequest request) {
		String uri = request.getRequestURI().trim().replaceAll(AppConstants.DOUBLE_SLASH, AppConstants.SLASH);
		String[] requestTokens = uri.split(AppConstants.SLASH);
		return AppConstants.SLASH + requestTokens[2] + AppConstants.SLASH + requestTokens[3];
	}

}