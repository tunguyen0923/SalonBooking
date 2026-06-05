# Phase 1 API Verification

## Endpoints Created

### 1. Register Salon (New Account)
- **Endpoint:** `POST /auth/register`
- **Port:** 8080
- **Full URL:** `http://localhost:8080/auth/register`
- **No Auth Required:** ✅ public endpoint

#### Request DTO: `RegisterRequest`
```json
{
  "salonName": "string",
  "salonEmail": "string",
  "salonPhone": "string",
  "ownerEmail": "string",
  "ownerPassword": "string"
}
```

**Fields:**
- `salonName` (required): Name of the salon business
- `salonEmail` (required): Email for salon notifications
- `salonPhone` (required): Phone for salon contact
- `ownerEmail` (required): Unique email for owner account
- `ownerPassword` (required): Password (will be BCrypt hashed)

#### Response DTO: `SalonResponse`
```json
{
  "id": 1,
  "name": "Acme Nails",
  "email": "salon@acme.com",
  "phone": "555-0100",
  "createdAt": "2026-06-04T03:30:00"
}
```

**Status:** HTTP 200 OK

---

### 2. Login
- **Endpoint:** `POST /auth/login`
- **Port:** 8080
- **Full URL:** `http://localhost:8080/auth/login`
- **No Auth Required:** ✅ public endpoint

#### Request DTO: `LoginRequest`
```json
{
  "salonId": 1,
  "email": "owner@acme.com",
  "password": "secret123"
}
```

**Fields:**
- `salonId` (required): Salon ID (obtained from register response)
- `email` (required): Owner email used during registration
- `password` (required): Owner password (plain text, matched against BCrypt hash)

#### Response DTO: `LoginResponse`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "salonId": 1,
  "role": "OWNER"
}
```

**Status:** HTTP 200 OK (or 401 Unauthorized if credentials invalid)

**JWT Token Usage:** Include in subsequent requests:
```
Authorization: Bearer <accessToken>
```

**JWT Claims (inside token):**
```json
{
  "userId": 1,
  "salonId": 1,
  "role": "OWNER",
  "iat": 1717468200,
  "exp": 1717554600
}
```

---

## API Contract Summary

| Method | Endpoint | Auth | Request | Response | Status |
|--------|----------|------|---------|----------|--------|
| POST | /auth/register | ✅ No | RegisterRequest | SalonResponse | 200 |
| POST | /auth/login | ✅ No | LoginRequest | LoginResponse | 200/401 |

---

## Data Flow

### Registration Flow
```
1. Client sends RegisterRequest to POST /auth/register
   ↓
2. AuthController.register() calls AuthService.register()
   ↓
3. AuthService creates Salon entity + Owner User entity
   ↓
4. BCrypt hashes password
   ↓
5. Entities saved to DB
   ↓
6. SalonResponse DTO returned (NO entity exposure)
   ↓
7. Client receives salon details + salon ID
```

### Login Flow
```
1. Client sends LoginRequest to POST /auth/login
   ↓
2. AuthController.login() calls AuthService.login()
   ↓
3. AuthService finds Salon by salonId
   ↓
4. AuthService finds User by email + salon
   ↓
5. Password matched against BCrypt hash
   ↓
6. JWT generated with claims (userId, salonId, role)
   ↓
7. LoginResponse returned (token + metadata)
   ↓
8. Client receives JWT for authenticated requests
```

---

## Security Features Implemented

✅ **Password Hashing:** BCrypt with strength 10
✅ **JWT Signing:** HS256 algorithm
✅ **JWT Expiry:** 24 hours
✅ **Multi-Tenancy:** salonId in JWT claims
✅ **Role Claims:** "OWNER" mapped to GrantedAuthority("ROLE_OWNER")
✅ **No Entity Exposure:** Response DTOs used, never JPA entities
✅ **Domain Exceptions:** BadRequestException, UnauthorizedException mapped to proper HTTP codes
✅ **Constructor Injection:** All beans use constructor DI

---

## Testing Validation Checklist

- [ ] RegisterRequest serializes correctly to JSON
- [ ] LoginRequest serializes correctly to JSON  
- [ ] SalonResponse deserializes correctly from JSON
- [ ] LoginResponse deserializes correctly from JSON
- [ ] POST /auth/register creates salon + owner user
- [ ] POST /auth/register returns SalonResponse (HTTP 200)
- [ ] POST /auth/login validates credentials
- [ ] POST /auth/login returns JWT token (HTTP 200)
- [ ] POST /auth/login returns 401 on invalid password
- [ ] JWT contains userId, salonId, role claims
- [ ] JWT is signed with HS256
- [ ] Password is hashed with BCrypt (not plain text)
- [ ] Subsequent requests can use JWT for authentication

---
