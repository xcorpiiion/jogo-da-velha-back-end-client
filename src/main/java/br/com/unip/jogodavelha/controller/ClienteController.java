package br.com.unip.jogodavelha.controller;

import br.com.unip.jogodavelha.dto.ConnectionDTO;
import br.com.unip.jogodavelha.dto.GameDTO;
import br.com.unip.jogodavelha.model.Cliente;
import br.com.unip.jogodavelha.service.ClienteService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Getter
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id) {
        return this.getClienteService().findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        return this.getClienteService().findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/page")
    public ResponseEntity<Page<Cliente>> findAllPerPage(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "24") Integer linesPerPage,
                                                        @RequestParam(defaultValue = "nome") String orderBy,
                                                        @RequestParam(defaultValue = "ASC") String direction) {
        return this.getClienteService().findPerPage(page, linesPerPage, orderBy, direction);
    }

    @GetMapping("/email")
    public ResponseEntity<Cliente> findByEmail(@RequestParam(defaultValue = "value") String email) {
        return this.getClienteService().findByEmail(email);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody Cliente cliente) {
        return this.getClienteService().save(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Cliente cliente, @PathVariable long id) {
        return this.getClienteService().update(cliente, id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return this.getClienteService().delete(id);
    }

    @PostMapping("/testConnection")
    public ResponseEntity<String> testConnection(@RequestBody ConnectionDTO connectionDTO) {
        return this.getClienteService().testConnection(connectionDTO.getIp(), connectionDTO.getPort(), connectionDTO.getPlayerName());
    }

    @PostMapping("/play")
    public ResponseEntity<Void> play(@RequestBody ConnectionDTO connectionDTO) {
        return this.getClienteService().play(connectionDTO.getIp(), connectionDTO.getPort(), connectionDTO.getPlayerName(),
                connectionDTO.getEmail());
    }

    @PostMapping("/buttonNumber")
    public ResponseEntity<Void> buttonNumberClicked(@RequestBody String buttonNumber) {
        return this.getClienteService().buttonNumberClicked(buttonNumber);
    }

    @GetMapping("/blockSquare")
    public ResponseEntity<GameDTO> blockSquareGame() {
        return this.getClienteService().blockSquareGame();
    }

    @GetMapping("/logged")
    public ResponseEntity<Boolean> logged() {
        return this.clienteService.logged();
    }

    @GetMapping("/allowed")
    public ResponseEntity<Boolean> allowed() {
        return this.clienteService.allowed();
    }

    @GetMapping("/firstToPlay")
    public ResponseEntity<GameDTO> firstToPlay() {
        return this.clienteService.firstToPlay();
    }

}
