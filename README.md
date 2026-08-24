# 🦷 BrightSmile — Dental Clinic Management System

**BrightSmile** is a full-stack web application designed for the management of a dental clinic. The system provides a modern and responsive interface for patients and administrators, allowing appointment management, patient information management, clinical records, services, schedules, and authentication.

The project was developed as a practical **Full-Stack application** using **Angular, TypeScript, Java, Spring Boot, PostgreSQL, and JWT authentication**.

---

## 🚀 Features

### 👤 Patient

* User registration and authentication.
* Secure login using JWT.
* Appointment scheduling.
* Selection of dental services.
* Consultation of available schedules.
* Responsive interface for desktop and mobile devices.
* AI-powered chatbot assistant for answering questions about the clinic and its services.

### 🧑‍💼 Administrator

* Secure administrator authentication.
* Appointment management.
* Patient management.
* Clinical history management.
* Dental service management.
* Schedule and availability management.
* Appointment status management.
* Responsive administration dashboard.

### 🤖 AI Assistant

BrightSmile includes an AI-powered virtual assistant called **Bright**, designed to help users with common questions about:

* Dental services.
* Clinic information.
* Appointment-related questions.
* Service prices.
* General information about the platform.

The assistant is integrated using the **OpenAI API**.

---

## 🛠️ Technologies

### Frontend

* Angular 19
* TypeScript
* HTML5
* CSS3
* Reactive Forms
* Angular Router
* Responsive Web Design

### Backend

* Java 21
* Spring Boot 3.5
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* Lombok
* Maven
* Swagger / OpenAPI

### Database

* PostgreSQL 17

### AI

* OpenAI API

### Development Tools

* Visual Studio Code
* IntelliJ IDEA
* Git
* GitHub

---

## 🏗️ Architecture

The application follows a client-server architecture:

```text
                    ┌─────────────────────┐
                    │      BrightSmile    │
                    │    Web Application  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Angular Frontend  │
                    │      Angular 19     │
                    └──────────┬──────────┘
                               │
                         HTTP / REST API
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Spring Boot API   │
                    │       Java 21       │
                    └──────────┬──────────┘
                               │
                    ┌──────────┴──────────┐
                    ▼                     ▼
             ┌──────────────┐     ┌──────────────┐
             │ PostgreSQL   │     │  OpenAI API  │
             │     17       │     │  AI Assistant│
             └──────────────┘     └──────────────┘
```

---

## 📂 Project Structure

The project is divided into two main repositories:

### Frontend — Angular

Contains the user interface and client-side application.

**Technologies:** Angular 19 · TypeScript · HTML · CSS

👉 **Frontend Repository:**

https://github.com/ederj08/BrightSmile-Frontend.git

### Backend — Spring Boot

Contains the REST API, business logic, authentication, database integration, and security configuration.

**Technologies:** Java 21 · Spring Boot · Spring Security · PostgreSQL · JWT

👉 **Backend Repository:**
https://github.com/ederj08/BrightSmile-Backend.git

---

## 🔐 Security

The backend implements authentication and authorization using **Spring Security and JWT (JSON Web Tokens)**.

The system protects restricted endpoints and differentiates access according to the user's role.

```text
User
 │
 ▼
Login
 │
 ▼
Spring Security
 │
 ▼
JWT Token
 │
 ▼
Authorized Requests
 │
 ├── Patient
 │
 └── Administrator
```

---

## 📅 Appointment Management

The appointment module allows patients to:

1. Select a dental service.
2. View available schedules.
3. Select a date and time.
4. Create an appointment.

Administrators can subsequently manage appointments from the administration dashboard.

---

## 🦷 Dental Services

The platform supports different dental services, such as:

* Dental evaluation.
* Dental cleaning.
* Preventive care.
* General dental procedures.

The service selected by the patient is associated with the appointment and can be managed by the administrator.

---

## 📋 Clinical History

Administrators can manage patient clinical information through the clinical history module.

This allows the clinic to maintain organized patient information associated with their dental care.

---

## 📱 Responsive Design

BrightSmile was designed with a responsive approach to provide a consistent experience across:

* 💻 Desktop
* 📱 Mobile
* 📟 Tablet

The administration dashboard also includes a responsive navigation menu adapted for smaller screens.

---

## ⚙️ Installation

### Prerequisites

Before running the project, make sure you have installed:

* Java 21
* Node.js
* Angular CLI
* PostgreSQL 17
* Maven
* Git

### 1. Clone the repositories

Clone both the frontend and backend repositories.

```bash
git clone <FRONTEND_REPOSITORY_URL>
git clone <BACKEND_REPOSITORY_URL>
```

### 2. Configure PostgreSQL

Create the database required by the backend and configure the database connection in:

```text
src/main/resources/application.properties
```

### 3. Run the backend

From the backend directory:

```bash
mvn spring-boot:run
```

The API will run on:

```text
http://localhost:8080
```

### 4. Run the frontend

From the frontend directory:

```bash
npm install
ng serve
```

The Angular application will normally be available at:

```text
http://localhost:4200
```

---

## 🔑 Environment Variables

For security reasons, sensitive credentials such as database passwords and API keys should not be committed to the repository.

Configure the required environment variables according to the backend configuration.

Example:

```text
DB_USERNAME=your_username
DB_PASSWORD=your_password
OPENAI_API_KEY=your_api_key
JWT_SECRET=your_secret
```

---

## 📸 Project Preview

*Add screenshots of the main application screens here.*

Recommended screenshots:

* Home page
* Login
* Appointment scheduling
* Patient dashboard
* Administrator dashboard
* Clinical history
* AI chatbot

---

## 🎯 Project Purpose

BrightSmile was developed as a **Full-Stack portfolio project** to demonstrate practical experience building a complete web application, from the frontend user interface to the backend REST API, database, authentication, authorization, and third-party AI integration.

The project demonstrates the integration of:

**Angular → REST API → Spring Boot → PostgreSQL**

along with secure authentication and AI-assisted functionality.

---

## 👨‍💻 Author

**Eder Acuña**

Telecommunications Engineer | Junior Full-Stack Developer

**Main technologies:**

`Java` · `Spring Boot` · `Angular` · `TypeScript` · `PostgreSQL` · `JavaScript` · `HTML` · `CSS`

---

## 📄 License

This project was developed for educational and portfolio purposes.
