import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ResourceEnum } from '../../../../enums/resourceEnum';
import { IResource } from '../../course-home.component';
import { PersistenceService } from './../../../../services/persistence.service';

@Component({
  selector: 'app-resource-item',
  imports: [CommonModule, ButtonModule],
  templateUrl: './resource-item.component.html',
  styleUrl: './resource-item.component.scss'
})
export class ResourceItemComponent {

  constructor(private persistenceService: PersistenceService) { }

  resource = input.required<IResource>();
  canEditResource = input(false);
  editResource = output<IResource>();
  deleteResource = output<string>();
  ResourceEnum = ResourceEnum;
  markAsView() {
    this.persistenceService.postRequest('/v1/resource/finalize/', { resourceUUID: this.resource().uuid }).subscribe();
  }
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
      this.markAsView();
    } catch (error) {
      console.error('Erro ao abrir arquivo:', error);
      alert('Erro ao abrir o arquivo. Verifique se o formato está correto.');
    }
  }

}
