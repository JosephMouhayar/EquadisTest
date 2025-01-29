import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = 'http://localhost:8080/api/categories/';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token'); 
    if (!token) {
      console.error('No token found in localStorage');
      throw new Error('Unauthorized: No token found');
    }

    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  // Method to retrieve all categories
  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}getCategories`, { headers: this.getHeaders() });
  }

}


