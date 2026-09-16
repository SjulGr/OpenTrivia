package com.opentrivia.backend.service;

import com.opentrivia.backend.client.OpenTdbClient;
import com.opentrivia.backend.controller.dto.answer.AnswerRequest;
import com.opentrivia.backend.controller.dto.answer.AnswerResponse;
import com.opentrivia.backend.controller.dto.answer.SubmittedAnswer;
import com.opentrivia.backend.controller.dto.opentdb.OpenTdbQuestion;
import com.opentrivia.backend.controller.dto.opentdb.OpenTdbResponse;
import com.opentrivia.backend.controller.dto.quiz.QuestionResponse;
import com.opentrivia.backend.controller.dto.quiz.QuizResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private OpenTdbClient openTdbClient;

    @InjectMocks
    private QuizService quizService;

    private OpenTdbQuestion createTestQuestion() {
        return new OpenTdbQuestion(
                "Multiple",
                "easy",
                "Cooking",
                "What is made using flower, milk and eggs",
                "Pancakes",
                List.of("Pie", "Bread", "Pizza")
        );
    }

    private OpenTdbResponse createTestResponse() {
        return new OpenTdbResponse(
                0,
                List.of(createTestQuestion())
        );
    }

    private QuizResponse createTestQuiz() {
        when(openTdbClient.getQuestion(1, "", "", "")).thenReturn(createTestResponse());

        return quizService.createQuiz(1, "", "", "");
    }

    @Test
    void createQuiz_returnsCorrectNumberOfQuestions() {
        QuizResponse response = createTestQuiz();

        assertEquals(1, response.questions().size());
    }

    @Test
    void createQuiz_containsAllAnswers() {
        QuizResponse response = createTestQuiz();

        QuestionResponse result = response.questions().getFirst();

        assertTrue(result.answers().contains("Pancakes"));
        assertEquals(4, result.answers().size());
    }

    @Test
    void checkAnswer_returnsTrueWhenAnswerIsCorrect() {
        QuizResponse quiz = createTestQuiz();

        UUID quizId = quiz.id();
        UUID questionId = quiz.questions().getFirst().id();

        AnswerRequest request = new AnswerRequest(quizId, List.of(new SubmittedAnswer(questionId, "Pancakes")));
        AnswerResponse response = quizService.checkAnswers(request);

        assertTrue(response.results().getFirst().is_correct());
    }

    @Test
    void checkAnswer_returnsFalseWhenAnswerIsIncorrect() {
        QuizResponse quiz = createTestQuiz();

        UUID quizId = quiz.id();
        UUID questionId = quiz.questions().getFirst().id();

        AnswerRequest request = new AnswerRequest(quizId, List.of(new SubmittedAnswer(questionId, "Pie")));
        AnswerResponse response = quizService.checkAnswers(request);

        assertFalse(response.results().getFirst().is_correct());
    }
}
