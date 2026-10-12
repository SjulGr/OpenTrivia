package com.opentrivia.backend.controller;

import com.opentrivia.backend.controller.dto.answer.AnswerRequest;
import com.opentrivia.backend.controller.dto.answer.AnswerResponse;
import com.opentrivia.backend.controller.dto.quiz.QuizResponse;
import com.opentrivia.backend.service.QuizService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class QuestionsController {

    private final QuizService quizService;

    public QuestionsController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/questions")
    public QuizResponse questions(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String difficulty,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "10") int amount
    ) {

        return quizService.createQuiz(amount, category, difficulty, type);
    }

    @PostMapping("/checkanswers")
    public AnswerResponse checkAnswers(
            @RequestBody AnswerRequest answerRequest
    ) {
        return quizService.checkAnswers(answerRequest);
    }

}
