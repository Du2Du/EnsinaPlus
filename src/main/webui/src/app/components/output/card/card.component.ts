import { AfterViewInit, Component, ElementRef, input, OnInit } from '@angular/core';

@Component({
  selector: 'app-card',
  imports: [],
  host: {
    class: 'block p-6 pt-8 pb-8 rounded-lg'
  },
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent implements OnInit {
  constructor(public el: ElementRef<HTMLDivElement>) { }

  backgroundColor = input.required<string>();
  color = input.required<string>();

  ngOnInit(): void {
    this.el.nativeElement.style.setProperty('background-color', 'var(--e-' + this.backgroundColor() + ')')
    this.el.nativeElement.style.setProperty('color', 'var(--e-' + this.color() + ')')
  }
}
