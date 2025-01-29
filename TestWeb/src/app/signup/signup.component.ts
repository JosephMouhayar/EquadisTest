import { Component } from '@angular/core';
import { CustomerService } from '../service/customer.service'; // Assuming CustomerService is correctly imported
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http'; // Import HttpClientModule
import { CommonModule } from '@angular/common'; // Import CommonModule for ngIf


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, RouterModule, HttpClientModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  customerData = { name: '', password: '' }; // customer data without c_password
  c_password: string = ''; // this holds the confirm password value temporarily
  value = ""; // for holding the virtual keyboard input
  isShiftActive = false; // flag to track shift key status

  signupFailed: boolean = false; // Flag for signup failure
  errorMessage: string = ''; // Message for signup failure

  constructor(private customer: CustomerService, private router: Router) { }

  ngOnInit() {
    // Subscribing to the user data to update customerData
    this.customer.currentUserData.subscribe(customerData => {
      this.customerData = customerData; // Update customerData without c_password
    });
  }

  signUp() {
    if (this.customerData.password !== this.c_password) {
      this.signupFailed = true;
      this.errorMessage = 'Passwords do not match!';
      return;
    }

    this.customer.signUpCustomer(this.customerData).subscribe(
      (response) => {
        this.signupFailed = false;
        alert('Sign up successful!');
        this.router.navigate(['/login']); // Redirect to login page after success
      },
      (error) => {
        console.error('Signup failed:', error);
        this.signupFailed = true;

        // Extract and show error message from the response
        if (error.error === 'Customer already exists') {
          this.errorMessage = 'Customer already exists. Please choose a different name.';
        } else {
          this.errorMessage = 'Signup failed. Please try again.';
        }
        console.log(this.errorMessage);
      }
    );
  }

  // Handle key press for input
  onKeyPress(button: string) {
    if (button === 'shift') {
      this.toggleShift();
    } else if (button === 'backspace') {
      this.value = this.value.slice(0, -1);
    } else if (button === 'space') {
      this.value += ' ';
    } else {
      this.value += button;
    }

    console.log("Current Input:", this.value);
  }

  // Toggle shift key functionality
  toggleShift() {
    this.isShiftActive = !this.isShiftActive;
  }

  // Keyboard Layout (shifted and unshifted keys)
  getKeyboardLayout() {
    const defaultLayout = [
      ['q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'],
      ['a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'],
      ['shift', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'backspace'],
      ['space']
    ];

    const shiftedLayout = [
      ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
      ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
      ['shift', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', 'backspace'],
      ['space']
    ];

    return this.isShiftActive ? shiftedLayout : defaultLayout;
  }
}
