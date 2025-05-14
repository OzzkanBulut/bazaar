# 🛍️ Bazaar Backend 

This repository contains the **backend API** for **Bazaar**, a scalable, full-featured, multi-vendor e-commerce platform. The backend is built with **Spring Boot** and handles authentication, product and order management, payment processing, and role-based access control.

Bazaar supports two distinct roles: **Customers** and **Sellers**, each with their own access and responsibilities. The backend communicates with a PostgreSQL database, manages real-time alerts using RabbitMQ, and integrates Stripe for secure online payments.

Bazaar supports two user roles:
- 👤 **Customer** – Can browse products, add items to cart, add items to wishlist and place orders.
- 🛍️ **Seller** – Can manage their own products and handle incoming orders.

---

## 🚀 Project Purpose

Bazaar aims to replicate a real-world e-commerce experience with secure, scalable technologies. This project demonstrates:
- OTP-based login system
- Role-based resource access (Customer / Seller)
- Stripe integration for payments
- Asynchronous real-time notifications with RabbitMQ
- Clean architecture with layered separation
- JavaMailSender usage for email-based OTP verification

This backend powers the entire business logic and integrates smoothly with a React-based frontend.

---

## 🧰 Technologies Used

| Technology         | Description                                        |
|--------------------|----------------------------------------------------|
| Java 21            | Main programming language                          |
| Spring Boot        | Application framework                              |
| Spring Security    | Authentication and role-based access control       |
| JWT (JSON Web Token) | Stateless token-based session system             |
| PostgreSQL         | Relational database                                |
| Hibernate/JPA      | ORM for DB operations                              |
| JavaMailSender     | Sends OTP codes via email                          |
| RabbitMQ           | Event-driven order notifications                   |
| Stripe API         | Payment integration                                |
| Lombok             | Reduces boilerplate code                           |
| Maven              | Build and dependency tool                          |

---

## ✅ Features (Detailed)

### 🔐 OTP-Based Authentication
- No password required
- Users receive a **time-sensitive OTP** via email during login/register
- OTPs are verified and deleted immediately after validation
- Stateless JWT issued after successful OTP validation

### 👥 Role-Based Access
- `CUSTOMER`: Browse, order, manage cart, view own orders
- `SELLER`: Manage their own products, view & manage orders for their products
- Token-based route protection with role restrictions

### 📧 Email Integration
- Sends OTPs using **JavaMailSender**
- Works with Gmail SMTP or any SMTP provider

### 🛒 Product Management
- Sellers can create, update, and delete their products
- All products must belong to a category
- Product image URL, price, stock, and description support

### 🧺 Order Management
- Customers: Create orders after Stripe payment success
- Sellers: See only orders related to their products
- Order status tracking (e.g. pending, shipped, delivered)

### 💳 Stripe Integration
- Secure checkout via Stripe Checkout Session
- Frontend redirected to Stripe payment page
- Order recorded only after successful payment
- Supports itemized billing and redirect to success page

### 🧰 Category Management
- Admin-free category model: categories are predefined
- Products are always associated with a category
- Used for product filtering and UI organization

### 🔔 Real-Time Notifications (RabbitMQ)
- Event-driven system using Spring AMQP and RabbitMQ
- New order → Notifies relevant seller
- Order status updated → Notifies the relevant customer
- Easy to expand for future events (email/SMS/etc.)

---

## 📁 Project Structure

![image](https://github.com/user-attachments/assets/5857c8b3-4be1-4a4d-8699-f014c99ff117)




---

## ▶️ Getting Started

### 📦 Prerequisites

- Java 21
- Maven
- PostgreSQL
- RabbitMQ
- Stripe Account (Test Mode)
- Email SMTP (e.g., Gmail)

### 🔧 Environment Configuration

You can use either environment variables or an `application.properties` file. Here's what you need:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/bazaar
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JWT Secret
jwt.secret=your_jwt_secret

# Stripe
stripe.secret.key=your_stripe_secret_key
frontend.url=http://localhost:5173

# Email (JavaMailSender)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

### 🏃 Running the Project Locally

1. **Clone the repository:**

```bash
git clone https://github.com/OzzkanBulut/bazaar-backend.git
cd bazaar-backend
```

2. **Set up PostgreSQL & RabbitMQ**

- Create a `bazaar` database in PostgreSQL.
- Ensure RabbitMQ is running (locally or via Docker).

3. **Run the project:**

```bash
./mvnw spring-boot:run
```

Or build and run the JAR:

```bash
./mvnw clean package
java -jar target/bazaar-backend.jar
```

---

## 🔌 API Endpoints Overview

Here are some key endpoints:

### 🧾 Authentication (OTP)
- `POST /auth/sent/login-signup-otp"`  
Sends a one-time password to the user’s email.

```json
{
  "email": "example@mail.com",
  "otp": "123456",
  "role": "CUSTOMER" // or SELLER
}
```

### 👤 User & Product
- `GET users/profile` – Returns user info
- `POST /seller/products` – Add a new product (seller only)
- `GET /seller/products` – Get all products of a seller
- `PUT /seller/products/{productId}` – Update a product (seller only)
- `DELETE /seller/products/{productId}` – Delete a product

### 🛒 Cart & Order
- `POST /api/cart/add` – Add to cart
- `POST /api/orders` – Place order after successful payment
- `GET /api/orders/user` – Customer’s orders
- `GET /api/seller/orders` – Seller’s orders

---

## 🧪 Testing

- Use Postman or Swagger (if enabled) to test endpoints.
- JWT Bearer token required for most authenticated endpoints.
- Integration testing with JUnit & Mockito possible.

---

## 🌐 Deployment

- Hosted backend: **Render**
- Frontend: **Vercel**
- DB: **PostgreSQL (Render)**
- Queue: **RabbitMQ (CloudAMQP)**

---

## 📌 Future Plans

- Password login as an optional fallback
- Swagger documentation for all APIs
- Enhanced product media (multiple images, uploads)
- Email alerts for order status (in addition to RabbitMQ)

---

## 👨‍💻 Author

**Özkan – Full-Stack Java Developer**  
📍 Ankara, Turkey  
🔗 [LinkedIn](#) • [GitHub](#) • 📧 your_email@example.com
