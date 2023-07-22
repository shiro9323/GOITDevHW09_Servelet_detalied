package servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        String timezone = req.getParameter("timezone");
        if (timezone == null || isValidTimezone(timezone.replaceAll(" ", "+"))) {
            chain.doFilter(req, resp);
        } else {
            resp.setContentType("text/html; charset=utf-8");
            resp.getWriter().write("ERROR 400: Invalid timezone");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().close();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private boolean isValidTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
