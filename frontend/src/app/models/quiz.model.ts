export interface QuizQuestionData {
  id: string;
  type: string;
  difficulty: string;
  category: string;
  question: string;
  answers: string[];
}

export interface QuizData {
  id: string;
  response_code: number;
  questions: QuizQuestionData[];
}

export interface QuizAnswer {
  id: string;
  answer: string;
}

export interface AnswerResult {
  id: string;
  selected_answer: string;
  is_correct: boolean;
}

export interface QuizResultData {
  id: string;
  results: AnswerResult[];
}
