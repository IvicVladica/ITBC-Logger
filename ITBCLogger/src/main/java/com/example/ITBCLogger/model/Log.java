package com.example.ITBCLogger.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Logs")
public class Log {
    @Id
    private UUID id;
    String message;
    int logType;
    String logTypeName;
    LocalDate dateOfLog;

    UUID token;


    public Log(){};
    public Log(String message, int logType) {
        this.message = message;
        this.logType = logType;
    }
    public Log(UUID id, String message, int logType, String logTypeName, LocalDate dateOfLog) {
        this.id = id;
        this.message = message;
        this.logType = logType;
        this.logTypeName = logTypeName;
        this.dateOfLog = dateOfLog;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public String getLogTypeName() {
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName) {
        this.logTypeName = logTypeName;
    }

    public LocalDate getDateOfLog() {
        return dateOfLog;
    }

    public void setDateOfLog(LocalDate dateOfLog) {
        this.dateOfLog = dateOfLog;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", logType=" + logType +
                ", logTypeName='" + logTypeName + '\'' +
                ", date=" + dateOfLog +
                '}';
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
