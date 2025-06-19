 Customer Management API

A lightweight Spring Boot REST service for creating, retrieving, updating, and deleting customer records.  
The project uses an in-memory **H2** database by default and can be run locally with zero external dependencies.

---

1  Build & Run

| Step | Command | Notes |
|------|---------|-------|
| **Clone** | `git clone https://github.com/vimaldeveloper01/Customer-Manager-API.git`
| **Build JAR** | `./mvnw clean package` | Uses the Maven Wrapper; generates `target/customermanagement-0.0.1-SNAPSHOT.jar` |
| **Run** | `java -jar target/customermanagement-0.0.1-SNAPSHOT.jar` | Starts on **`http://localhost:8080`** |

> **Prerequisites**: JDK 17 + , Maven 3.9 +  (or use the Maven Wrapper).

---

2  Sample Requests

2.1 Create Customer

curl --location 'http://localhost:8080/customers' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "vimalkumar1",
    "email": "vimalkumar1@gmail.com",
    "annualSpend": 11100.50,
    "lastPurchaseDate": "2025-04-15T14:30:00"
}'

2.2 Get Customer by ID

curl http://localhost:8080/customers/5580eac4-3785-412a-8bc2-ee0e7cbe3eed


 2.3 Get Customers by Name (contains match)

curl http://localhost:8080/api/customers?name=vimalkumar1

 2.4 Get Customer by Email

curl http://localhost:8080/api/customers/email/vimalkumar1@gmail.com

 2.5 Update Customer

curl --location --request PUT 'http://localhost:8080/customers/5580eac4-3785-412a-8bc2-ee0e7cbe3eed' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "vimalkumar",
    "email": "vimalkumar@gmail.com",
    "annualSpend": 1100.50,
    "lastPurchaseDate": "2025-04-15T14:30:00"
}'


 2.6 Delete Customer

curl -X DELETE http://localhost:8080/api/customers/5580eac4-3785-412a-8bc2-ee0e7cbe3eed


---

 3  H2 Console (access if needed)

| URL | `http://localhost:8080/h2-console` |
|---|---|
| **Driver Class** | `org.h2.Driver` |
| **JDBC URL** | `jdbc:h2:mem:customer_db` |
| **User / Password** | `sa` / *(leave blank)* |

---