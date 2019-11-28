package ga.patrick.smns.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApiErrorHandler implements Filter {

    private ObjectMapper objectMapper = new ObjectMapper();

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

                List<String> constraints = cve.getConstraintViolations().stream()
                        .map((cv) -> cv.getMessage() + ": " + cv.getInvalidValue())
                        .collect(Collectors.toList());

                String payload = formErrorJson("Constraint violation.", constraints);

                response.reset();
                response.getWriter().write(payload);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                throw ex;
            }
        }
    }

    private String formErrorJson(String desc, List<String> violations) throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("success", false);
        payload.put("message", desc);
        payload.put("violated", violations);
        return objectMapper.writeValueAsString(payload);
    }

    @Override
    public void destroy() {}
}
