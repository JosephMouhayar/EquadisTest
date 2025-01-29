import { Component, OnInit } from "@angular/core";
import { CustomerService } from "../service/customer.service";
import { FormsModule } from "@angular/forms";
import { RouterModule, Router } from "@angular/router";
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from "@angular/common";

interface CustomerData {
  name: string;
  password: string;
}

@Component({
  selector: "app-login",
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"]
})
export class LoginComponent implements OnInit {
  customerData: CustomerData = { name: '', password: '' };
  errorMessage: string = ''; // Added error message variable

  constructor(private customer: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.customer.currentUserData.subscribe((userData: CustomerData) => {
      this.customerData = userData;
    });
  }

  changeData(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const msg = inputElement.value;
    this.customer.changeData({ name: msg, password: this.customerData.password });
  }

  login(customerData: CustomerData) {
    this.customer.loginCustomer(customerData.name, customerData.password).subscribe(
      (response: HttpResponse<any>) => {
        const token = response.headers.get('Authorization'); // Retrieve the JWT from the header
        if (token) {
          const decodedToken = this.customer.decodeToken(token.replace('Bearer ', ''));
          const customerId = decodedToken.customerID; // Use exact claim keys from your backend
          const name = decodedToken.name;

          localStorage.setItem('token', token);
          localStorage.setItem("customerID", customerId);
          localStorage.setItem("name", name);

          this.router.navigate(['/dashboard']); // Redirect to login page after success
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Authentication failed', error);
        this.errorMessage = 'Login failed. Please check your credentials and try again.'; // Set error message
      }
    );
  }
}
