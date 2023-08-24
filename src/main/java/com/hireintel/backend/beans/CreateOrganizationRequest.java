package com.hireintel.backend.beans;

import lombok.Data;

@Data
public class CreateOrganizationRequest {
   private String name;
    private int userId;
}
