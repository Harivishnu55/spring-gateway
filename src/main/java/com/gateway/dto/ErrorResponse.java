package com.gateway.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private boolean success;
    private String timeStamp;
    private int statusCode;
    private String message;
}
