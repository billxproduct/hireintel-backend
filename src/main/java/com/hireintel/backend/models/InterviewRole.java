package com.hireintel.backend.models;

import java.util.List;

public class InterviewRole {

    private String name;
    private List<String> skills;
    private int minExperience;
    private int maxExperience;
    private String jobDescription;
    private List<String> questions;
    private InterviewRoleStatus status;

    private enum InterviewRoleStatus {

    }
}
