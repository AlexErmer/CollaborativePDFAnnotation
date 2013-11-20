package de.uni.passau.fim.mics.ermera.filter;

import de.uni.passau.fim.mics.ermera.common.MessageUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This filter checks if there is a {@code MessageUtil} in the session. If not one is created.
 */
@WebFilter(urlPatterns = "/pages/*")
public class MessageUtilFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        if (session.getAttribute(MessageUtil.NAME) == null) {
            session.setAttribute(MessageUtil.NAME, new MessageUtil());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
