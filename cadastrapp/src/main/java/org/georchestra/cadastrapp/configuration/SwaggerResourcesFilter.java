package org.georchestra.cadastrapp.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Swagger UI is served at the webapp root, while Spring MVC is mapped to
 * /services. Forward its discovery requests to that servlet.
 */
public class SwaggerResourcesFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        request.getRequestDispatcher("/services" + path).forward(request, response);
    }

    @Override
    public void destroy() {
    }
}
