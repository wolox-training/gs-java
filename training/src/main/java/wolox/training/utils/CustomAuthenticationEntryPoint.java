package wolox.training.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
        AuthenticationException authException) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);
        res.getWriter()
            .write(String.valueOf(new JSONObject()
                .put("timestamp", System.currentTimeMillis())
                .put("status", 403)
                .put("message", "Access denied"))
            );

    }
}
