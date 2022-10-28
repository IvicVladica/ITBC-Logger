package com.example.ITBCLogger.repository;

import com.example.ITBCLogger.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UUID token;

    public List<Client> getAllClients() {
        String query = "SELECT id, username, email, logCount FROM Clients";

        return jdbcTemplate.query(
                query,
                BeanPropertyRowMapper.newInstance(Client.class)
        );
    }

    public int isDuplicateName (String name)  {
        String query = "SELECT COUNT (*) FROM Clients WHERE username ='"+name+"'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int isDuplicateEmail (String email)  {
        String query = "SELECT COUNT (*) FROM Clients WHERE email ='"+email+"'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public boolean isEmailValid (String name) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return (Pattern.matches(regex, name));
    }

    public boolean isPasswordValid (String password) {
        return (password.matches(".*[a-zA-Z].*")
                & password.matches(".*[0-9].*")
                & password.length()>=8);
    }

    public UUID getId(String input) {
        String query = "SELECT id FROM Clients WHERE id LIKE '"+input+"'";
        try {
            return jdbcTemplate.queryForObject(query, UUID.class); }
        catch(EmptyResultDataAccessException e) {return null;}
    }

    public void insertClient (Client client) {
        client.setId(UUID.randomUUID());
        client.setLogCount(0);
        String action = "insert into Clients (id, username, password, email, logCount)  VALUES ('"
                + client.getId() + "','" +client.getUsername() +"', '"+client.getPassword()+"','"+client.getEmail() +"','"+client.getLogCount()+"')";
        jdbcTemplate.execute(action);
    }

    public UUID clientLogin (Client client) {
        String findUserQuery = "SELECT username FROM Clients WHERE username = '"+client.getUsername()+"'";
        String findUser = jdbcTemplate.queryForObject(findUserQuery, String.class);
        String findPasswordQuery = "SELECT password FROM Clients WHERE username = '"+client.getUsername()+"'";String findPassword = jdbcTemplate.queryForObject(findPasswordQuery, String.class);
        String findIdQuery = "SELECT id FROM Clients WHERE username = '"+client.getUsername()+"'";
        UUID findId = jdbcTemplate.queryForObject(findIdQuery, UUID.class);
        if (findUser==null) {return null;}
        if (!client.getPassword().equals(findPassword)) {return null;}
        token = findId;
        return token;
    }

    public void changePassword (UUID clientID, Client client) {
        String password = client.getPassword();
        String action = "UPDATE Clients SET [password]='"+password+"' WHERE id='"+clientID+"'";
        jdbcTemplate.execute(action);
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}


