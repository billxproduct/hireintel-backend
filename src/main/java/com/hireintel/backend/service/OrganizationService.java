package com.hireintel.backend.service;

import com.hireintel.backend.models.User;
import com.hireintel.backend.repository.UserRepository;

import java.util.Optional;

public class OrganizationService {

    UserRepository userRepository;
    public Organization createOrganization(String name, int userId) {
        Optional<User> user = userRepository.findById(userId);
        return null;
    }

    public boolean addMember(Long organizationId, Long userId, UserRole userRole) {
        return false;
    }

    public List<InterviewRole> getInterviewRoles(Long organizationId) {
        return null;
    }

    public List<Interview> getInterviews(Long organizationId, Long roleId) {
        return null;
    }

    public InterviewResult getInterviewResult(Long organizationId, Long interviewId) {
        return null;
    }
}
