package br.com.unip.jogodavelha.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface AuthService {

    ResponseEntity<Void> refreshToken(HttpServletResponse response);

}
