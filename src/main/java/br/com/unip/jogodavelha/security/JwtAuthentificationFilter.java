package br.com.unip.jogodavelha.security;

import br.com.unip.jogodavelha.dto.CredenciaisDTO;
import br.com.unip.jogodavelha.security.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static br.com.unip.jogodavelha.constante.Constantes.*;

@Getter
public class JwtAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public JwtAuthentificationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.setAuthenticationFailureHandler(new JwtAuthentificationFailuerHandler());
    }

    public JwtAuthentificationFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager1;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            CredenciaisDTO credenciaisDTO = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credenciaisDTO.getEmail(), credenciaisDTO.getSenha(), new ArrayList<>());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            logger.error(e.getMessage(), e.getCause());
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((UserSecurity) authResult.getPrincipal()).getUsername();
        String token = this.getJwtUtil().generateToken(username);
        response.addHeader(getAUTHORIZATION(), getBEARER() + token);
        response.addHeader(getHEADER(), getAUTHORIZATION());
    }
}
