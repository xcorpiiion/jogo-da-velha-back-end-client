package br.com.unip.jogodavelha.service;

import br.com.unip.jogodavelha.model.Cliente;
import br.com.unip.jogodavelha.repository.ClienteRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class DatabaseInitializationService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void inicialization() {
        Cliente cliente = new Cliente();
        cliente.setNickname("teste");
        cliente.setNome("teste");
        cliente.setEmail("teste@gmail.com");
        cliente.setSenha("12345678");
        this.getClienteRepository().save(cliente);
    }

}
