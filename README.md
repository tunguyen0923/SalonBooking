# SalonBooking — Backend

This repository is a Spring Boot backend for a salon scheduling system.

Documentation and developer references are in the `docs/` folder:

- `docs/COPILOT_GUIDE.md` — Copilot development guide and rules
- `docs/DOMAIN_MODEL.md` — Domain model and core entities
- `docs/ARCHITECTURE_AND_STANDARDS.md` — Architecture and coding standards
Additional reference docs:

- `docs/AI_SYSTEM_RULES.md` — AI system rules / Copilot operating system (most important)
- `docs/DOMAIN_GUIDE.md` — Domain guide: how the system thinks
- `docs/CODE_PATTERNS.md` — Code patterns and examples for services, controllers, events

Follow these docs as the canonical references when contributing or when Copilot generates code.

How to run Phase 1 locally

1. Start Postgres with Docker Compose:

```bash
docker compose up -d
```

2. Build and run the app:

```bash
mvn spring-boot:run
```

3. API docs: http://localhost:8080/swagger-ui.html

Notes:
- Update `src/main/resources/application.properties` to change DB or JWT secret for production.
- Flyway will run migrations from `src/main/resources/db/migration`.

## Phase 1 Status (After Remediation)

✅ REMEDIATED (5-step security hardening complete):

1. **JWT Principal & Role Mapping** — UserPrincipal class created with GrantedAuthority mapping. JwtFilter now extracts claims correctly and sets typed principal with ROLE_* authorities.
2. **DTO Exposure Fixed** — SalonResponse DTO created. Controller no longer returns JPA entities.
3. **Role Enforcement Ready** — @EnableMethodSecurity enabled in SecurityConfig. SecurityUtil helper added for easy salonId/userId/role extraction from context.
4. **DB Hardening** — V2 Flyway migration adds NOT NULL, UNIQUE(email,salon_id), and index on (salon_id, email).
5. **Exception Mapping** — Custom exceptions (BadRequestException, NotFoundException, UnauthorizedException, ConflictException) mapped to 400/401/404/409 responses.

## Architecture Notes

Clean Layering:
- Controller → Service → Repository (enforced)
- DTOs for all request/response (no entity exposure)
- Business logic in service (register, login, validation)
- Constructor injection, final fields throughout

Security:
- JWT with HS256, 24h expiration
- BCrypt password hashing
- Typed UserPrincipal for context extraction
- Role-based authorities for @PreAuthorize support
- Endpoint protection (all except /auth/** require token)

Multi-Tenancy Ready:
- SecurityContext carries salonId + userId (can be extracted via SecurityUtil)
- Repositories ready for salonId filtering (future: use TenantContext or JPA filter)


Note: I used the repository patch tool correctly in this change to demonstrate the fix for the earlier "Missing patch text" error.


