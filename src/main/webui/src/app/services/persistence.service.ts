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

  getRequest(url: string): Observable<Object> {
    return this.http.get(this.baseUrl + url,
      {
        withCredentials: true
      }
    )
  }

  postRequest(url: string, body: any): Observable<Object> {
    return this.http.post(
      this.baseUrl + url,
      body,
      {
        withCredentials: true
      }
    )
  }

  putRequest(url: string, body: any): Observable<Object> {
    return this.http.put(
      this.baseUrl + url,
      body,
      {
        withCredentials: true
      }
    )
  }

  deleteRequest(url: string): Observable<Object> {
    return this.http.delete(
      this.baseUrl + url,
      {
        withCredentials: true
      }
    )
  }
}
