import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PersistenceService {
  private baseUrl = environment.baseUrl;
  constructor(private http: HttpClient) { }

  getRequest(url: string, options?: any): Observable<Object> {
    return this.http.get(this.baseUrl + url,
      {
        ...options,
        withCredentials: true,
      }
    )
  }

  postRequest(url: string, body: any, options?: any): Observable<Object> {
    return this.http.post(
      this.baseUrl + url,
      body,
      {
        ...options,
        withCredentials: true
      }
    )
  }

  putRequest(url: string, body: any, options?: any): Observable<Object> {
    return this.http.put(
      this.baseUrl + url,
      body,
      {
        ...options,
        withCredentials: true
      }
    )
  }

  deleteRequest(url: string, options?: any): Observable<Object> {
    return this.http.delete(
      this.baseUrl + url,
      {
        ...options,
        withCredentials: true
      }
    )
  }
}
