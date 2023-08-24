package com.hireintel.backend.controller;

import com.hireintel.backend.service.PacketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/packets")
public class PacketController {

    private final PacketService packetService;

    public PacketController(PacketService packetService) {
        this.packetService = packetService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPacket(@RequestParam("name") String name,
                                               @RequestParam("role") String role,
                                               @RequestParam("interviewType") String interviewType,
                                               @RequestParam("validTill") Date validTill,
                                               @RequestParam("organizationId") Long organizationId,
                                               @RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            byte[] buffer = file.getBytes();

            UUID uniqueId = UUID.randomUUID();
            String filePath = "resumes/" + uniqueId + originalFilename;

            // Use PacketService to handle the logic for creating a packet
            Packet packet = packetService.createPacket(name, role, interviewType, validTill, organizationId, filePath, buffer);

            return new ResponseEntity<>(new ApiResponse(true, "Packet created successfully", packet), HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while creating packet"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/start-interview")
    public ResponseEntity<Object> startInterview(@RequestBody StartInterviewRequest request) {
        try {
            Long packetId = request.getPacketId();

            // Use PacketService to handle the logic for starting an interview
            Packet packet = packetService.startInterview(packetId);

            if (packet == null) {
                return new ResponseEntity<>(new ApiResponse(false, "Interview not found"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse(true, "Interview started successfully", packet), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/evaluate-answer")
    public ResponseEntity<Object> evaluateAnswer(@RequestParam("question") String question,
                                                 @RequestParam("packetId") Long packetId,
                                                 @RequestParam("file") MultipartFile file) {
        try {
            byte[] buffer = file.getBytes();

            UUID uniqueId = UUID.randomUUID();
            String filePath = "interviewaudio/" + uniqueId + file.getOriginalFilename();

            // Use PacketService to handle the logic for evaluating an answer
            boolean result = packetService.evaluateAnswer(packetId, question, filePath, buffer);

            if (result) {
                return new ResponseEntity<>(new ApiResponse(true, "Solution successfully submitted"), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Error while evaluating answer"), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while evaluating answer"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/end-interview")
    public ResponseEntity<Object> endInterview(@RequestBody EndInterviewRequest request) {
        try {
            Long packetId = request.getPacketId();

            // Use PacketService to handle the logic for ending an interview
            Packet packet = packetService.endInterview(packetId);

            if (packet == null) {
                return new ResponseEntity<>(new ApiResponse(false, "Interview not found"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse(true, "Interview ended successfully"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit-questions")
    public ResponseEntity<Object> editQuestions(@RequestBody EditQuestionsRequest request) {
        try {
            Long packetId = request.getPacketId();
            List<String> questions = request.getQuestions();

            // Use PacketService to handle the logic for editing questions
            Packet packet = packetService.editQuestions(packetId, questions);

            if (packet == null) {
                return new ResponseEntity<>(new ApiResponse(false, "Interview not found"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse(true, "Questions successfully edited", packet), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{packetId}")
    public ResponseEntity<Object> getInterview(@PathVariable Long packetId) {
        try {
            // Use PacketService to handle the logic for getting an interview
            Packet packet = packetService.getInterview(packetId);

            if (packet == null) {
                return new ResponseEntity<>(new ApiResponse(false, "Interview not found"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse(true, "Interview successfully fetched", packet), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
