package com.hireintel.backend.beans;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
