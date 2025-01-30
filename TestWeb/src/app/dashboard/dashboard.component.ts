import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BankAccountsService } from '../service/bankAccounts.service';
import { TransactionService } from '../service/transactions.service';
import { CategoryService } from '../service/categories.service';


interface Bank {
  bankAccountID: number;
  amount: number;
  transactions: Transaction[];  // Transactions specific to each bank
}


interface Transaction {
  transactionID: number;
  categoryID: number;
  categoryName: string;
  bankAccountID: number;
  type: string;
  amount: number;
  createdAt: Date;
}

@Component({
  selector: 'app-dashboard',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  customerID: string | null = null;
  name: string | null = null;
  
  banks: Bank[] = [];

  selectedBank: Bank | null = null; // Store the selected bank

  // Transaction related properties
  showTransactionForm: boolean = false;
  transactionForm: FormGroup;

  categories: any[] = [];

  // Pagination
  totalPages: number = 0;
  currentPage: number = 0;
  pageSize: number = 5; // Number of transactions per page

  errorMessage: string = '';

  constructor(private fb: FormBuilder, private router: Router, private http: HttpClient, private bankAccountsService: BankAccountsService,  private transactionService: TransactionService, private categoryService: CategoryService) {
    this.transactionForm = this.fb.group({
      categoryID: ['', Validators.required],
      type: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    // Retrieve customerID and name from localStorage
    this.customerID = localStorage.getItem('customerID');
    this.name = localStorage.getItem('name');
  
    // Redirect if not logged in
    if (!this.customerID || !this.name) {
      this.router.navigate(['/login']);
      return;
    }
  
    // Fetch banks from the API
    this.getBankAccounts();

    // Fetch categories from the API
    this.loadCategories();
  }

  showError(message: string) {
    this.errorMessage = message;
  
    // Hide the message after 5 seconds
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }

  getBankAccounts(): void {
    if (!this.customerID) return;

    this.bankAccountsService.getBankAccounts(this.customerID).subscribe({
      next: (response) => {
        this.banks = response;
      },
      error: (error) => {
        console.error('Error fetching bank accounts:', error);
      }
    });
  }
  
  // Call the service method to initialize a new bank account for the customer
  initializeNewBankAccount(): void {
    if (!this.customerID) return;

    this.bankAccountsService.initializeBankAccount(Number(this.customerID)).subscribe({
      next: (response) => {
        this.banks.push(response);
        console.log('Bank account initialized successfully:', response);
      },
      error: (error) => {
        console.error('Error initializing bank account:', error);
      }
    });
  }
  

  // Method to delete a bank account
  deleteBank(bankAccountID: number): void {
    this.bankAccountsService.deleteBankAccount(bankAccountID).subscribe({
      next: (response) => {
        console.log('Bank account deleted successfully:', response);
        
        // After deletion, remove the bank account from the banks array
        this.banks = this.banks.filter(bank => bank.bankAccountID !== bankAccountID);
      },
      error: (error) => {
        console.error('Error deleting bank account:', error);
      }
    });
  }

  // Select a bank and show its transactions
  selectBank(bank: Bank): void {
    this.selectedBank = bank;
    this.loadTransactions(); // Load transactions for the selected bank
  }

  // Transaction related methods
  toggleTransactionForm(): void {
    this.showTransactionForm = !this.showTransactionForm;
    if (!this.showTransactionForm) {
      this.transactionForm.reset();
    }
  }

  // Load the selected Bank Account Transactions
  loadTransactions(page: number = 0): void {
    if (this.selectedBank) {
      this.transactionService.getTransactionsByBankAccount(this.selectedBank.bankAccountID, page, this.pageSize, 'createdAt', 'desc')
        .subscribe({
          next: (data) => {
            if (this.selectedBank) {
              this.selectedBank.transactions = data.content; // Extract paginated transaction data
              this.totalPages = data.totalPages;
              this.currentPage = data.number;
            }
          },
          error: (err) => {
            console.error('Error fetching transactions:', err);
          }
        });
    }
  }

  // Change of Page for Transactions List Pagination
  changePage(newPage: number): void {
    if (newPage >= 0 && newPage < this.totalPages) {
      this.loadTransactions(newPage);
    }
  }

  // Add a new transaction
  saveTransaction(): void {
    if (!this.transactionForm.valid || !this.selectedBank) {
      console.error("Form is invalid or no bank selected.");
      return;
    }

    const formValues = this.transactionForm.value;
    const newTransaction = {
      type: formValues.type,
      amount: formValues.amount,
      createdAt: new Date().toISOString(),
      bankAccountID: this.selectedBank.bankAccountID,
      categoryID: formValues.categoryID
    };

    this.transactionService.addTransaction(newTransaction)
      .subscribe({
        next: (data) => {
          console.log('Transaction added:', data);
          this.loadTransactions(); // Reload transactions after adding
          this.getBankAccounts();   // Reload bank accounts to update balance
          this.transactionForm.reset(); // Reset the form after submission
        },
        error: (err) => {
          console.error('Error adding transaction:', err);
          this.showError(err); // Display error message
        }
      });

      this.showTransactionForm = false;
  }

  // Delete a transaction by its ID
  deleteTransaction(transactionID: number): void {
    this.transactionService.deleteTransaction(transactionID)
      .subscribe({
        next: () => {
          console.log('Transaction deleted');
          this.loadTransactions(); // Reload transactions after deletion
          this.getBankAccounts();   // Reload bank accounts to update balance
        },
        error: (err) => {
          console.error('Error deleting transaction:', err);
          this.showError(err); // Display error message
        }
      });
  }

  // Load all Categories
  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Error fetching categories:', err);
      }
    });
  }

  // Logout method
  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
