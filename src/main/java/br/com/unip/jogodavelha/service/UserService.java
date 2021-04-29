package br.com.unip.jogodavelha.service;

import lombok.extern.slf4j.Slf4j;
import br.com.unip.jogodavelha.security.UserSecurity;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
public class UserService {

    public static UserSecurity authenticated() {
        try {
            log.info("Retornando usuario logado");
            return (UserSecurity) getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return null;
        }
    }

}
