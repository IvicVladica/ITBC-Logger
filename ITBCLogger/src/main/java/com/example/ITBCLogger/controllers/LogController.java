package com.example.ITBCLogger.controllers;

import com.example.ITBCLogger.model.Client;
import com.example.ITBCLogger.model.Log;
import com.example.ITBCLogger.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class LogController {

    private LogRepository logRepository;

    @Autowired
    public LogController (LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("/api/logs/search")
    public ResponseEntity<?> getLogs(@RequestParam(value="message",required = false) String message,
                             @RequestParam(value = "logType", required = false) Integer logType,
                             @RequestParam(value = "firstDate", required = false) String firstDate,
                             @RequestParam(value = "secondDate", required = false) String secondDate
    ) throws ParseException {
        if (firstDate!=null && secondDate!=null &&  logRepository.checkDate(firstDate, secondDate))
        { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid dates");  }
        if (logType!=null && logType!=1 && logType!=2 && logType!=3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid logType");
        }
        return ResponseEntity.status(HttpStatus.OK).body(logRepository.getLogs(message, logType, firstDate, secondDate));}

    @PostMapping("/api/logs/create")
    public ResponseEntity<String> addLog (@RequestBody Log log) throws SQLDataException {
        if (log.getLogType()!=1 && log.getLogType()!=2 && log.getLogType()!=3) {   //provera da li je logType 1,2,3
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect log type");
        }
        if (log.getMessage().length() > 1024) {                             //provera da li je poruka kraca od 1024
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Message should be less than 1024");
        }
        logRepository.insertLog(log);                                      //unos log-a, ako je sve ok
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}

//    @RequestMapping(value="submitForm.html", method=RequestMethod.POST)
//    public ModelAndView submitForm(@RequestParam Map<String, String> reqParam) {
//        String name  = reqParam.get("studentName");
//        String email = reqParam.get("studentEmail");
//        ModelAndView model = new ModelAndView("AdmissionSuccess");
//        model.addObject("msg", "Details submitted by you::Name: " + name
//                + ", Email: " + email );
//    }