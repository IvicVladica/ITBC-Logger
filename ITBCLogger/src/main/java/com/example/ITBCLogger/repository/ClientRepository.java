package com.example.ITBCLogger.repository;

import com.example.ITBCLogger.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.beans.BeanProperty;
import java.sql.*;
import java.util.List;
import java.util.UUID;

@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Client> getAllClients() {
        String query = "SELECT id, username, password, email, logCount FROM Clients";

        return jdbcTemplate.query(
                query,
                BeanPropertyRowMapper.newInstance(Client.class)
        );
    }

    public int isDuplicateName (String name)  {
        String query = "SELECT COUNT (*) FROM Clients WHERE username ='"+name+"'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int isDuplicatePassword (String email)  {
        String query = "SELECT COUNT (*) FROM Clients WHERE email ='"+email+"'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public void insertClient (Client client) {
        client.setId(UUID.randomUUID());
        client.setLogCount(0);
        String action = "insert into Clients (id, username, password, email, logCount)  VALUES ('"
                + client.getId() + "','" +client.getUsername() +"', '"+client.getPassword()+"','"+client.getEmail() +"','"+client.getLogCount()+"')";
        jdbcTemplate.execute(action);
    }

    public void changePassword (UUID clientID, Client client) {
        String password = client.getPassword();
        String action = "UPDATE Clients SET [password]='"+password+"' WHERE id='"+clientID+"'";
        jdbcTemplate.execute(action);
    }
}

