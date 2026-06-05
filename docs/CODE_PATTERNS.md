# Code Patterns — Spring Boot Standards

## 1. Service Pattern

All services follow this structure:

```text
@Service
@RequiredArgsConstructor
@Transactional
public class ExampleService {

    private final ExampleRepository repository;
    private final ApplicationEventPublisher publisher;

    public Example create(CreateExampleRequest request) {
        validate(request);
        Example entity = build(request);

        Example saved = repository.save(entity);

        publisher.publishEvent(new ExampleCreatedEvent(saved.getId()));

        return saved;
    }
}
```

## 2. Controller Pattern

```text
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/examples")
public class ExampleController {

    private final ExampleService service;

    @PostMapping
    public ResponseEntity<ExampleResponse> create(@RequestBody ExampleRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
}
```

## 3. Event Pattern

Event class:

```text
public class ExampleCreatedEvent {
    private final Long id;
}
```

Listener:

```text
@EventListener
public void handle(ExampleCreatedEvent event) {
    // side effects only (email, logging, etc.)
}
```

## 4. Repository Pattern

```text
public interface ExampleRepository extends JpaRepository<Example, Long> {
}
```

## 5. Validation Pattern

All validation MUST be inside service layer:

```text
if (conflictsExist) {
    throw new ConflictException();
}
```

## 6. DTO Pattern

NEVER expose entities.

Request:

CreateExampleRequest

Response:

ExampleResponse

Mapping:

MapStruct preferred or manual mapper class


