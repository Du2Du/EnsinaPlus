import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ResourceEnum } from '../../../../enums/resourceEnum';
import { IResource } from '../../course-home.component';
import { PersistenceService } from './../../../../services/persistence.service';
import { FileUploadService } from '../../../../services/file-upload.service';
import { tap } from 'rxjs';

@Component({
  selector: 'app-resource-item',
  imports: [CommonModule, ButtonModule],
  templateUrl: './resource-item.component.html',
  styleUrl: './resource-item.component.scss'
})
export class ResourceItemComponent {

  constructor(
    private persistenceService: PersistenceService,
    private fileUploadService: FileUploadService
  ) { }

  resource = input.required<IResource>();
  canEditResource = input(false);
  editResource = output<IResource>();
  deleteResource = output<string>();
  reloadCourse = output();

  ResourceEnum = ResourceEnum;
  markAsView() {
    this.persistenceService.postRequest('/v1/resource/finalize/', { resourceUUID: this.resource().uuid })
      .pipe(tap((response: any) => {
        this.reloadCourse.emit();
      })).subscribe();
  }
  openFile(): void {
    if (!this.resource().file) return;
    
    const downloadUrl = this.fileUploadService.getFileDownloadUrl(this.resource().file ?? '');
    window.open(downloadUrl, '_blank');
  }

  getVideoUrl(): string {
    if (!this.resource().video) return '';
    return this.fileUploadService.getFileDownloadUrl(this.resource().video ?? '');
  }

}
