package com.hireintel.backend.controller;

import com.hireintel.backend.service.QuestionService;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService; // You should define a QuestionService class

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<Object> createQuestion(@RequestBody QuestionRequest request) {
        try {
            Question question = questionService.createQuestion(request);
            return new ResponseEntity<>(new ApiResponse(true, "Question created successfully", question), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error while creating question"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getQuestion(@PathVariable Long id) {
        try {
            TypePatternQuestions.Question question = questionService.getQuestionById(id);

            if (question != null) {
                return new ResponseEntity<>(new ApiResponse(true, "Question retrieved successfully", question), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Question not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error getting the question"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllQuestions() {
        try {
            List<Question> questions = questionService.getAllQuestions();
            return new ResponseEntity<>(new ApiResponse(true, "Questions retrieved successfully", questions), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error getting all questions"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateQuestion(@PathVariable Long id, @RequestBody QuestionRequest request) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, request);

            if (updatedQuestion != null) {
                return new ResponseEntity<>(new ApiResponse(true, "Question updated successfully", updatedQuestion), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Question not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error updating the question"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuestion(@PathVariable Long id) {
        try {
            Question deletedQuestion = questionService.deleteQuestion(id);

            if (deletedQuestion != null) {
                return new ResponseEntity<>(new ApiResponse(true, "Question deleted successfully", deletedQuestion), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Question not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "Error deleting the question"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

