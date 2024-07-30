# CRUD Application with Spring Boot

This project demonstrates a simple CRUD (Create, Read, Update, Delete) application using Spring Boot with a PostgreSQL database. It provides RESTful APIs to manage books, including functionalities to retrieve all books, retrieve a book by ID, add a new book, update an existing book, and delete a book.

## Prerequisites

- Java 17
- Maven
- PostgreSQL
- Docker (for running PostgreSQL in a container)
- pgAdmin 4 (for managing PostgreSQL)
- POSTMAN (for testing RESTful APIs)
- IDE (Eclipse, IntelliJ IDEA, etc.)

## Setup Instructions

### Docker Setup for PostgreSQL

To quickly set up a PostgreSQL database using Docker, follow these steps:

1. **Pull the PostgreSQL Docker Image:**

   ```bash
   docker pull postgres
   ```

2. **Run PostgreSQL Container:**

   ```bash
   docker run --name PostgresDB -p 15432:5432 -e POSTGRES_USER=your_username -e POSTGRES_PASSWORD=your_password -e POSTGRES_DB=postgres -d postgres
   ```

   This command will start a PostgreSQL container named `PostgresDB`, expose it on port `15432`, and create a database named `postgres` with the username and password.

### Register PostgreSQL Database Connection in pgAdmin 4

1. **Open pgAdmin 4:**

   Launch pgAdmin 4 in your web browser. By default, it should be accessible at `http://localhost:80`.

2. **Add a New Server:**

   - Click on the **"Add New Server"** button in the dashboard or from the **"Servers"** section in the left sidebar.

3. **Configure Connection:**

   - **General Tab:**
     - **Name**: Enter a name for your server, e.g., `PostgresDB`.

   - **Connection Tab:**
     - **Host name/address**: `localhost`
     - **Port**: `15432`
     - **Maintenance database**: `postgres`
     - **Username**: `your_username`
     - **Password**: `your_password`
     - Check the box to **Save Password** if you wish.

   - Click **"Save"** to register the server.

4. **Verify Connection:**

   - Once added, you should see `PostgresDB` listed under **Servers** in the left sidebar.
   - Expand the server node to see the databases and verify that the `postgres` database is listed.

### Application Setup

1. **Clone the Repository:**

   ```bash
   git clone <repository-url>
   cd CRUDApplication
   ```

2. **Configure Database:**

   Ensure PostgreSQL is installed and running. Update the database credentials in `src/main/resources/application.properties` to match the Docker container configuration:

   ```properties
   spring.application.name=CRUDApplication
   server.port=9090
   spring.datasource.url=jdbc:postgresql://localhost:15432/postgres
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the Project:**

   Use Maven to build the project:

   ```bash
   mvn clean install
   ```

4. **Run the Application:**

   Start the Spring Boot application:

   ```bash
   java -jar target/CRUDApplication-0.0.1-SNAPSHOT.jar
   ```

   Alternatively, you can run it directly from your IDE by executing the `CrudApplication` class, which contains the `main` method.

## API Endpoints

You can interact with the CRUD application using POSTMAN for testing RESTful APIs. Below are the available endpoints:

### 1. **Get All Books**

   - **Endpoint**: `GET /getAllBooks`
   - **Description**: Retrieves all books from the database.
   - **Response**:
     - `200 OK` with a list of books if successful.
     - `204 No Content` if no books are found.

### 2. **Get Book by ID**

   - **Endpoint**: `GET /getBookById/{id}`
   - **Description**: Retrieves a book by its ID.
   - **Path Variable**: `{id}` is the ID of the book.
   - **Response**:
     - `200 OK` with the book details if found.
     - `404 Not Found` if the book with the specified ID does not exist.

### 3. **Add a Book**

   - **Endpoint**: `POST /addBook`
   - **Description**: Adds a new book to the database.
   - **Request Body**: JSON object representing the book (title and author).
   - **Example Request Body**:
     ```json
     {
       "title": "The Great Gatsby",
       "author": "F. Scott Fitzgerald"
     }
     ```
   - **Response**:
     - `200 OK` with the added book details.

### 4. **Update Book by ID**

   - **Endpoint**: `POST /updateBookById/{id}`
   - **Description**: Updates an existing book by its ID.
   - **Path Variable**: `{id}` is the ID of the book to update.
   - **Request Body**: JSON object containing updated title and author.
   - **Example Request Body**:
     ```json
     {
       "title": "The Great Gatsby (Updated)",
       "author": "F. Scott Fitzgerald"
     }
     ```
   - **Response**:
     - `200 OK` with the updated book details if successful.
     - `404 Not Found` if the book with the specified ID does not exist.

### 5. **Delete Book by ID**

   - **Endpoint**: `DELETE /deleteBookById/{id}`
   - **Description**: Deletes a book by its ID.
   - **Path Variable**: `{id}` is the ID of the book to delete.
   - **Response**:
     - `200 OK` on successful deletion.

## Interacting with the Application Using POSTMAN

1. **Open POSTMAN:**

   Launch POSTMAN and create a new request.

2. **Set Up Requests:**

   - **GET All Books**: 
     - Method: `GET`
     - URL: `http://localhost:9090/getAllBooks`

   - **GET Book by ID**: 
     - Method: `GET`
     - URL: `http://localhost:9090/getBookById/{id}` (replace `{id}` with the actual book ID)

   - **POST Add a Book**: 
     - Method: `POST`
     - URL: `http://localhost:9090/addBook`
     - Body: Select `raw` and `JSON` format, then enter the JSON object for the book.

   - **POST Update Book by ID**: 
     - Method: `POST`
     - URL: `http://localhost:9090/updateBookById/{id}` (replace `{id}` with the actual book ID)
     - Body: Select `raw` and `JSON` format, then enter the updated JSON object for the book.

   - **DELETE Book by ID**: 
     - Method: `DELETE`
     - URL: `http://localhost:9090/deleteBookById/{id}` (replace `{id}` with the actual book ID)

## Interacting with the Application Using pgAdmin 4

1. **Open pgAdmin 4:**

   Launch pgAdmin 4 in your web browser. By default, it should be accessible at `http://localhost:80`.

2. **Connect to the Database:**

   - Follow the steps provided in the **Register PostgreSQL Database Connection in pgAdmin 4** section.

3. **View Tables and Data:**

   - Expand the `PostgresDB` server, navigate to the `Databases` node, and select the `postgres` database.
   - Under `Schemas`, expand `public`, and then click on `Tables` to view the `Books` table.
   - Right-click on the `Books` table and select **View/Edit Data** to interact with the data.

## Technologies Used

- **Spring Boot**: Simplifies the development of Spring applications.
- **Spring Data JPA**: Provides easy integration with JPA repositories.
- **PostgreSQL**: Relational database management system used for persistence.
- **Lombok**: Reduces boilerplate code by providing annotations.

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.CRUDApplication/
│   │   │       ├── controller/
│   │   │       │   └── BookController.java
│   │   │       ├── model/
│   │   │       │   └── Book.java
│   │   │       ├── repo/
│   │   │       │   └── BookRepo.java
│   │   │       └── CrudApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com.example.CRUDApplication/
│               └── controller/
│                   └── BookController

Test.java
└── pom.xml
```

## Notes

- The application uses annotations like `@RestController`, `@Entity`, `@Repository`, and `@Autowired` for implementing RESTful services and managing data persistence.
- Tests are included under `src/test` to ensure the correctness of API endpoints.

## Further Improvements

- Implement validation for request payloads (e.g., using `@Valid` and `@NotBlank`).
- Enhance error handling and exception management.
- Add logging and security measures depending on deployment requirements.

This README provides an overview of the CRUDApplication project, demonstrating how to set up, run, and use the application effectively. Adjustments and enhancements can be made based on specific project requirements and deployment environments.
