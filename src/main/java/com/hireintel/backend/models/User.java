package com.hireintel.backend.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private UserStatus status;
    public enum UserStatus {
        UNVERIFIED,
        PARTIALLY_VERIFIED,
        VERIFIED
    }
}