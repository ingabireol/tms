# Theatre Management System

A comprehensive software application designed to manage and automate the operations of a theatre or cinema. The system handles tasks such as show scheduling, ticket booking, seat allocation, and customer management.

## Features

### Core Functionality
- **Movie Management**: Add, update, and manage movie information including title, description, duration, genres, ratings, and release dates
- **Theatre Management**: Manage multiple theatre locations with detailed information about facilities and contact details
- **Hall Management**: Configure different types of halls (Standard, Premium, IMAX, 3D, 4DX) with seat capacity
- **Screening Management**: Schedule movie screenings with date, time, pricing, and hall allocation
- **Booking System**: Complete ticket booking system with seat selection and booking number generation
- **Payment Processing**: Handle multiple payment methods (Cash, Credit/Debit Card, Mobile Money, PayPal)
- **User Management**: User registration and profile management with role-based access (Customer, Admin, Staff)
- **Location Hierarchy**: Comprehensive Rwanda location system with Province > District > Sector > Cell > Village hierarchy

### Additional Features
- Booking status tracking (Pending, Confirmed, Cancelled, Completed)
- Payment status management (Pending, Completed, Failed, Refunded)
- Real-time seat availability tracking
- Pagination and sorting for movie listings
- Search functionality for movies and locations
- Automated booking number and transaction ID generation

## Technologies Used

### Backend
- **Java 17**
- **Spring Boot 3.5.7**
  - Spring Web
  - Spring Data JPA
  - Spring Boot Starter Test
- **PostgreSQL** - Database
- **Lombok** - Reduce boilerplate code
- **Maven** - Dependency management

### API Documentation
- **SpringDoc OpenAPI 2.8.13** - Swagger UI for API documentation

## Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 17 or higher
- Maven 3.9.x
- PostgreSQL 12 or higher
- Your favorite IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Database Setup

1. Install PostgreSQL if not already installed
2. Create a new database:
```sql
CREATE DATABASE theatre_db;
```

3. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/theatre_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Installation & Setup

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/Theatre_Management_System_26509.git
cd Theatre_Management_System_26509
```

2. **Configure the application**
   - Edit `src/main/resources/application.properties` with your database credentials

3. **Build the project**
```bash
./mvnw clean install
```
Or on Windows:
```bash
mvnw.cmd clean install
```

4. **Run the application**
```bash
./mvnw spring-boot:run
```
Or on Windows:
```bash
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the Swagger UI for interactive API documentation:

```
http://localhost:8080/swagger-ui.html
```

API Documentation JSON:
```
http://localhost:8080/v3/api-docs
```

## Entity Relationship Diagram


![ERD Diagram](/erd.png)


## API Endpoints Overview

### User Management
- `POST /api/users/register` - Register new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/location/{locationCode}` - Get users by location
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Movie Management
- `POST /api/movies` - Create new movie
- `GET /api/movies` - Get all movies (with pagination)
- `GET /api/movies/{id}` - Get movie by ID
- `GET /api/movies/active` - Get active movies
- `GET /api/movies/genre/{genreName}` - Get movies by genre
- `GET /api/movies/search?title={title}` - Search movies
- `PUT /api/movies/{id}` - Update movie
- `DELETE /api/movies/{id}` - Deactivate movie

### Theatre Management
- `POST /api/theatres?locationCode={code}` - Create theatre
- `GET /api/theatres` - Get all theatres
- `GET /api/theatres/{id}` - Get theatre by ID
- `GET /api/theatres/active` - Get active theatres
- `GET /api/theatres/location/{locationCode}` - Get theatres by location
- `PUT /api/theatres/{id}` - Update theatre
- `DELETE /api/theatres/{id}` - Deactivate theatre

### Hall Management
- `POST /api/halls?theatreId={id}` - Create hall
- `GET /api/halls` - Get all halls
- `GET /api/halls/{id}` - Get hall by ID
- `GET /api/halls/theatre/{theatreId}` - Get halls by theatre
- `GET /api/halls/type/{type}` - Get halls by type
- `PUT /api/halls/{id}` - Update hall
- `DELETE /api/halls/{id}` - Deactivate hall

### Screening Management
- `POST /api/screenings` - Create screening
- `GET /api/screenings` - Get all screenings
- `GET /api/screenings/{id}` - Get screening by ID
- `GET /api/screenings/movie/{movieId}` - Get screenings by movie
- `GET /api/screenings/theatre/{theatreId}` - Get screenings by theatre
- `GET /api/screenings/date/{date}` - Get screenings by date
- `PUT /api/screenings/{id}` - Update screening
- `DELETE /api/screenings/{id}` - Cancel screening

### Booking Management
- `POST /api/bookings` - Create booking
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/number/{bookingNumber}` - Get booking by number
- `GET /api/bookings/user/{userId}` - Get user's bookings
- `GET /api/bookings/screening/{screeningId}` - Get bookings by screening
- `PUT /api/bookings/{id}/confirm` - Confirm booking
- `PUT /api/bookings/{id}/cancel` - Cancel booking

### Payment Management
- `POST /api/payments/booking/{bookingId}` - Create payment
- `GET /api/payments/{id}` - Get payment by ID
- `GET /api/payments/booking/{bookingId}` - Get payment by booking
- `GET /api/payments/transaction/{transactionId}` - Get payment by transaction
- `PUT /api/payments/{id}/process` - Process payment
- `PUT /api/payments/{id}/refund` - Refund payment

### Location Management
- `POST /api/locations` - Create location
- `GET /api/locations` - Get all locations
- `GET /api/locations/{id}` - Get location by ID
- `GET /api/locations/code/{code}` - Get location by code
- `GET /api/locations/provinces` - Get all provinces
- `GET /api/locations/children/{parentCode}` - Get child locations
- `GET /api/locations/province/{provinceCode}/hierarchy` - Get province hierarchy
- `PUT /api/locations/{id}` - Update location
- `DELETE /api/locations/{id}` - Delete location


## Exception Handling

The application includes global exception handling for:
- `ResourceNotFoundException` (404)
- `ValidationException` (400)
- `DuplicateResourceException` (409)
- General exceptions (500)

All errors return a consistent JSON response with timestamp, status, error message, and path.