# 🔐 AuthX – Secure JWT Authentication API

A secure and professional-grade backend authentication system built using **Spring Boot**, **Spring Security**, and **JWT (JSON Web Token)**. Designed to demonstrate best practices in stateless REST API development with token-based authentication and email verification.
---
# 🎯 Features
✅ User registration and login with validation  
✅ JWT-based stateless authentication  
✅ Secure profile access using token  
✅ Email verification with OTP  
✅ Reset password via OTP  
✅ Modular and clean project structure  
✅ MySQL integration  
✅ CORS enabled for frontend access
---
#🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT (jjwt)**
- **MySQL**
- **Maven**
- **Postman** (for testing)

 📦 Project Structure

```
src/
├── configurations/            # Security and JWT filter configs
├── controller/                # REST Controllers (Auth, Profile)
├── entity/                    # User entity
├── repository/                # Spring Data JPA repositories
├── services/                  # Business logic
├── userdto/                   # DTOs: Login, Register, AuthResponse, etc.
├── jwt/                       # JWT token utility classes
└── resources/                 # application.properties
```

---

## 🔗 API Endpoints

### 🔸 Authentication

#### ▶️ Register User
```http
POST /api/v1.0/auth/register
```

#### ▶️ Login User
```http
POST /login
```

#### ✅ Check Authentication
```http
GET /is-authenticated
```
Returns `true` if user is authenticated using JWT.

---

### 👤 Profile

#### 🔎 Get Profile (Protected)
```http
GET /api/v1.0/profile
```
**Headers**
| Key            | Value                  |
|----------------|------------------------|
| Authorization  | Bearer JWT_TOKEN_HERE  |

---

### 🔐 Email Verification & Password Reset

#### 📤 Send Email Verification OTP  
```http
POST /send-otp
```
**Headers**
| Key           | Value                 |
|---------------|-----------------------|
| Authorization | Bearer JWT_TOKEN_HERE |

---

#### ✅ Verify Email with OTP  
```http
POST /verify-email
```
**Headers**
| Key           | Value                 |
|---------------|-----------------------|
| Authorization | Bearer JWT_TOKEN_HERE |

**Body**
```json
{
  "otp": "123456"
}
```

---

#### 🔁 Send Reset Password OTP  
```http
POST /send-reset-otp?email=user@example.com
```

---

#### 🔒 Reset Password Using OTP  
```http
POST /reset-password
```
**Body**
```json
{
  "email": "user@example.com",
  "otp": "123456",
  "newPassword": "newSecurePassword"
}
```

---

## 📁 Configuration (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/authx_db
spring.datasource.username=root
spring.datasource.password=yourpassword

jwt.secret=your_jwt_secret
jwt.expiration=86400000
```

---

## 🧪 Postman Workflow

1. Register → `/auth/register`
2. Login → `/login` → copy `token`
3. Access `/profile` with header:
   ```http
   Authorization: Bearer <JWT_TOKEN>
   ```
4. Email Verification → `/send-otp`, `/verify-email`
5. Forgot Password → `/send-reset-otp`, `/reset-password`

---

## 📸 Screenshots



---

## 📌 TODO / Future Enhancements

- [ ] 🔄 Refresh token endpoint
- [ ] 🔓 Logout endpoint (client-side JWT destroy)
- [ ] 👮 Admin-specific role-based routes

---

## 🙋 Author

**Rustam** – Final Year B.Tech Student  
🔗 GitHub: [@RustamPoonia](https://github.com/RustamPoonia/AuthX.git)

---

## 📄 License

This project is released under the [MIT License](LICENSE). Feel free to use and contribute.

---

🟦🟩🟨🟧🟥🟪 **Thanks for checking out AuthX!**
