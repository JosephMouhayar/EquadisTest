# Equadis Test

## Overview
Equadis Test is a financial management application that allows users to manage bank accounts and transactions. The backend is built using Spring Boot, and the frontend is developed using Angular.

## Prerequisites
Before running the project, ensure you have the following installed:

- **Java 23** (For Spring Boot backend)
- **Maven** (For dependency management)
- **Node.js & npm** (For Angular frontend)
- **Microsoft SQL Server** (For database management)
- **Git** (For cloning the repository)

## Setup Instructions

### 1. Clone the Repository
```sh
 git clone https://github.com/JosephMouhayar/EquadisTest.git
 cd EquadisTest
```

### 2. Database Setup
1. Open MySQL and create a new database:
   ```sql
   CREATE DATABASE Aquadis;
   ```
2. Run the SQL script **AquadisDb.sql** located in the root of the repository to generate the database schema and insert initial data:
   ```sh
   mysql -u <your-username> -p Aquadis < AquadisDb.sql
   ```

### 3. Backend Setup (Spring Boot)
Navigate to the `test` folder (backend directory):
```sh
cd test
```

#### Configure Database Connection
Edit the `application.properties` file located in `src/main/resources/`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Aquadis
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

#### Build and Run the Backend
```sh
mvn clean install
mvn spring-boot:run
```
The backend will start at `http://localhost:8080`.

### 4. Frontend Setup (Angular)
Navigate to the frontend directory and install dependencies:
```sh
cd TestWeb  # Adjust if the folder name is different
npm install
```

#### Run the Angular Application
```sh
ng serve --open
```
The frontend will run at `http://localhost:4200`.

## Running the Application
1. Ensure SQL Server is running and the database is set up.
2. Start the backend using `mvn spring-boot:run`.
3. Start the frontend using `ng serve --open`.
4. Open `http://localhost:4200` in your browser.

## Additional Information

- **Steps.docx**: A document containing a detailed step-by-step guide of the development process followed in this project. It serves as a project report, outlining the methodologies, setup, and other essential details.
