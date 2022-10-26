package com.example.ITBCLogger.repository;

import com.example.ITBCLogger.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class LogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertLog (Log log) {
        log.setId(UUID.randomUUID());
        if (log.getLogType()==1) {log.setLogTypeName("ERROR");};
        if (log.getLogType()==2) {log.setLogTypeName("WARNING");};
        if (log.getLogType()==3) {log.setLogTypeName("INFO");};
        log.setDateOfLog(LocalDate.now());
        String action = "insert into Logs (id, message, logType, logTypeName, dateOfLog)  VALUES ('"
                +log.getId()+ "','" +log.getMessage()+"', '"+log.getLogType()+"'," +
                "'"+log.getLogTypeName() +"','"+log.getDateOfLog()+"')";
        jdbcTemplate.execute(action);
    }
    public List<Log> getLogs(String message, Integer logType, String firstDate, String secondDate) {
        String query = "";
        if (message==null) {query = "SELECT * FROM LOGS WHERE logType='"+logType+"' AND dateOfLog BETWEEN '"+firstDate+"' AND '"+secondDate+"'";}
        if (logType==null) {query = "SELECT * FROM LOGS WHERE message LIKE '%"+message+"%' AND dateOfLog BETWEEN '"+firstDate+"' AND '"+secondDate+"'";}
        if (firstDate==null && secondDate==null) {query = "SELECT * FROM LOGS WHERE message LIKE'%"+message+"%' AND logType='"+logType+"'";}
        if (message==null && logType==null) {query = "SELECT * FROM LOGS WHERE dateOfLog BETWEEN '"+firstDate+"' AND '"+secondDate+"'";}
        if (message==null && firstDate==null && secondDate==null) {query = "SELECT * FROM LOGS WHERE logType='"+logType+"'";}
        if (logType==null && firstDate==null && secondDate==null) {query = "SELECT * FROM LOGS WHERE message LIKE '%"+message+"%'";}
        if (message!=null && logType!=null && firstDate!=null && secondDate!=null) {query = "SELECT * FROM LOGS WHERE [message] like '%"+message+"%' AND logType='"+logType+"' AND dateOfLog BETWEEN '"+firstDate+"' AND '"+secondDate+"'";}
        if (message==null && logType==null && firstDate==null && secondDate==null) {query = "SELECT * FROM LOGS";}

        return jdbcTemplate.query(
                query,
                BeanPropertyRowMapper.newInstance(Log.class)
        );
    }
    public boolean checkDate(String d1, String d2) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date a = formatter.parse(d1);
        Date b = formatter.parse(d2);
        return (a.after(b));
    }
}

