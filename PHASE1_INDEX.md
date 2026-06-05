#  Phase 1 Testing & API Documentation - Complete Index

##  Quick Start (Read This First!)

You have **7 comprehensive guides** to test Phase 1 API endpoints using IntelliJ's built-in HTTP Client.

### ⚡ Fastest Path (5 minutes)
1. Open **`requests.http`** in IntelliJ
2. Start Spring Boot: `mvn spring-boot:run -DskipTests=true`
3. Click Run button (▶️) on each request
4. Review responses and test assertions

**That's it!** All 7 requests will test your API.

---

##  Files Created for You

### 1. **`requests.http`** ⭐ START HERE
- **What:** Ready-to-run HTTP requests for IntelliJ
- **Contains:** 7 pre-configured POST/GET requests
- **Features:** Auto-save variables, test assertions, multi-tenant tests
- **Use:** Open in IntelliJ, click Run button
- **Time to test:** ~5 minutes

### 2. **`TESTING_CHECKLIST.md`** ✅ FOLLOW THIS
- **What:** Step-by-step checklist for running all tests
- **Contains:** 60+ checkboxes to verify each aspect
- **Use:** Go through checklist while running requests
- **Verifies:** All DTOs, security, error handling, multi-tenancy

### 3. **`HTTP_CLIENT_QUICK_REFERENCE.md`**  QUICK LOOKUP
- **What:** One-page cheatsheet
- **Contains:** Keyboard shortcuts, expected responses, troubleshooting
- **Use:** Reference while testing (pin to sidebar)
- **Time:** 2-minute read

### 4. **`INTELLIJ_HTTP_CLIENT_GUIDE.md`**  DETAILED
- **What:** Complete tutorial with screenshots references
- **Contains:** Step-by-step setup, advanced features, conditionals, assertions
- **Use:** When you need deep dive or advanced features
- **Time:** 10-minute read

### 5. **`API_VERIFICATION.md`**  SPECIFICATION
- **What:** Official API contract documentation
- **Contains:** Request/response schemas, data flows, security checklist
- **Use:** Verify API follows spec, reference for other developers
- **Includes:** 2 endpoints fully documented

### 6. **`PHASE1_TESTING_SUMMARY.md`**  CONTEXT
- **What:** High-level overview of entire testing approach
- **Contains:** What you'll verify, expected results, JWT decoding guide
- **Use:** Understand the big picture
- **Time:** 5-minute read

### 7. **`INTELLIJ_HTTP_CLIENT_GUIDE.md`** (this file)
- **What:** Meta-documentation
- **Use:** Find which file answers your questions

---

## ️ File Selection Guide

**Choose the right file based on your need:**

| Need | File | Time |
|------|------|------|
| "Just run the tests" | `requests.http` | 5 min |
| "Show me every step" | `TESTING_CHECKLIST.md` | 20 min |
| "Quick lookup" | `HTTP_CLIENT_QUICK_REFERENCE.md` | 2 min |
| "How does this work?" | `INTELLIJ_HTTP_CLIENT_GUIDE.md` | 10 min |
| "What should the API look like?" | `API_VERIFICATION.md` | 5 min |
| "What am I testing?" | `PHASE1_TESTING_SUMMARY.md` | 5 min |
| "Where do I even start?" | **→ You are here** | now |

---

##  Execution Flow

```
1. Read this file
   ↓
2. Open requests.http in IntelliJ
   ↓
3. Start Spring Boot (mvn spring-boot:run)
   ↓
4. Follow TESTING_CHECKLIST.md
   ↓
5. For questions → HTTP_CLIENT_QUICK_REFERENCE.md
   ↓
6. For details → INTELLIJ_HTTP_CLIENT_GUIDE.md
   ↓
7. Verify results against API_VERIFICATION.md
```

---

## ✅ What Gets Tested (7 Requests)

| # | Endpoint | Method | Test Focus |
|---|----------|--------|-----------|
| 1 | `/auth/register` | POST | Create salon + owner, DTO response |
| 2 | `/auth/login` | POST | Generate JWT, multi-tenant claims |
| 3 | `/auth/login` | POST | Error handling (wrong password) |
| 4 | `/auth/register` | POST | Conflict handling (duplicate email) |
| 5 | `/auth/register` | POST | Multi-tenant isolation (2nd salon) |
| 6 | `/auth/login` | POST | Multi-tenant JWT (2nd salon) |
| 7 | `/swagger-ui.html` | GET | API documentation available |

### What You'll Verify

✅ Auth system works (register + login)
✅ JWT tokens generated correctly
✅ Passwords hashed with BCrypt
✅ DTOs used (no entity exposure)
✅ Error codes correct (400/401/409)
✅ Multi-tenant isolation works
✅ Test assertions pass
✅ Swagger docs available

---

##  10-Minute Quick Start

```
Step 1: Open requests.http
  └─ Right-click → Open With → IntelliJ Editor

Step 2: Start app
  └─ Terminal: mvn spring-boot:run -DskipTests=true

Step 3: Run requests in order (click ▶️)
  ├─ Request 1: Register
  ├─ Request 2: Login
  ├─ Request 3: Error test
  ├─ Request 4: Conflict test
  ├─ Request 5: 2nd Salon
  ├─ Request 6: 2nd Login
  └─ Request 7: Swagger

Step 4: Check results
  └─ All green checkmarks ✅ = Success!

Step 5: Review with checklist
  └─ Open TESTING_CHECKLIST.md
  └─ Compare actual vs expected
  └─ Check off boxes
```

**When done:** Mark this document with completion date.

---

##  Key Files & Paths

```
Project Root:
/Users/tunguyen/Desktop/Projects/SalonBooking/

Test Files (you are here):
├── requests.http                           ← HTTP requests
├── TESTING_CHECKLIST.md                   ← Checklist
├── HTTP_CLIENT_QUICK_REFERENCE.md         ← Shortcuts
├── INTELLIJ_HTTP_CLIENT_GUIDE.md          ← Full guide
├── API_VERIFICATION.md                    ← Spec
├── PHASE1_TESTING_SUMMARY.md              ← Overview
└── PHASE1_INDEX.md                        ← This file

Source Code:
└── src/main/java/com/salon/
    ├── auth/
    │   ├── controller/AuthController.java
    │   ├── service/AuthService.java
    │   └── dto/ (RegisterRequest, LoginRequest, LoginResponse)
    └── salon/dto/SalonResponse.java

Config:
├── pom.xml                                ← Dependencies
└── src/main/resources/
    └── application.properties              ← H2 config
```

---

##  Pro Tips

**Tip 1: Keyboard Shortcut**
- Instead of clicking Run button, press **Cmd+Alt+R** (Mac) / **Ctrl+Alt+R** (Windows)
- Faster workflow!

**Tip 2: Variable Inspection**
- After Request 1 (register), the `salonId` variable is auto-saved
- After Request 2 (login), the `accessToken` variable is auto-saved
- These are used in subsequent requests automatically

**Tip 3: Test Assertions**
- Each request has built-in tests
- Green checkmark = test passed
- Red X = test failed
- Scroll down to see all assertions

**Tip 4: JWT Decoding**
- Copy accessToken from Request 2 response
- Go to https://jwt.io
- Paste token to see decoded content (userId, salonId, role)

**Tip 5: Multi-Window**
- Open `requests.http` in one editor window
- Open `TESTING_CHECKLIST.md` in another
- Reference while testing

---

## ❓ FAQ

**Q: "Do I need to install anything?"**
A: No! IntelliJ has HTTP Client built-in. Just open `.http` file.

**Q: "Where do I put the token for protected endpoints?"**
A: Use `Authorization: Bearer {{accessToken}}` header. See `requests.http` for examples.

**Q: "Can I run all requests at once?"**
A: Yes! Select all requests, right-click, choose "Run all in file"

**Q: "What if a test fails?"**
A: Check the Response tab. Error message should tell you what's wrong. See Troubleshooting in guides.

**Q: "Do I need the Postman app?"**
A: No! IntelliJ HTTP Client is simpler and built-in.

---

##  When You Get Stuck

| Problem | Solution |
|---------|----------|
| "How do I open the file?" | See Step 1 in Quick Start |
| "App won't start" | Run: `mvn clean spring-boot:run` |
| "Connection refused" | Verify app started on port 8080 |
| "401 Unauthorized" | Check password matches (hint: copy from requests.http) |
| "Can't find variable" | Run earlier requests first (they set variables) |
| "Response is empty" | Click "Response" tab (not "Raw") |

---

## ✨ After Testing

When all tests pass:

**To Team:**
- [ ] Screenshot all green checkmarks
- [ ] Document time taken
- [ ] Share with team members

**For Next Phase:**
- [ ] Document any issues found
- [ ] Update requests.http with Phase 2 endpoints
- [ ] Plan customer management endpoints

**For Production:**
- [ ] Review REMEDIATION_SUMMARY.md (5-step security hardenin)
- [ ] Review CONTRIBUTING.md (dev guidelines)
- [ ] Review CI workflow in .github/workflows/ci.yml

---

##  Completion Status

| Item | Status | Notes |
|------|--------|-------|
| `requests.http` created | ✅ | 7 requests, auto-saves |
| `TESTING_CHECKLIST.md` created | ✅ | 60+ checkboxes |
| All guides written | ✅ | 4 comprehensive docs |
| Ready for testing | ✅ | Just start Spring Boot |

---

##  Learning Resources

Want to learn more about the architecture?

- **Architecture Details:** `docs/ARCHITECTURE_AND_STANDARDS.md`
- **Domain Model:** `docs/DOMAIN_MODEL.md`
- **AI System Rules:** `docs/AI_SYSTEM_RULES.md`
- **Security Hardening:** `REMEDIATION_SUMMARY.md`
- **Contributing Guide:** `CONTRIBUTING.md`

---

## ✅ You're Ready!

Everything is set up. Time to test!

**Next action:**
1. Open `requests.http` in IntelliJ
2. Start Spring Boot
3. Click Run on first request
4. Watch the tests pass! ✅

---

**Last Updated:** June 4, 2026
**Phase 1 Status:** ✅ COMPLETE & READY FOR TESTING
