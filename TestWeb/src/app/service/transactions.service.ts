import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = 'http://localhost:8080/api/transactions/';

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

  getTransactionsByBankAccount(bankAccountID: number, page: number, size: number, sortBy: string, sortDir: string): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
  
    // Add the headers to the request
    const headers = this.getHeaders();
  
    return this.http.get<any>(`${this.apiUrl}getTransactions/${bankAccountID}`, { params, headers })
      .pipe(
        catchError(error => {
          console.error('Error fetching transactions:', error);
          return throwError(() => error);
        })
      );
  }
  

  addTransaction(transaction: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}add`, transaction, { headers: this.getHeaders() })
      .pipe(
        catchError(error => {
          console.error('Error adding transaction:', error);
          return throwError(() => error.error.message || 'Transaction failed: Bank account cannot have a negative balance.');
        })
      );
  }

  deleteTransaction(transactionID: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}delete/${transactionID}`, { headers: this.getHeaders() })
      .pipe(
        catchError(error => {
          console.error('Error deleting transaction:', error);
          return throwError(() => error.error.message || 'Transaction deletion failed: Bank account cannot have a negative balance.');
        })
      );
  }
}
