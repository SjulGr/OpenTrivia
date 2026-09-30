import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Card } from './quiz/card/card';
import { QuizCreate } from './quiz/quiz-create/quiz-create';
import { QuizQuestion } from './quiz/quiz-question/quiz-question';
import { QuizResult } from './quiz/quiz-result/quiz-result';
import { QuizService } from './services/quiz.service';

@Component({
  imports: [
    RouterOutlet,
    Card,
    QuizCreate,
    QuizQuestion,
    QuizResult
  ],
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private quizService = inject(QuizService);

  cardTitle = "";

  quiz = this.quizService.getQuiz();
  quizResult = this.quizService.getQuizResult();

  onTitleChanged(newTitle: string) {
    this.cardTitle = newTitle;
  }
}
