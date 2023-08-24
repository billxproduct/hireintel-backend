package com.hireintel.backend.beans;

import lombok.Builder;

@Builder
public class SimpleMailMessage {
    private String from;
    private String to;
    private String subject;
    private String text;
}
