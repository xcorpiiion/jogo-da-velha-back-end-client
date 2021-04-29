package br.com.unip.jogodavelha.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthentificationFailuerHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().append(toJson());
    }

    private String toJson() {
        long date = new Date().getTime();
        return "{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", " + "\"path\": \"/login\"}";
    }
}
