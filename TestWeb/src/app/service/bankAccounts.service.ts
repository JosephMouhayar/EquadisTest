import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BankAccountsService {
  private apiUrl = 'http://localhost:8080/api/bankAccounts/';

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

  // Method to get all bank accounts for a given customer
  getBankAccounts(customerID: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}getBankAccounts?customerID=${customerID}`, { headers: this.getHeaders() });
  }

  // Method to initialize a new bank account for a given customer
  initializeBankAccount(customerID: number): Observable<any> {
    // Make POST request to initialize the bank account with amount 0
    return this.http.post<any>(`${this.apiUrl}initializeBankAccount?customerID=${customerID}`, {}, { headers: this.getHeaders() });
  }

  // Method to delete a bank account for a given bank ID
  deleteBankAccount(bankAccountID: number): Observable<any> {
    // Make DELETE request to delete the bank account by bank ID
    return this.http.delete<any>(`${this.apiUrl}deleteBankAccount?bankAccountID=${bankAccountID}`, { headers: this.getHeaders() });
  }
}
