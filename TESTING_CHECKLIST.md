# ✅ Phase 1 Testing Checklist - IntelliJ HTTP Client

Complete this checklist to fully test Phase 1 API endpoints.

---

## Pre-Test Setup

- [ ] **Install IntelliJ IDEA** (Community or Ultimate edition)
- [ ] **Locate `requests.http` file** in project root (same directory as `pom.xml`)
- [ ] **Open `requests.http`** in IntelliJ editor
  - Right-click file → Open With → IntelliJ Editor
  - OR double-click to open

---

## Start Spring Boot Application

- [ ] **Terminal 1: Start App**
  ```bash
  cd /Users/tunguyen/Desktop/Projects/SalonBooking
  mvn spring-boot:run -DskipTests=true
  ```

- [ ] **Verify app started successfully**
  - Look for: `Started Application in X seconds`
  - Port: `8080`
  - Swagger available at: http://localhost:8080/swagger-ui.html

---

## Run HTTP Client Tests (7 Requests)

### Request 1: Register Salon
- [ ] Click **▶️ Run** button next to `POST {{host}}/auth/register`
  - Mac shortcut: **Cmd+Alt+R**
  - Windows: **Ctrl+Alt+R**
- [ ] **View Response** tab (bottom panel)
- [ ] **Verify Status Code:** `200 OK`
- [ ] **Verify Response Fields:**
  - [ ] `id` present (should be `1`)
  - [ ] `name`: "Acme Nails"
  - [ ] `email`: "acme@nails.com"
  - [ ] `phone`: "555-0123"
  - [ ] `createdAt`: timestamp
- [ ] **Check Test Results:**
  - [ ] All green checkmarks ✅
  - [ ] No red X marks ❌

### Request 2: Login
- [ ] Click **▶️ Run** next to `POST {{host}}/auth/login`
- [ ] **Verify Status Code:** `200 OK`
- [ ] **Verify Response Fields:**
  - [ ] `accessToken` present (long string starting with "eyJ...")
  - [ ] `salonId`: `1`
  - [ ] `role`: "OWNER"
- [ ] **Check Test Results:**
  - [ ] ✅ "Login Success - Status 200"
  - [ ] ✅ "Response has accessToken"
  - [ ] ✅ "Response has salonId"
  - [ ] ✅ "Response has role OWNER"
  - [ ] ✅ "JWT token is non-empty"

### Request 3: Wrong Password (Error Test)
- [ ] Click **▶️ Run** next to `POST {{host}}/auth/login` (wrong password)
- [ ] **Verify Status Code:** `401 Unauthorized`
- [ ] **Verify Error Response:**
  - [ ] `status`: `401`
  - [ ] `message`: Contains "Invalid credentials"
  - [ ] `timestamp`: present
- [ ] **Check Test Results:**
  - [ ] ✅ "Wrong Password Returns 401"

### Request 4: Duplicate Email (Conflict Test)
- [ ] Click **▶️ Run** next to duplicate registration
- [ ] **Verify Status Code:** `409 Conflict` OR `400 Bad Request`
- [ ] **Verify Error Response:**
  - [ ] `status`: `409` or `400`
  - [ ] `message`: present
- [ ] **Check Test Results:**
  - [ ] ✅ "Duplicate Email Returns 409 or 400"

### Request 5: Register Second Salon (Multi-Tenant Test)
- [ ] Click **▶️ Run** next to second register request
- [ ] **Verify Status Code:** `200 OK`
- [ ] **Verify Response:**
  - [ ] `id`: `2` (different from first salon)
  - [ ] `name`: "Beauty Plus"
- [ ] **Check Test Results:**
  - [ ] ✅ "Second Salon Registration Success"

### Request 6: Login Second Salon (Multi-Tenant JWT)
- [ ] Click **▶️ Run** next to second login
- [ ] **Verify Status Code:** `200 OK`
- [ ] **Verify Response:**
  - [ ] `salonId`: `2` (different tenant)
  - [ ] `accessToken`: present (different from first token)
  - [ ] `role`: "OWNER"
- [ ] **Check Test Results:**
  - [ ] ✅ "Second Salon Login Success"
  - [ ] ✅ "Multi-Tenant: Different salonId in JWT"

### Request 7: Check Swagger UI
- [ ] Click **▶️ Run** next to `GET {{host}}/swagger-ui.html`
- [ ] **Verify Status Code:** `200 OK`
- [ ] **Verify Response:** Contains HTML content
- [ ] **Check Test Results:**
  - [ ] ✅ "Swagger UI Available"

---

## Verify Test Assertions

After running all 7 requests, review the **Test Results** panel:

- [ ] **All tests passed** (green checkmarks ✅)
- [ ] **No tests failed** (no red X marks ❌)
- [ ] **Total assertions:** ~13 passing tests

---

## Verify DTOs (DTO Exposure Check)

- [ ] **Register Response (Request 1):**
  - [ ] Response is `SalonResponse` DTO (NOT raw JPA entity)
  - [ ] Only fields: `id`, `name`, `email`, `phone`, `createdAt`
  - [ ] No internal fields exposed (no `hibernateLazyInitializer`, etc.)

- [ ] **Login Response (Request 2):**
  - [ ] Response is `LoginResponse` DTO (NOT raw entity)
  - [ ] Contains: `accessToken`, `salonId`, `role`
  - [ ] Token is JWT (starts with "eyJ...")

---

## Verify Security Features

### BCrypt Password Hashing
- [ ] **Request 2 (successful login) passes**
  - Proves password matching works
  - Password was hashed in DB (not visible in API)

### JWT Token Details
- [ ] **Copy `accessToken` from Request 2 response**
- [ ] **Go to:** https://jwt.io
- [ ] **Paste token, verify payload contains:**
  - [ ] `userId`: `1`
  - [ ] `salonId`: `1`
  - [ ] `role`: "OWNER"
  - [ ] `iat`: issued timestamp
  - [ ] `exp`: expiration (24 hours from iat)

### Multi-Tenancy Isolation
- [ ] **Request 2 JWT has `salonId: 1`**
- [ ] **Request 6 JWT has `salonId: 2`**
- [ ] **Tokens are different** (compare accessToken values)

---

## Verify Error Handling

- [ ] **Request 3 returns 401** for wrong password
- [ ] **Request 4 returns 409/400** for duplicate email
- [ ] **All error responses have structure:**
  - [ ] `status` field (HTTP code)
  - [ ] `message` field (description)
  - [ ] `timestamp` field (when error occurred)

---

## Verify API Documentation

- [ ] **Request 7 returns 200**
- [ ] **Open in browser:** http://localhost:8080/swagger-ui.html
- [ ] **Swagger UI shows:**
  - [ ] `/auth/register` endpoint documented
  - [ ] `/auth/login` endpoint documented
  - [ ] Request/response schemas visible
  - [ ] Try it out! button available

---

## Post-Test Verification

- [ ] **All 7 requests executed successfully**
- [ ] **All assertions passed** (13+ tests)
- [ ] **No HTTP errors** (4xx, 5xx status codes only as expected)
- [ ] **DTOs properly used** (no entity exposure)
- [ ] **Security verified** (JWT, BCrypt, multi-tenancy)
- [ ] **Error handling works** (proper HTTP status codes)

---

## Phase 1 ✅ COMPLETE

If all checkboxes above are checked:

✅ **Auth system is production-ready**
✅ **JWT implementation verified**
✅ **Multi-tenant architecture working**
✅ **DTOs properly implemented**
✅ **Exception handling correct**
✅ **All endpoints tested and working**

---

## Next Steps

### If all tests pass:
- [ ] Move to **Phase 2: Customer Management**
- [ ] Add protected `/api/customers` endpoints
- [ ] Implement customer CRUD operations
- [ ] Add test requests for protected endpoints

### If tests fail:
- [ ] Check error messages in Response
- [ ] Review **TROUBLESHOOTING** section in guides
- [ ] Verify Spring Boot is still running
- [ ] Check port 8080 is available
- [ ] Make sure `requests.http` is latest version

---

## Support Resources

-  **`API_VERIFICATION.md`** — API contracts & schemas
-  **`HTTP_CLIENT_QUICK_REFERENCE.md`** — Keyboard shortcuts & tips
-  **`INTELLIJ_HTTP_CLIENT_GUIDE.md`** — Full step-by-step guide
-  **`PHASE1_TESTING_SUMMARY.md`** — Detailed overview
-  **`requests.http`** — The actual test requests (this file)

---

## Completion Timestamp

When complete, record the date/time:

**Date:** _________________

**Time:** _________________

**All tests passed:** ☐ Yes ☐ No

**Notes:**

_________________________________________________________________

_________________________________________________________________

---
