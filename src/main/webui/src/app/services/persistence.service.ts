import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PersistenceService {
  constructor(private http: HttpClient) { }

  getRequest(url: string): Observable<Object> {
    return this.http.get(
      url,
      {
        withCredentials: true
      }
    )
  }

  postRequest(url: string, body: any): Observable<Object> {
    return this.http.post(
      url,
      body
    )
  }

  putRequest(url: string, body: any): Observable<Object> {
    return this.http.put(
      url,
      body,
      {
        withCredentials: true
      }
    )
  }

  deleteRequest(url: string): Observable<Object> {
    return this.http.delete(
      url,
      {
        withCredentials: true
      }
    )
  }
}
