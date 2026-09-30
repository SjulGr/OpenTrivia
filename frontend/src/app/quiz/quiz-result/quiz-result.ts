import {Component, effect, inject, output} from '@angular/core';
import {QuizQuestionData} from '../../models/quiz.model';
import {QuizService} from '../../services/quiz.service';

@Component({
  selector: 'quiz-result',
  templateUrl: './quiz-result.html'
})
export class QuizResult {
  quizService = inject(QuizService);

  quiz = this.quizService.getQuiz();
  result = this.quizService.getQuizResult();

  title = output<string>();

  constructor() {
    effect(() => {
      this.title.emit("Quiz Results");
    })
  }

  correctAnswers(): number {
    let correctAmount = 0;

    this.result()!.results.forEach(question => {
      if (question.is_correct) correctAmount++;
    })

    return correctAmount;
  }

  getQuestionById(id: string): QuizQuestionData | null {
    let res: QuizQuestionData | null = null;

    this.quiz()!.questions.forEach(question => {
      if (question.id === id) res = question;
    })

    return res;
  }
}
