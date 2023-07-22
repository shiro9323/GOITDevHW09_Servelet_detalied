package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String UTC = "UTC";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String timezone = req.getParameter("timezone");

        timezone = (timezone == null) ? UTC : timezone.replaceAll(" ", "+");

        String localtime = ZonedDateTime.now(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X"))
                .replaceAll("Z", "");

        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(localtime);
        resp.getWriter().close();
    }
}
