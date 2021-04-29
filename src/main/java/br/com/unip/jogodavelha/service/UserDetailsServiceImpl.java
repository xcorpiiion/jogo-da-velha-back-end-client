package br.com.unip.jogodavelha.service;

import br.com.unip.jogodavelha.model.Cliente;
import br.com.unip.jogodavelha.repository.ClienteRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.unip.jogodavelha.security.UserSecurity;

@Slf4j
@Getter
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Buscando usuário pelo email...");
        final Cliente cliente = this.getClienteRepository().findByEmail(email);
        if (cliente == null) {
            log.info("Usuário não encontrado");
            throw new UsernameNotFoundException(email);
        }
        log.info("Cliente encontrado");
        return new UserSecurity(cliente.getId(), cliente.getEmail(), cliente.getSenha(), cliente.getEnumPerfilClientes());
    }
}
