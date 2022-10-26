package com.example.ITBCLogger.controllers;

import com.example.ITBCLogger.model.Client;
import com.example.ITBCLogger.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
public class ClientController {
    private ClientRepository clientRepository;

    @Autowired
    public ClientController (ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/api/clients")                                                 //ispis svih klijenata
    public List<Client> getAllClients(){
        return clientRepository.getAllClients();
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity<String> addClient (@RequestBody Client client) throws SQLDataException {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(regex, client.getEmail())) {                    //validacija email-a
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email must be valid");
        }
        if (client.getUsername().length()<3) {                               //provera da li je ime duze od 3 karaktera
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username at least 3 characters");
        }
        if (!(client.getPassword().matches(".*[a-zA-Z].*")            //provera da li je password duzi od 8 karaktera
                & client.getPassword().matches(".*[0-9].*")           //i da li ima 1 slovo i 1 broj
                & client.getPassword().length()>=8)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password at least 8 characters and one letter and one number");
        }
        if (clientRepository.isDuplicateName(client.getUsername()) > 0) {   //provera da li vec postoji username
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        if (clientRepository.isDuplicateEmail(client.getEmail()) > 0) { //provera da li vec postoji email
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        clientRepository.insertClient(client);                              //unos klijenta, ako je sve ok
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
                                                 @RequestBody Client client) {
        if (client.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No content");
        }
        clientRepository.changePassword(clientId, client);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
