package com.gateway.controller;

import com.gateway.constants.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/secured")
public class SecuredController {

    @GetMapping("/date")
    public ResponseEntity<Object> getData(){
        return ResponseEntity.ok(LocalDate.now().format(DateTimeFormatter.ofPattern(AppConstants.RESPONSE_DATE))) ;
    }

    @GetMapping("/datetime")
    public ResponseEntity<Object> getDataTime(){
        return ResponseEntity.ok(LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstants.RESPONSE_DATE_TIME))) ;
    }
}
