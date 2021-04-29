package br.com.unip.jogodavelha.service;

import br.com.unip.jogodavelha.dto.GameDTO;
import br.com.unip.jogodavelha.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteService {

    ResponseEntity<Void> save(Cliente cliente);

    ResponseEntity<Cliente> findById(Long id);

    ResponseEntity<Cliente> findByEmail(String email);

    ResponseEntity<List<Cliente>> findAll();

    ResponseEntity<Void> update(Cliente clienteDTO, long id);

    ResponseEntity<Void> delete(long id);

    ResponseEntity<Page<Cliente>> findPerPage(Integer page, Integer linesPerPage, String orderBy, String direction);

    ResponseEntity<String> testConnection(String ip, Integer port, String playerName);

    ResponseEntity<Void> play(String ip, Integer port, String playerName, String email);

    ResponseEntity<Boolean> logged();

    ResponseEntity<Boolean> allowed();

    ResponseEntity<Void> buttonNumberClicked(String buttonNumber);

    ResponseEntity<GameDTO> blockSquareGame();

    ResponseEntity<GameDTO> firstToPlay();

}
