import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IResource } from '../../course-home.component';
import { ResourceEnum } from '../../../../enums/resourceEnum';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-resource-item',
  imports: [CommonModule, ButtonModule],
  templateUrl: './resource-item.component.html',
  styleUrl: './resource-item.component.scss'
})
export class ResourceItemComponent {

  resource = input.required<IResource>();
  canEditResource = input(false);
  editResource = output<IResource>();
  ResourceEnum = ResourceEnum;

  openFile(): void {
    if (!this.resource().file) return;

    try {
      const base64Data = this.resource().file?.includes(',')
        ? this.resource().file?.split(',')[1]
        : this.resource().file;

      const byteCharacters = atob(base64Data ?? '');
      const byteNumbers = new Array(byteCharacters.length);

      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }

      const byteArray = new Uint8Array(byteNumbers);

      const blob = new Blob([byteArray], { type: 'application/pdf' });

      const url = window.URL.createObjectURL(blob);
      window.open(url, '_blank');

      setTimeout(() => window.URL.revokeObjectURL(url), 1000);

    } catch (error) {
      console.error('Erro ao abrir arquivo:', error);
      alert('Erro ao abrir o arquivo. Verifique se o formato está correto.');
    }
  }
}
