package de.uni.passau.fim.mics.ermera.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter that checks, if there is a profile in the session. If not, it redirects to the loginpage
 */
@WebFilter(urlPatterns = "/pages/*")
public class LoginFilter2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String path = httpRequest.getRequestURI();

        if (!path.equals("/pages/login")) {
            if (session.getAttribute("profile") == null) {
                ((HttpServletResponse) response).sendRedirect(((HttpServletResponse) response).encodeRedirectURL("/pages/login"));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
