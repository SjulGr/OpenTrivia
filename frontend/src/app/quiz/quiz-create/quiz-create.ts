import {Component, effect, inject, output, signal} from '@angular/core';
import {FormField, FormRoot, form, required, min, max} from '@angular/forms/signals';
import { QuizService } from '../../services/quiz.service';

@Component({
  imports: [FormField, FormRoot],
  selector: 'quiz-create',
  templateUrl: './quiz-create.html'
})
export class QuizCreate {
  private quizService = inject(QuizService);

  quizModel = signal({
    amount: 10
  });

  title = output<string>();

  constructor() {
    effect(() => {
      this.title.emit("Create Quiz");
    })
  }

  quizForm = form(this.quizModel, (schemaPath) => {
      min(schemaPath.amount, () => 1, {message: 'Amount should be at least 1'});
      max(schemaPath.amount, () => 50, {message: 'Amount should be at most 50'});
      required(schemaPath.amount, {message: 'An amount between 1 and 50 is required'});
    }, {
      submission: {
        action: async (field) => {
          try {
            await this.quizService.createQuiz(
              field().value().amount
            );
            return;
          } catch (error) {
            return {
              kind: 'serverError',
              message: 'Failed to load quiz'
            };
          }
        }
      }
    }
  );
}
