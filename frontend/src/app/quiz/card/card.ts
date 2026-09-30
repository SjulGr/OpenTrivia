import {Component, input} from '@angular/core';

@Component({
  selector: 'card',
  templateUrl: './card.html'
})
export class Card {
  cardTitle = input.required<String>();
}
