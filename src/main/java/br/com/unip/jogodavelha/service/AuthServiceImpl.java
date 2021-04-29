package br.com.unip.jogodavelha.service;

import br.com.unip.jogodavelha.repository.ClienteRepository;
import br.com.unip.jogodavelha.service.exception.AuthorizationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.unip.jogodavelha.security.UserSecurity;
import br.com.unip.jogodavelha.security.utils.JwtUtil;

import javax.servlet.http.HttpServletResponse;

import static br.com.unip.jogodavelha.constante.Constantes.*;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@Getter
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        log.info("Atualiazando token...");
        UserSecurity userSecurity = UserService.authenticated();
        if (userSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }
        String token = this.getJwtUtil().generateToken(userSecurity.getUsername());
        response.addHeader(getHEADER(), getBEARER() + token);
        response.addHeader(getHEADER(), getAUTHORIZATION());
        log.info("Token atualizado");
        return noContent().build();
    }

}
