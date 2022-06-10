package com.ericsson.isf.jwtSecurity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.JwtUser;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.service.RedisService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.google.gson.Gson;

@Component("CustomFilters")
@Configuration
public class CustomFilters extends GenericFilterBean {

	private static final String INVALID_TOKEN = "Invalid Token.";
	private static final String UNABLE_TO_CONNECT_WITH_REDIS = "Unable to connect with Redis!! ";
	private static final String AUTHORIZATION_MESSAGE = "Email is not authorized in ISF!! ";
	private static final String EMAIL_ID = "emailID";
	private static final Pattern VERSION_PATTERN = Pattern.compile("^v[0-9]{1,2}$");

	@Autowired
	/* Bind to bean/pojo */
	private AccessManagementService accessManagementService;
	
	@Autowired
    private ApplicationConfigurations configurations;
	
	
	@Autowired
	private RedisService redisService;

	private static final Logger LOG = LoggerFactory.getLogger(CustomFilters.class);
	private static final String SESSION_ID_NOT_AVAILABLE = "User Data Not available in Redis Cache for SessionId ";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		
		servletResponse.setHeader("Access-Control-Allow-Origin", "*");
		servletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		servletResponse.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, X-Auth-Token,MarketArea ,Signum, Role");
		  
		String path = servletRequest.getServletPath();
		String xHeader = servletRequest.getHeader("X-Auth-Token");
		String referer = servletRequest.getHeader("APIM_Referer");
		// temp changes for logging
		// LOG.info(String.format("Referer: %s ApiName %s", referer,
		// getAPIFromRequest(servletRequest)));
		try {
		if (configurations.getBooleanProperty(ConfigurationFilesConstants.APP_SECURITY_ENABLED, false)) {
				if (referer != null && referer.contains("apim")) {
					String apiName = getAPIFromRequest(servletRequest);
					if (accessManagementService.validateReferer(referer, apiName)) {
						LOG.info(String.format("Referer: %s", referer));
						chain.doFilter(request, response);
					} else {
						servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
								"Referer used to consume the API or Referer and API mapping is not correct.");
						LOG.info("Referer used to consume the API or Referer and API mapping is not correct.");
					}
				} else if (xHeader != null) {
				if (xHeader.startsWith("Bearer ")) {
					JwtValidator validator = new JwtValidator();
					JwtUser jwtUser = null;
						String sessionId = xHeader.split(" ")[1];
					try {
							jwtUser = validator.validate(sessionId);
					} catch (Exception e) {
							servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Is Not Correct !!");
						LOG.info("Token Is Not Correct !!");
					}
					if (jwtUser != null) {
						
						String apiName = getAPIFromRequest(servletRequest);
						Map<String, Object> data = this.accessManagementService
									.getAndValidateSource(jwtUser.getOwnerSignum(), sessionId, apiName);
						if (data != null) {
							chain.doFilter(request, response);
						} else {
							throw new ServletException(
									"Token Got Expired!! or Disabled token is Used to consume the API or Token and API mapping is not correct");
						}
					} else {
							servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
								"JWT Token Epired and Updated at back end !!");
						LOG.info("JWT Token Epired and Updated at back end !!");
					}
					} else if (xHeader.startsWith("Session ")) {
					if (path.matches("/accessManagement/getUserSignumByEmail")
							|| path.matches("/woExecution/pauseAllTaskOnLogout")
							|| path.matches("/accessManagement/saveLogoutDetails")
							|| path.matches("/accessManagement/saveLoginDetails")) {
						chain.doFilter(request, response);
					}else {

							String sessionId = xHeader.split(" ")[1].split("-")[0];
							if (sessionId != null) {
							String empInfo = xHeader.split(" ")[1].split("-")[1];
							empInfo = empInfo.substring(10);
							empInfo = empInfo.substring(0, empInfo.length() - 10);
							String decryptString = decrypt(empInfo);
							List<String> ascii = Arrays.asList(decryptString.split("\\|"));
							Collections.reverse(ascii);
								StringBuilder employeeEmail = new StringBuilder();

							for (String item : ascii){
						    	   char tmpPass = ASCIIToChar(Integer.parseInt(item));
									employeeEmail.append(tmpPass);
						    }
								if (configurations.getBooleanProperty(ConfigurationFilesConstants.REDIS_ISENABLE)) {
									validateUserSessionRedisData(request, response, chain, servletResponse, sessionId,
											employeeEmail);
								} else {
									Map<String, Object> tokenData = this.accessManagementService
											.validateUiToken(sessionId);
							if (tokenData != null) {
										validateEmailFromdb(request, response, chain, servletResponse, employeeEmail);
								} else {
										servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_TOKEN);
										LOG.info(INVALID_TOKEN);
								}
							}

						}
					}
				}
				else if (xHeader.equalsIgnoreCase("Internal")) {
					chain.doFilter(request, response);
				}
				else {
					servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header or Token Expires");
					LOG.info("Invalid Authorization header or Token Expires");
				}
			}else {
				//String referer = servletRequest.getHeader("Referer");
				if (	path.contains("swagger")
						|| path.matches("/externalInterface/checkIsfHealth")
						|| path.matches("/externalInterface/generateWOrkOrder")
						|| path.matches("/accessManagement/getUserSignumByEmail") 
							|| path.matches("/accessManagement/getUserProfileByEmail") || path.contains("api-docs")
							|| path.contains("csrf") || path.matches("^/$")
						|| (servletRequest.getMethod().equalsIgnoreCase("OPTIONS"))
							|| path.contains("/dbJobs/startJob") || path.contains("/dbJobs/stopJob")
						|| path.contains("/appUtil/getListOfUnsentMails")
						|| path.matches("/accessManagement/generateCustomAPIToken")
						|| path.matches("/externalInterface/subscribeIsfFeeds")) {
					chain.doFilter(request, response);
				} 
				else {
					if(referer !=null) {
						String apiName = getAPIFromRequest(servletRequest);
						if(accessManagementService.validateReferer(referer, apiName)) {
							chain.doFilter(request, response);
						}
						else {
							servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header.");
							LOG.info("Missing Authorization header.");
						}
					}
					else {
						servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header.");
						LOG.info("Missing Authorization header.");
					}
				}
			} 
		}else {
			chain.doFilter(request, response);
		}
		} catch (Exception e) {
			LOG.info("Error : "+e.getMessage());
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
		}
	}
	
	private void validateEmailFromdb(ServletRequest request, ServletResponse response, FilterChain chain,
			HttpServletResponse servletResponse, StringBuilder employeeEmail) throws IOException, ServletException {
		boolean data = this.accessManagementService
				.validateEmployeeEmailAndSignum(employeeEmail.toString());
		if (data) {
			chain.doFilter(request, response);
		} else {
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTHORIZATION_MESSAGE);
			LOG.info(AUTHORIZATION_MESSAGE);
		}
	}

	private void validateRedisCachedEmail(ServletRequest request, ServletResponse response, FilterChain chain,
			HttpServletResponse servletResponse, StringBuilder employeeEmail, Object userSessionData)
			throws IOException, ServletException {
		String userSessionDataInString = new Gson().toJson(userSessionData);
		JSONObject userSessionDataJsonObject = new JSONObject(userSessionDataInString);
		if (StringUtils.equalsIgnoreCase(userSessionDataJsonObject.get(EMAIL_ID).toString().trim(),
				StringUtils.trim(employeeEmail.toString()))) {
			/*
			 * LOG.info("Email Id " + userSessionDataJsonObject.get(EMAIL_ID) +
			 * " is authorized in ISF from Redis cache!! ");
			 */
			chain.doFilter(request, response);
		} else {
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTHORIZATION_MESSAGE);
			LOG.info(AUTHORIZATION_MESSAGE);
		}
	}

	public static char ASCIIToChar(final int ascii){
		return  (char)ascii ;
	}
    
    public String decrypt(String encrypted) {
    	Base64.Decoder decoder = Base64.getUrlDecoder();  
    // Decoding URl  
    	String dStr = new String(decoder.decode(encrypted));  
		return dStr;  
    
    }
    
	
	private String getAPIFromRequest(HttpServletRequest request) {
		String uri = request.getRequestURI().trim().replaceAll(AppConstants.FORWARD_DOUBLE_SLASH,
				AppConstants.FORWARD_SLASH);
		String[] requestTokens = uri.split(AppConstants.FORWARD_SLASH);
		Matcher matcher = VERSION_PATTERN .matcher(requestTokens[3]);
		if(matcher.matches() && requestTokens.length>4) {
			return (new StringBuilder()).append(AppConstants.FORWARD_SLASH).append(requestTokens[2])
					.append(AppConstants.FORWARD_SLASH).append(requestTokens[3]).append(AppConstants.FORWARD_SLASH).append(requestTokens[4]).toString();
		}
		return (new StringBuilder()).append(AppConstants.FORWARD_SLASH).append(requestTokens[2])
				.append(AppConstants.FORWARD_SLASH).append(requestTokens[3]).toString();
	}

	private void validateUserSessionRedisData(ServletRequest request, ServletResponse response, FilterChain chain,
			HttpServletResponse servletResponse, String sessionId, StringBuilder employeeEmail)
			throws IOException, ServletException {
		try {
			Object userSessionData = this.redisService.getEmployeeSessionData(sessionId);
			if (userSessionData != null) {
				validateRedisCachedEmail(request, response, chain, servletResponse, employeeEmail, userSessionData);
			} else {
				servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTHORIZATION_MESSAGE);
				LOG.info(SESSION_ID_NOT_AVAILABLE+sessionId);
			}
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Unable to connect with Redis+++++++++!! "+e.getMessage());
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNABLE_TO_CONNECT_WITH_REDIS);
		}
	}
}