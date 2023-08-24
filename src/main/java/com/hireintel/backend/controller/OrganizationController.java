package com.hireintel.backend.controller;

import com.hireintel.backend.beans.AddMemberRequest;
import com.hireintel.backend.beans.CheckOrgHandleAvailabilityRequest;
import com.hireintel.backend.beans.CreateOrganizationRequest;
import com.hireintel.backend.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService; // You should define an OrganizationService class

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrganization(@RequestBody CreateOrganizationRequest request) {
        try {
            // Use OrganizationService to handle the logic for creating an organization
            Organization organization = organizationService.createOrganization(request.getName(), request.getUserId());

            return new ResponseEntity<>(new ApiResponse(true, "Organization successfully created", organization), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error occurred while creating organization"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-member")
    public ResponseEntity<Object> addMember(@RequestBody AddMemberRequest request) {
        try {
            Long organizationId = request.getOrganizationId();
            Long userId = request.getUserId();
            UserRole userRole = request.getUserRole();

            // Use OrganizationService to handle the logic for adding a member to the organization
            boolean result = organizationService.addMember(organizationId, userId, userRole);

            if (!result) {
                return new ResponseEntity<>(new ApiResponse(false, "Error while adding member"), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(new ApiResponse(true, "Email sent to user for joining the organization"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while adding member"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/check-org-handle-availability")
    public ResponseEntity<Object> checkOrgHandleAvailability(@RequestBody CheckOrgHandleAvailabilityRequest request) {
        // Implement logic to check organization handle availability
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{organizationId}/interview-roles")
    public ResponseEntity<Object> getInterviewRoles(@PathVariable Long organizationId) {
        try {
            // Use OrganizationService to handle the logic for getting interview roles
            List<InterviewRole> interviewRoles = organizationService.getInterviewRoles(organizationId);

            return new ResponseEntity<>(new ApiResponse(true, "Interview roles successfully fetched", interviewRoles), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while fetching interview roles"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{organizationId}/interviews/{roleId}")
    public ResponseEntity<Object> getInterviews(@PathVariable Long organizationId, @PathVariable Long roleId) {
        try {
            // Use OrganizationService to handle the logic for getting interviews
            List<Interview> interviews = organizationService.getInterviews(organizationId, roleId);

            return new ResponseEntity<>(new ApiResponse(true, "Interviews successfully fetched", interviews), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while fetching interviews"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{organizationId}/interviews/{roleId}/{interviewId}")
    public ResponseEntity<Object> getInterviewResult(@PathVariable Long organizationId, @PathVariable Long interviewId) {
        try {
            // Use OrganizationService to handle the logic for getting interview result
            InterviewResult interviewResult = organizationService.getInterviewResult(organizationId, interviewId);

            if (interviewResult == null) {
                return new ResponseEntity<>(new ApiResponse(false, "Invalid organization or forbidden access"), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(new ApiResponse(true, "Successfully fetched the interview results", interviewResult), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while fetching interview result"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}