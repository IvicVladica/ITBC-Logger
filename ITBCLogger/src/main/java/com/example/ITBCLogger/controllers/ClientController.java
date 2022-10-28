package com.example.ITBCLogger.controllers;

import com.example.ITBCLogger.model.Client;
import com.example.ITBCLogger.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLDataException;
import java.util.UUID;


@RestController
public class ClientController {
    private ClientRepository clientRepository;

    @Autowired
    public ClientController (ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/api/clients")                                                 //ispis svih klijenata
    public ResponseEntity<?> getAllClients (@RequestHeader String authorization){
        UUID logToken = clientRepository.getId(authorization);
        if (logToken==null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect token");
        }
        if (!authorization.equals("073efd22-9d06-4bd9-971f-fb155977d35b")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correct token, but not admin");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clientRepository.getAllClients());
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity<String> addClient (@RequestBody Client client) throws SQLDataException {
        if (!clientRepository.isEmailValid(client.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email must be valid");
        }
        if (client.getUsername().length()<3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username at least 3 characters");
        }
        if (!clientRepository.isPasswordValid(client.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password at least 8 characters and one letter and one number");
        }
        if (clientRepository.isDuplicateName(client.getUsername()) > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        if (clientRepository.isDuplicateEmail(client.getEmail()) > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        clientRepository.insertClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/api/clients/login")
    public ResponseEntity<?> loginClient (@RequestBody Client client) throws  SQLDataException {
        if (clientRepository.clientLogin(client)==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password incorrect");
        }
        clientRepository.clientLogin(client);
        return ResponseEntity.status(HttpStatus.OK).body(clientRepository.clientLogin(client));
    }

    @PatchMapping("/api/clients/{clientId}/reset-password")
    public ResponseEntity<String> changePassword(@RequestParam(value = "clientId") UUID clientId,
                                                 @RequestBody Client client,
                                                 @RequestHeader String authorization) {
        UUID logToken = clientRepository.getId(authorization);
        if (logToken==null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect token");
        }
        if (!authorization.equals("073efd22-9d06-4bd9-971f-fb155977d35b")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correct token, but not admin");
        }
        if (client.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No content");
        }
        clientRepository.changePassword(clientId, client);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    // adminID = 073efd22-9d06-4bd9-971f-fb155977d35b
}
