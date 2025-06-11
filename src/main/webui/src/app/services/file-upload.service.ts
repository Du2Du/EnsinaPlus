import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PersistenceService } from './persistence.service';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {


  constructor(private persistenceService: PersistenceService) { }

  uploadFile(file: File, type: 'file' | 'video'): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    return this.persistenceService.postRequest('/v1/file/upload', formData);
  }

  getFileDownloadUrl(filePath: string): string {
    const pathParts = filePath.split('/');
    const type = pathParts[0];
    const fileName = pathParts[1];
    return `/v1/file/download/${type}/${fileName}`;
  }
}