## Contributing to SalonBooking

Thank you for contributing! This file explains how to get your development environment ready and the repository rules enforced by CI.

1) Follow the canonical docs
- The single source of truth for code generation and architecture is `docs/AI_SYSTEM_RULES.md`. All PRs and Copilot-generated code must comply with it.
- Also read `docs/ARCHITECTURE_AND_STANDARDS.md` and `docs/COPILOT_GUIDE.md`.

2) Branch and PR workflow
- Create a feature branch: `git checkout -b feat/short-description`
- Open a Pull Request to `main` (or `develop` if your team uses it).
- PR description should reference design docs and link any relevant issues.

3) Commit messages
- Use imperative, present-tense messages, e.g. `Add auth register endpoint`.

4) Code standards
- Keep business logic in `service` layer only.
- Do not call other module repositories directly — use events when cross-module communication is required.
- Use constructor injection (@RequiredArgsConstructor) and final fields in services.

5) Local development
- Start Postgres:
```bash
docker compose up -d
```
- Run the app:
```bash
mvn -DskipTests=true spring-boot:run
```

6) CI checks (what the automated workflow verifies)
- Project builds (`mvn -DskipTests package`)
- `docs/AI_SYSTEM_RULES.md` exists (this file is required for Copilot/automation compliance)

7) Pull request checklist
- [ ] PR targets the correct base branch
- [ ] All tests pass locally
- [ ] Code follows `docs/AI_SYSTEM_RULES.md`
- [ ] Add or update docs when introducing behavior or architectural changes

If you have questions about architecture or CI, open an issue or discuss in the PR.

