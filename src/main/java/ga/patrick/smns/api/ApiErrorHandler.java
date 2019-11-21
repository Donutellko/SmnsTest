package ga.patrick.smns.api;

import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class ApiErrorHandler implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter(
                (HttpServletRequest) request,
                (HttpServletResponse) response,
                chain
        );
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (NestedServletException ex) {
            Throwable e = ex.getRootCause();
            if (e instanceof TransactionSystemException) {
                ConstraintViolationException cve
                        = (ConstraintViolationException) e.getCause().getCause();
                String s = cve.getConstraintViolations().stream()
                        .map((cv) -> cv.getMessage() + ": " + cv.getInvalidValue())
                        .collect(Collectors.joining("; "));
                response.reset();
                response.getWriter().write(s);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    public void destroy() {}
}
