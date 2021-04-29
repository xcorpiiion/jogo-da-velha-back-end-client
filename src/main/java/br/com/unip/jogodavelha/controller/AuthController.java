package br.com.unip.jogodavelha.controller;

import br.com.unip.jogodavelha.service.AuthService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Getter
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/refreshToken")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        return this.getAuthService().refreshToken(response);
    }

}
