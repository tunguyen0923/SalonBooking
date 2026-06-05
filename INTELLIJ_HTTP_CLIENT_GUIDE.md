### Phase 1 API Testing Guide - IntelliJ HTTP Client

## Prerequisites
1. IntelliJ IDEA (Community or Ultimate)
2. Spring Boot app running on `http://localhost:8080`
3. `.http` or `.rest` file support (built-in to IntelliJ)

---

## Step-by-Step Setup

### Step 1: Create HTTP Request File
1. In IntelliJ, right-click on your project root folder
2. Select **New → File**
3. Name it: `requests.http` (exact name, with `.http` extension)
4. Location: Project root (same level as `pom.xml`)

### Step 2: Add HTTP Requests to File

Copy the content from `Phase1_API_Requests.http` file in this project.

### Step 3: Start Spring Boot App
Terminal:
```bash
cd /Users/tunguyen/Desktop/Projects/SalonBooking
mvn spring-boot:run -DskipTests=true
```

Wait for: `Started Application in X seconds`

### Step 4: Run Requests in IntelliJ

1. **Open `requests.http` file in editor**
2. **For Each Request:**
   - Hover over the green play button (▶) on the left side of request
   - Click to execute
   - Or use keyboard: **Ctrl+Alt+R** (Windows/Linux) or **Cmd+Alt+R** (Mac)

### Step 5: View Response

- Response appears in the **HTTP Client** tab at bottom
- Shows HTTP status code, headers, and JSON body
- Can switch between **Response**, **Headers**, **Cookies** tabs

---

## Request Execution Order

Execute in this order for successful testing:

1. **POST /auth/register** — Creates salon + owner
   - Response: SalonResponse with `id: 1`
   
2. **POST /auth/login** — Login with owner credentials
   - Response: LoginResponse with JWT token
   - Copy the `accessToken` value
   
3. (Optional) **GET /api/protected** — Test authenticated request
   - Paste token into `Authorization: Bearer <TOKEN>` header

---

## Save Response Values for Reuse

After running Register request:
1. Copy `id` from response (should be `1`)
2. In Login request, `salonId` should already be `1`

After running Login request:
1. Copy full `accessToken` value
2. In other requests, replace `{{accessToken}}` variable with copied value

Or use IntelliJ's **Response Handler Scripts** to auto-save:
- See `Phase1_API_Requests.http` for examples

---

## Keyboard Shortcuts

| Action | Mac | Windows/Linux |
|--------|-----|---------------|
| Run Request | Cmd+Alt+R | Ctrl+Alt+R |
| Run Request in New Tab | Cmd+Shift+Alt+R | Ctrl+Shift+Alt+R |
| Rerun Last Request | Cmd+Alt+L | Ctrl+Alt+L |
| View Response Body | Tab 1 | Tab 1 |
| View Headers | Tab 2 | Tab 2 |

---

## Troubleshooting

**"Connection refused"**
- Ensure Spring Boot is running on port 8080
- Check: `http://localhost:8080/swagger-ui.html` loads

**"400 Bad Request"**
- Check JSON format in request body
- Ensure all required fields are present and correct

**"401 Unauthorized"**
- Password mismatch in login
- Check credentials match what was registered

**"Variable not found" error**
- Make sure you've run Register request first
- Check variable name matches: `{{salonId}}`

---

## Example: Full Test Scenario

### 1. Register (Create new salon)
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "salonName": "Acme Nails",
  "salonEmail": "acme@nails.com",
  "salonPhone": "555-0123",
  "ownerEmail": "owner@acme.com",
  "ownerPassword": "SecurePass123!"
}
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "name": "Acme Nails",
  "email": "acme@nails.com",
  "phone": "555-0123",
  "createdAt": "2026-06-04T..."
}
```

### 2. Login (Get JWT token)
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "salonId": 1,
  "email": "owner@acme.com",
  "password": "SecurePass123!"
}
```

**Expected Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInNhbG9uSWQiOjEsInJvbGUiOiJPV05FUiIsImlhdCI6MTcxNzQ2ODIwMCwiZXhwIjoxNzE3NTU0NjAwfQ.xxx",
  "salonId": 1,
  "role": "OWNER"
}
```

### 3. Use JWT in Protected Request
```http
GET http://localhost:8080/api/some-protected-endpoint
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## IntelliJ HTTP Client Features

### Variables (Auto-save from responses)
```http
> {% 
  client.global.set("accessToken", response.body.accessToken); 
  client.global.set("salonId", response.body.salonId); 
%}
```

### Conditional Requests
```http
### Only run if token exists
{% if client.global.has("accessToken") %}
GET http://localhost:8080/api/protected
Authorization: Bearer {{accessToken}}
{% else %}
// Token not found - run login first
{% endif %}
```

### Test Assertions
```http
> {%
  client.test("Status is 200", function() {
    client.assert(response.status === 200, "Expected status 200");
  });
  
  client.test("Response has accessToken", function() {
    client.assert(response.body.hasOwnProperty("accessToken"), "Missing accessToken");
  });
%}
```

---

## Next Steps (After Testing)

✅ Verify all endpoints work correctly
✅ Check JWT token contains: userId, salonId, role
✅ Verify password is hashed (never plain text in DB)
✅ Check multi-tenant isolation (salonId enforcement)

---
