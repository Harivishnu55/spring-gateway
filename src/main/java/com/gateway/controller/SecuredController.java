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

    /**
     * Returns the current date
     *
     * @return {@link ResponseEntity} containing the formatted current date as a String.
     *
     * <p><b>URL:</b> GET /secured/date</p>
     * <p><b>Returns:</b> 200 OK with current date string</p>
     */
    @GetMapping("/date")
    public ResponseEntity<Object> getData(){
        return ResponseEntity.ok(LocalDate.now().format(DateTimeFormatter.ofPattern(AppConstants.RESPONSE_DATE))) ;
    }

    /**
     * Returns the current date and time.
     *
     * @return {@link ResponseEntity} containing the formatted current date and time as a String.
     *
     * <p><b>URL:</b> GET /secured/datetime</p>
     * <p><b>Returns:</b> 200 OK with current date-time string</p>
     */
    @GetMapping("/datetime")
    public ResponseEntity<Object> getDataTime(){
        return ResponseEntity.ok(LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstants.RESPONSE_DATE_TIME))) ;
    }
}
