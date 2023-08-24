package com.hireintel.backend.beans;

import lombok.Data;

@Data
public class AddMemberRequest {
    private Long organizationId;
    private Long userId;
    private UserRole userRole;
}
