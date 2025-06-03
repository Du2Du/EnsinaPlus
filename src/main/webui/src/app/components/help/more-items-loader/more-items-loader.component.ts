import { Component, OnInit, output } from '@angular/core';

@Component({
  selector: 'app-more-items-loader',
  imports: [],
  templateUrl: './more-items-loader.component.html',
  styleUrl: './more-items-loader.component.scss'
})
export class MoreItemsLoaderComponent implements OnInit{
  load = output<void>();

  ngOnInit(): void {
    this.load.emit();
  }
}
