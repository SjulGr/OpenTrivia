import { Component, effect, inject, output, signal } from '@angular/core';
import { QuestionBoolean } from '../question-boolean/question-boolean';
import { QuestionMultiple } from '../question-multiple/question-multiple';
import { QuizQuestionData, QuizAnswer } from '../../models/quiz.model';
import { QuizService } from '../../services/quiz.service';

@Component({
  selector: 'quiz-question',
  imports: [QuestionBoolean, QuestionMultiple],
  templateUrl: './quiz-question.html'
})
export class QuizQuestion {
  private quizService = inject(QuizService);

  quiz = this.quizService.getQuiz();

  title = output<string>();

  currentQuestionIndex = signal(0);

  constructor() {
    effect(() => {
      const question = this.currentQuestion();

      this.title.emit(question.question);
    });
  }

  currentQuestion(): QuizQuestionData {
    return this.quiz()!.questions[this.currentQuestionIndex()];
  }

  currentAnswer(): QuizAnswer | undefined {
    return this.quizService.getAnswer(
      this.currentQuestion().id
    );
  }

  selectAnswer(answer: QuizAnswer) {
    this.quizService.selectAnswer(answer);
  }

  next() {
    if (!this.isLastQuestion()) {
      this.currentQuestionIndex.update(index => index + 1);
    }
  }

  previous() {
    if (!this.isFirstQuestion()) {
      this.currentQuestionIndex.update(index => index - 1);
    }
  }

  isFirstQuestion() {
    return this.currentQuestionIndex() === 0;
  }

  isLastQuestion() {
    return this.currentQuestionIndex() ===
      this.quiz()!.questions.length - 1;
  }

  answeredAllQuestions() {
    return this.quizService.answeredAllQuestions();
  }

  async finish() {
    if (!this.quizService.answeredAllQuestions()) return;

    try {
      await this.quizService.submitQuiz();
    } catch (error) {
      console.error("Failed to submit quiz:", error);
    }
  }
}
