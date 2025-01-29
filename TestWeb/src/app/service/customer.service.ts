import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

// Define an interface for user data to enforce type safety
interface CustomerData {
  name: string;
  password: string;
}

@Injectable({
  providedIn: 'root',  // Use 'root' to provide the service globally
})
export class CustomerService {
  private userDataSource = new BehaviorSubject<CustomerData>({ name: '', password: '' });
  currentUserData = this.userDataSource.asObservable();

  private apiUrl = 'http://localhost:8080/api/customer/';  // Adjust the API endpoint as needed

  constructor(private http: HttpClient) {}

  // Method to update the user data with proper typing
  changeData(newUserData: CustomerData): void {
    this.userDataSource.next(newUserData);
  }

  // Method to send signup data to the backend
  signUpCustomer(customerData: CustomerData): Observable<any> {
    return this.http.post<any>(this.apiUrl + "signup", customerData);
  }

  // Method to send signup data to the backend
  loginCustomer(name: string, password: string): any {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const body = {name, password};

    return this.http.post(this.apiUrl + 'authenticate', body, { observe: 'response' }); // Observe the full response to access headers
  }

  decodeToken(token: string): any {
    return jwtDecode(token); // Decode the token to get claims
  }
}
