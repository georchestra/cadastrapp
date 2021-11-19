package org.georchestra.cadastrapp.providers;

import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.Map.Entry;

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
		
        // Add debug information (duration and parameters list)
		if(logger.isDebugEnabled()){
            
            // used for duration
            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime); 

            // list of params
            StringBuilder paramsSBuilder = new StringBuilder("Parameter list : { ");
            for (Entry<String, String[]> entry : request.getParameterMap().entrySet()){
                paramsSBuilder.append(entry.getKey()).append(":");
                paramsSBuilder.append(Arrays.toString(entry.getValue())).append("--");
              }
            paramsSBuilder.append(" }");
           logger.debug(paramsSBuilder.toString());
		}
        return true;
    }

    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {

         // Add duration 
		if(logger.isDebugEnabled()){        
            long executeTime = System.currentTimeMillis() - (Long)request.getAttribute("startTime");
            logger.debug("Request handle in " + executeTime + "ms");
        }     

        logger.info("Send response");

        MDC.remove(CadastrappConstants.HTTP_HEADER_USERNAME);
		MDC.remove(CadastrappConstants.HTTP_HEADER_ROLES);
        MDC.remove(CadastrappConstants.HTTP_HEADER_ORGANISME);
		MDC.remove("uri");	
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
            Object handler, Exception ex) throws Exception {
        
         // Add duration 
		if(logger.isDebugEnabled()){        
            long executeTime = System.currentTimeMillis() - (Long)request.getAttribute("startTime");
            logger.debug("Request finished in " + executeTime + "ms");

            logger.debug("Make sure to clear MDC information");
        }     
        MDC.clear();	
    }

}
