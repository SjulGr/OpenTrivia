package com.opentrivia.backend.service;

import com.opentrivia.backend.client.OpenTdbClient;
import com.opentrivia.backend.controller.dto.answer.AnswerResult;
import com.opentrivia.backend.controller.dto.answer.SubmittedAnswer;
import com.opentrivia.backend.controller.dto.answer.AnswerRequest;
import com.opentrivia.backend.controller.dto.answer.AnswerResponse;
import com.opentrivia.backend.controller.dto.opentdb.OpenTdbQuestion;
import com.opentrivia.backend.controller.dto.opentdb.OpenTdbResponse;
import com.opentrivia.backend.controller.dto.quiz.QuestionResponse;
import com.opentrivia.backend.controller.dto.quiz.QuizResponse;
import com.opentrivia.backend.exception.QuizNotFoundException;
import com.opentrivia.backend.model.StoredQuestion;
import com.opentrivia.backend.model.StoredQuiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private static final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final OpenTdbClient openTdbClient;

    private final Map<UUID, StoredQuiz> quizzes = new ConcurrentHashMap<>();

    public QuizService(OpenTdbClient openTdbClient) {
        this.openTdbClient = openTdbClient;
    }

    public QuizResponse createQuiz(
            int amount,
            String category,
            String difficulty,
            String type
    ) {
        OpenTdbResponse openTdbResponse = openTdbClient.getQuestion(amount, category, difficulty, type);

        List<StoredQuestion> storedQuestions = openTdbResponse.results()
                .stream()
                .map(this::toStoredQuestion)
                .toList();

        StoredQuiz quiz = new StoredQuiz(UUID.randomUUID(), openTdbResponse.response_code(), storedQuestions);
        log.info(quiz.toString());
        quizzes.put(quiz.id(), quiz);

        List<QuestionResponse> questions = storedQuestions.stream()
                .map(this::toQuestionResponse)
                .toList();

        QuizResponse quizResponse = new QuizResponse(quiz.id(), quiz.response_code(), questions);
        log.info(quizResponse.toString());

        return quizResponse;
    }

    public AnswerResponse checkAnswers(AnswerRequest request) {
        StoredQuiz quiz = quizzes.get(request.id());

        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found");
        }

        Map<UUID, StoredQuestion> questions = quiz.questions()
                .stream()
                .collect(Collectors.toMap(
                        StoredQuestion::id,
                        Function.identity()
                ));

        List<AnswerResult> checkedAnswers = request.questions()
                .stream()
                .map(answer -> checkAnswer(answer, questions))
                .toList();

        AnswerResponse AnswerResponse = new AnswerResponse(request.id(), checkedAnswers);
        log.info(AnswerResponse.toString());

        return AnswerResponse;
    }

    private StoredQuestion toStoredQuestion(OpenTdbQuestion question) {
        return new StoredQuestion(
                UUID.randomUUID(),
                question.type(),
                question.difficulty(),
                question.category(),
                question.question(),
                question.correct_answer(),
                question.incorrect_answers()
        );
    }

    private QuestionResponse toQuestionResponse(StoredQuestion question) {
        List<String> answers = new ArrayList<>(question.incorrectAnswers());
        answers.add(question.correctAnswer());
        Collections.shuffle(answers);

        return new QuestionResponse(
                question.id(),
                question.type(),
                question.difficulty(),
                question.category(),
                question.question(),
                answers
        );
    }

    private AnswerResult checkAnswer(
            SubmittedAnswer submittedAnswer,
            Map<UUID, StoredQuestion> questions
    ) {
        StoredQuestion question = questions.get(submittedAnswer.id());

        boolean correct = question != null
                && question.correctAnswer().equals(submittedAnswer.selected_answer());

        return new AnswerResult(
                submittedAnswer.id(),
                submittedAnswer.selected_answer(),
                correct
        );
    }

}
