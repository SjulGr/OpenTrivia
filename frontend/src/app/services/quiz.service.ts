import { Injectable, signal, inject } from '@angular/core';
import { QuizData, QuizResultData, QuizAnswer } from '../models/quiz.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class QuizService {
  private quiz = signal<QuizData | null>(null);
  private answers = signal<QuizAnswer[]>([]);
  private quizResult = signal<QuizResultData | null>(null);

  private http = inject(HttpClient);
  private baseURL = "http://localhost:8080"

  getQuiz() {
    return this.quiz.asReadonly();
  }

  getQuizResult() {
    return this.quizResult.asReadonly();
  }

  async createQuiz(amount: number): Promise<void> {
    this.http.get<QuizData>(`${this.baseURL}/questions`, {
      params: {amount: amount}
    }).subscribe((quizData) => {
        this.quiz.set(quizData);
        this.answers.set([]);
        this.quizResult.set(null);
      });
  }

  selectAnswer(answer: QuizAnswer) {
    this.answers.update(answers => {
      const existingIndex = answers.findIndex(
        a => a.id === answer.id
      );

      if (existingIndex !== -1) {
        const updated = [...answers];
        updated[existingIndex] = answer;
        return updated;
      }

      return [...answers, answer];
    });
  }

  answeredAllQuestions(): boolean {
    if (!this.quiz()) return false;

    return this.quiz()!.questions.every(question =>
      this.answers().some(answer => answer.id === question.id)
    );
  }

  getAnswer(questionId: string): QuizAnswer | undefined {
    return this.answers().find(
      answer => answer.id === questionId
    );
  }

  async submitQuiz(): Promise<void> {
    const quiz = this.quiz();

    if (!quiz) {
      throw new Error("No quiz is currently loaded");
    }

    const payload = {
      id: quiz.id,
      questions: this.answers().map(answer => ({
        id: answer.id,
        selected_answer: answer.answer
      }))
    };

    this.http.post<QuizResultData>(`${this.baseURL}/checkanswers`, payload)
      .subscribe((quizResultData) => {
        this.quizResult.set(quizResultData);
      });
  }

  newQuiz() {
    this.quiz.set(null);
    this.answers.set([]);
    this.quizResult.set(null);
  }
}
