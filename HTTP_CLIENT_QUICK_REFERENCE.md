# IntelliJ HTTP Client - Quick Reference Card

## File: `requests.http` Location: Project Root (same directory as `pom.xml`)

---

## Quick Start (3 Steps)

### 1️⃣ Open File
Open `requests.http` in IntelliJ editor

### 2️⃣ Start Spring Boot
```bash
mvn spring-boot:run -DskipTests=true
```

### 3️⃣ Run Requests
Click **▶️ Run** button (left side of each request) or press **Cmd+Alt+R** (Mac) / **Ctrl+Alt+R** (Windows)

---

## Requests in File

| # | Name | Method | Endpoint | Purpose |
|---|------|--------|----------|---------|
| 1 | Register Salon | POST | `/auth/register` | Create new salon + owner |
| 2 | Login | POST | `/auth/login` | Get JWT token |
| 3 | Wrong Password | POST | `/auth/login` | Test 401 error |
| 4 | Duplicate Email | POST | `/auth/register` | Test 409 conflict |
| 5 | Register 2nd Salon | POST | `/auth/register` | Multi-tenant test |
| 6 | Login 2nd Salon | POST | `/auth/login` | Multi-tenant JWT |
| 7 | Swagger Docs | GET | `/swagger-ui.html` | API documentation |

---

## Execution Order

Run requests sequentially (top to bottom):

```
1. Register Salon
   ↓ (saves salonId)
2. Login
   ↓ (saves accessToken)
3. Wrong Password (test error)
4. Duplicate Email (test conflict)
5. Register 2nd Salon
   ↓ (saves salonId2)
6. Login 2nd Salon
   ↓ (saves accessToken2)
7. Check Swagger
```

---

## Variables Auto-Saved

| Variable | Auto-Set By | Used In |
|----------|-------------|---------|
| `salonId` | Request 1 Register | Request 2 Login |
| `accessToken` | Request 2 Login | Future protected endpoints |
| `salonId2` | Request 5 Register | Request 6 Login |
| `accessToken2` | Request 6 Login | Future multi-tenant tests |

---

## Keyboard Shortcuts

| Action | Mac | Windows |
|--------|-----|---------|
| Run current request | **Cmd+Alt+R** | **Ctrl+Alt+R** |
| Run in new tab | **Cmd+Shift+Alt+R** | **Ctrl+Shift+Alt+R** |
| Rerun last request | **Cmd+Alt+L** | **Ctrl+Alt+L** |
| Jump to response | **Tab** | **Tab** |
| Jump to headers | **Shift+Tab** | **Shift+Tab** |

---

## Expected Responses at a Glance

### ✅ Register (Request 1)
```
Status: 200 OK
Body: {id: 1, name: "Acme Nails", email: "...", phone: "...", createdAt: "..."}
```

### ✅ Login (Request 2)
```
Status: 200 OK
Body: {accessToken: "eyJ...", salonId: 1, role: "OWNER"}
```

### ❌ Wrong Password (Request 3)
```
Status: 401 Unauthorized
Body: {status: 401, message: "Invalid credentials", timestamp: ...}
```

### ❌ Duplicate Email (Request 4)
```
Status: 409 Conflict OR 400 Bad Request
Body: {status: 409/400, message: "...", timestamp: ...}
```

---

## Tips & Tricks

###  View Response Tab
After running request, click **Response** tab to see prettified JSON

###  View Raw Response
Click **Raw** to see exact response (useful for debugging)

###  Copy Token Value
1. Click on `accessToken` in response
2. Select and copy the value
3. Use in `Authorization: Bearer <PASTE_HERE>`

###  Test Assertions
Green checkmark ✅ = test passed
Red X ❌ = test failed

###  Run All Requests in Order
- Select from Request 1 to Request 7
- Right-click → **Run all in this file**

---

## After Testing: What You've Verified ✅

- [ ] Auth system works (register + login)
- [ ] JWT tokens are generated correctly
- [ ] Passwords are hashed (BCrypt)
- [ ] DTOs are used (no entity exposure)
- [ ] Error handling maps to correct HTTP codes
- [ ] Multi-tenant isolation works
- [ ] SwaggerUI/OpenAPI docs available
- [ ] Phase 1 API is production-ready

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| "Connection refused" | Ensure Spring Boot is running (check port 8080) |
| "Variable salonId not found" | Run Request 1 first to set variable |
| "400 Bad Request" | Check JSON syntax in request body |
| "401 Unauthorized" | Password mismatch - verify in request |
| "Variables not highlighted" | Variables use double curly braces: `{{variable}}` |
| Response tab is empty | Click "Response" tab, might be on "Raw" tab |

---

## Files in Project

```
SalonBooking/
├── requests.http                          ← YOU ARE HERE (HTTP requests)
├── INTELLIJ_HTTP_CLIENT_GUIDE.md         ← Detailed guide
├── API_VERIFICATION.md                    ← API contracts & flows
├── src/main/java/com/salon/
│   ├── auth/
│   │   ├── controller/AuthController.java
│   │   ├── service/AuthService.java
│   │   ├── dto/
│   │   │   ├── RegisterRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   └── LoginResponse.java
│   │   └── ...
│   └── salon/dto/SalonResponse.java
└── README.md
```

---

## Next Phase (Phase 2)

After verifying Phase 1 with these tests:
- Add protected endpoints (require JWT)
- Implement customer management
- Add appointment scheduling
- Implement notifications

Each will have similar `.http` test files.

---
