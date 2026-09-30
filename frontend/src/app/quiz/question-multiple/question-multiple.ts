import { Component, input, output } from '@angular/core';
import { QuizAnswer, QuizQuestionData } from '../../models/quiz.model';

@Component({
  selector: 'question-multiple',
  templateUrl: './question-multiple.html'
})
export class QuestionMultiple {
  question = input.required<QuizQuestionData>();

  selectedAnswer = input<QuizAnswer | null>(null);

  answerSelected = output<QuizAnswer>();

  selectAnswer(answer: string) {
    this.answerSelected.emit({
      id: this.question().id,
      answer: answer
    });
  }
}
