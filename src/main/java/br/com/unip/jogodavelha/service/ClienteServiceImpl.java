package br.com.unip.jogodavelha.service;

import br.com.unip.jogodavelha.dto.GameDTO;
import br.com.unip.jogodavelha.game.cliente.ClienteJogo;
import br.com.unip.jogodavelha.game.cliente.ServidorJogo;
import br.com.unip.jogodavelha.model.Cliente;
import br.com.unip.jogodavelha.repository.ClienteRepository;
import br.com.unip.jogodavelha.security.UserSecurity;
import br.com.unip.jogodavelha.service.exception.AuthorizationException;
import br.com.unip.jogodavelha.service.exception.DataIntegrityException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.unip.jogodavelha.model.EnumPerfilCliente.ADMIN;
import static br.com.unip.jogodavelha.service.UserService.authenticated;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Getter
@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ServidorJogo servidorJogo;

    private ClienteJogo cliente;

    private static boolean isAllowed = false;

    private static boolean opponentMessage = false;

    private static String currentNicknameTurn;

    private static String myNickname;

    private static String opponentNickname;

    private static String buttonNumberToBlock;

    private static long myId;

    private static long opponentId;

    private static List<String> messages = new ArrayList<>();

    @Transactional
    @Override
    public ResponseEntity<Void> save(Cliente cliente) {
        log.info("Salvando cliente...");
        cliente.setSenha(this.getPasswordEncoder().encode(cliente.getSenha()));
        this.getRepository().save(cliente);
        URI uri = fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        log.info("Cliente salvo com sucesso {}", uri);
        return created(uri).build();
    }

    @Override
    public ResponseEntity<Cliente> findById(Long id) {
        UserSecurity userSecurity = authenticated();
        this.loggedOrAdmin(id, userSecurity);
        Optional<Cliente> clientes = this.getRepository().findById(id);
        clientes.ifPresent(value -> log.info("Retornando clientes: {}", value));
        return clientes.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @Override
    public ResponseEntity<Cliente> findByEmail(String email) {
        UserSecurity userSecurity = authenticated();
        if (userSecurity == null || !userSecurity.hasRole(ADMIN) && !email.equals(userSecurity.getUsername())) {
            log.info("Acesso negado");
            throw new AuthorizationException("Acesso negado");
        }
        Cliente cliente = this.getRepository().findByEmail(userSecurity.getUsername());
        if (this.isPresent(cliente == null, "Cliente não encontrado...")) {
            return notFound().build();
        }
        log.info("Cliente encontrado");
        return ok(cliente);
    }

    private void loggedOrAdmin(Long id, UserSecurity userSecurity) {
        log.info("Verificando se usuário está autorizado...");
        if (userSecurity == null || !userSecurity.hasRole(ADMIN) && !id.equals(userSecurity.getId())) {
            log.info("Acesso negado");
            throw new AuthorizationException("Acesso negado");
        }
        log.info("Usuário autorizado...");
    }

    @Override
    public ResponseEntity<List<Cliente>> findAll() {
        log.info("Retornando clientes...");
        List<Cliente> clientes = this.getRepository().findAll();
        if (isPresent(isEmpty(clientes), "Clientes não encontrados...")) return notFound().build();
        log.info("Clientes encontrados");
        return ok(clientes);
    }

    @Override
    public ResponseEntity<Void> update(Cliente clienteDTO, long id) {
        log.info("Atualizando cliente...");
        Optional<Cliente> cliente = this.getRepository().findById(id);
        if (!this.isPresent(cliente.isPresent(), "Cliente não encontrado...")) {
            return notFound().build();
        }
        cliente.get().setId(id);
        cliente.get().setNome(clienteDTO.getNome());
        cliente.get().setEmail(clienteDTO.getEmail());
        this.getRepository().save(cliente.get());
        log.info("Cliente atualizado...");
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(long id) {
        log.info("Deletando cliente...");
        try {
            this.getRepository().deleteById(id);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new DataIntegrityException("Não é possivel excluir um cliente que possui produtos");
        }
        log.info("Cliente deletado...");
        return noContent().build();
    }

    @Override
    public ResponseEntity<Page<Cliente>> findPerPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        log.info("Buscando cleintes paginados...");
        Page<Cliente> clientes = this.getRepository().findAll(pageRequest);
        log.info("Clientes paginados encontrado...");
        return ok(clientes);
    }

    @Override
    public ResponseEntity<String> testConnection(String ip, Integer port, String playerName) {
        try {
            ClienteJogo cliente = new ClienteJogo(ip, port, playerName);
            cliente.executa();
            cliente.sendToGameServer("logarPassivo;" + playerName);
            return ok().body("Connectado");
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new RuntimeException("Erro de conexão");
        }
    }

    @Override
    public ResponseEntity<Void> play(String ip, Integer port, String playerName, String email) {
        try {
            ResponseEntity<Cliente> clienteReturned = this.findByEmail(email);
            log.info("Iniciando a conexão do jogador");
            setMessages(new ArrayList<>());
            cliente = new ClienteJogo(ip, port, playerName);
            cliente.executa();
            cliente.sendToGameServer("logar;" + playerName);
            log.info("Inicialização concluida com sucesso!");
            this.getServidorJogo().conectaComServidorChat(ip, playerName);
            if (clienteReturned.getBody() != null) {
                log.info("Salvando nickname...");
                clienteReturned.getBody().setNickname(playerName);
                this.getRepository().save(clienteReturned.getBody());
                log.info("Nickname salvo!");
            }
            return noContent().build();
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new RuntimeException("Erro de conexão");
        }
    }

    @Override
    public ResponseEntity<Boolean> logged() {
        UserSecurity userSecurity = authenticated();
        log.info("Verificando se usuário está autorizado...");
        if (userSecurity == null) {
            log.info("Acesso negado");
            throw new AuthorizationException("Acesso negado");
        }
        log.info("Usuário autorizado...");
        return ok(true);
    }

    @Override
    public ResponseEntity<Boolean> allowed() {
        if (isIsAllowed()) {
            setIsAllowed(false);
            return ok(true);
        }
        return ok(false);
    }

    @Override
    public ResponseEntity<Void> buttonNumberClicked(String buttonNumber) {
        this.getCliente().getRecebedor().getVelha().sendNumberButtonToServer(parseInt(buttonNumber));
        return noContent().build();
    }

    @Override
    public ResponseEntity<GameDTO> blockSquareGame() {
        GameDTO gameDTO = this.createNewGameDTO();
        if (isNumeric(getButtonNumberToBlock())) {
            gameDTO.setCurrentNickname(getCurrentNicknameTurn());
            gameDTO.setButtonNumber(parseInt(getButtonNumberToBlock()));
        }
        setButtonNumberToBlock(EMPTY);
        setCurrentNicknameTurn(EMPTY);
        return ok(gameDTO);
    }

    @Override
    public ResponseEntity<GameDTO> firstToPlay() {
        return ok(this.createNewGameDTO());
    }

    @Override
    public ResponseEntity<List<String>> getMessage() {
        if (isOpponentMessage()) {
            return ok(getMessages());
        }
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> messegeReceived() {
        setOpponentMessage(false);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> sendMessage(String message) {
        getMessages().add(message);
        this.getServidorJogo().getClienteChat().sendMessage(message);
        return noContent().build();
    }

    private GameDTO createNewGameDTO() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setMyNickname(getMyNickname());
        gameDTO.setMyId(getMyId());
        gameDTO.setOpponentId(getOpponentId());
        gameDTO.setOpponentNickname(getOpponentNickname());
        return gameDTO;
    }

    private boolean isPresent(boolean clienteNull, String logMessage) {
        if (clienteNull) {
            log.info(logMessage);
            return true;
        }
        return false;
    }

    // Get and set //

    public static boolean isIsAllowed() {
        return isAllowed;
    }

    public static void setIsAllowed(boolean isAllowed) {
        ClienteServiceImpl.isAllowed = isAllowed;
    }

    public static String getCurrentNicknameTurn() {
        return currentNicknameTurn;
    }

    public static void setCurrentNicknameTurn(String currentNicknameTurn) {
        ClienteServiceImpl.currentNicknameTurn = currentNicknameTurn;
    }

    public static String getButtonNumberToBlock() {
        return buttonNumberToBlock;
    }

    public static void setButtonNumberToBlock(String buttonNumberToBlock) {
        ClienteServiceImpl.buttonNumberToBlock = buttonNumberToBlock;
    }

    public static long getMyId() {
        return myId;
    }

    public static void setMyId(long myId) {
        ClienteServiceImpl.myId = myId;
    }

    public static long getOpponentId() {
        return opponentId;
    }

    public static void setOpponentId(long opponentId) {
        ClienteServiceImpl.opponentId = opponentId;
    }

    public static String getMyNickname() {
        return myNickname;
    }

    public static void setMyNickname(String myNickname) {
        ClienteServiceImpl.myNickname = myNickname;
    }

    public static String getOpponentNickname() {
        return opponentNickname;
    }

    public static void setOpponentNickname(String opponentNickname) {
        ClienteServiceImpl.opponentNickname = opponentNickname;
    }

    public static List<String> getMessages() {
        return messages;
    }

    public static void setMessages(List<String> messages) {
        ClienteServiceImpl.messages = messages;
    }

    public static boolean isOpponentMessage() {
        return opponentMessage;
    }

    public static void setOpponentMessage(boolean opponentMessage) {
        ClienteServiceImpl.opponentMessage = opponentMessage;
    }
}
