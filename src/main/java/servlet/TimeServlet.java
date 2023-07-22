package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.time.ZoneId;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String UTC = "UTC";
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {

        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String timezone = req.getParameter("timezone");

        timezone = (timezone == null) ? getCookiesLastTimezone(req)  : timezone.replaceAll(" ", "+");

        setCookie(resp, "lastTimezone", timezone);

        String localtime = ZonedDateTime.now(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X"))
                .replaceAll("Z", "");

        resp.setContentType("text/html; charset=utf-8");

        Context context = new Context();
        context.setVariable("time", localtime);
        engine.process("time", context, resp.getWriter());
        resp.getWriter().close();
    }

    public void setCookie(HttpServletResponse resp, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
    }

    public String getCookiesLastTimezone(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies == null) {

            return UTC;

        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("lastTimezone"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(UTC);
    }
}
