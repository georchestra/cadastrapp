package org.georchestra.cadastrapp.providers;

import org.apache.commons.lang3.StringUtils;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *  Log all request
 * @author Pierre Jégo
 *
 */
@Component
public class CadastrappInterceptor implements HandlerInterceptor  {
	
	final Logger logger = LoggerFactory.getLogger(CadastrappInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        final String rolesList = request.getHeader(CadastrappConstants.HTTP_HEADER_ROLES);
		final String userName = request.getHeader(CadastrappConstants.HTTP_HEADER_USERNAME);
        final String org = request.getHeader(CadastrappConstants.HTTP_HEADER_ORGANISME);

        // Add contexte information, used in logs and to limit access to data
		MDC.put(CadastrappConstants.HTTP_HEADER_USERNAME, userName);
		MDC.put(CadastrappConstants.HTTP_HEADER_ROLES, rolesList);
        MDC.put(CadastrappConstants.HTTP_HEADER_ORGANISME, org);
		MDC.put("uri", request.getRequestURI());	
	
		logger.info("Incoming request");
		
		if(logger.isDebugEnabled()){
			logger.debug("Parameter list : " + StringUtils.join(request.getParameterMap()));
		}
        return true;
    }


    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {

        logger.info("Response");
        MDC.remove(CadastrappConstants.HTTP_HEADER_USERNAME);
		MDC.remove(CadastrappConstants.HTTP_HEADER_ROLES);
        MDC.remove(CadastrappConstants.HTTP_HEADER_ORGANISME);
		MDC.remove("uri");	
    }

}
