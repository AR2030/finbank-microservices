# Contributing to FinBank Microservices

First off, thank you for considering contributing to FinBank! It's people like you that make FinBank such a great platform.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Pull Request Process](#pull-request-process)
- [Feature Implementation Guide](#feature-implementation-guide)

## 🤝 Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code:

- Be respectful and inclusive
- Welcome newcomers
- Focus on what is best for the community
- Show empathy towards others
- Accept constructive criticism gracefully

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have:
- Java 21 or higher
- Maven 3.8+
- Docker and Docker Compose
- Git
- Your favorite IDE (IntelliJ IDEA recommended)

### First-Time Setup

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/finbank-microservices.git
   cd finbank-microservices
   ```

3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/AR2030/finbank-microservices.git
   ```

4. **Start the development environment**:
   ```bash
   docker-compose -f docker-compose/default/docker-compose.yml up -d
   ```

5. **Build the project**:
   ```bash
   mvn clean install
   ```

6. **Verify everything works**:
   - Eureka: http://localhost:8070
   - Config Server: http://localhost:8071/actuator/health
   - Gateway: http://localhost:8072/actuator/health

## 💡 How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title** describing the issue
- **Steps to reproduce** the behavior
- **Expected behavior**
- **Actual behavior**
- **Screenshots** if applicable
- **Environment details**: OS, Java version, Docker version

**Bug Report Template**:
```markdown
### Description
[Clear description of the bug]

### Steps to Reproduce
1. Start service X
2. Call API endpoint Y with Z
3. See error

### Expected Behavior
[What should happen]

### Actual Behavior
[What actually happens]

### Environment
- OS: [e.g., Ubuntu 22.04]
- Java: [e.g., OpenJDK 21]
- Docker: [e.g., 24.0.0]

### Logs
```
[Paste relevant logs here]
```
```

### Suggesting Features

Check our [Feature Suggestions](./FEATURE_SUGGESTIONS.md) document first. If your feature isn't listed:

1. **Create an issue** with the `enhancement` label
2. **Describe the feature** and its benefits
3. **Explain the use case**
4. **Provide examples** if possible

**Feature Request Template**:
```markdown
### Feature Description
[Clear description of the feature]

### Problem It Solves
[What problem does this solve?]

### Proposed Solution
[How would you implement it?]

### Alternatives Considered
[What other solutions did you consider?]

### Additional Context
[Any other information]
```

### Implementing Features

Want to implement a feature? Great! Here's how:

1. **Check the roadmap**: See [ROADMAP.md](./ROADMAP.md) for planned features
2. **Start with Quick Wins**: See [QUICK_WINS.md](./QUICK_WINS.md) for beginner-friendly tasks
3. **Claim an issue**: Comment on the issue saying you want to work on it
4. **Create a branch**: `git checkout -b feature/your-feature-name`
5. **Implement**: Follow our [coding standards](#coding-standards)
6. **Test**: Write tests for your changes
7. **Submit PR**: Follow the [PR process](#pull-request-process)

## 🛠️ Development Setup

### Running Services Locally

For development, you might want to run services individually:

```bash
# Start infrastructure (databases, RabbitMQ, Redis)
docker-compose -f docker-compose/default/docker-compose.yml up -d mysql-accounts mysql-loans mysql-cards rabbitmq redis keycloak

# Run Config Server
cd configserver
./mvnw spring-boot:run

# Run Eureka Server
cd eurekaserver
./mvnw spring-boot:run

# Run a business service (e.g., Accounts)
cd accounts
./mvnw spring-boot:run

# Run Gateway
cd gatewayserver
./mvnw spring-boot:run
```

### Development Tools

#### Hot Reload

Add Spring Boot DevTools for automatic restarts:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

#### Database GUI

Use MySQL Workbench or DBeaver to connect to local databases:
- Accounts DB: `localhost:3306/accountsdb`
- Loans DB: `localhost:3307/loansdb`
- Cards DB: `localhost:3308/cardsdb`
- Credentials: `root/root`

#### Message Queue GUI

RabbitMQ Management UI: http://localhost:15672 (guest/guest)

## 📝 Coding Standards

### Java Code Style

Follow these conventions:

#### Naming Conventions
```java
// Classes: PascalCase
public class AccountsServiceImpl { }

// Interfaces: PascalCase with 'I' prefix or descriptive name
public interface IAccountsService { }
public interface AccountsService { }

// Methods: camelCase, verb-based
public void createAccount() { }
public AccountDto fetchAccountDetails() { }

// Variables: camelCase
String mobileNumber;
AccountDto accountDto;

// Constants: UPPER_SNAKE_CASE
public static final String DEFAULT_ACCOUNT_TYPE = "Savings";
```

#### Package Structure
```
com.ar2030.finbank.{service}
├── controller       // REST controllers
├── service          // Business logic interfaces
│   └── impl         // Business logic implementations
├── repository       // Data access layer
├── entity           // JPA entities
├── dto              // Data transfer objects
├── mapper           // Entity-DTO mappers
├── exception        // Custom exceptions
├── constants        // Constants
└── config           // Configuration classes
```

#### Documentation
```java
/**
 * Service implementation for managing customer accounts.
 * 
 * @author AR2030
 * @version 1.0
 * @since 2024
 */
@Service
public class AccountsServiceImpl implements IAccountsService {
    
    /**
     * Creates a new account for a customer.
     * 
     * @param customerDto the customer details
     * @throws CustomerAlreadyExistsException if customer already exists
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        // implementation
    }
}
```

### REST API Guidelines

#### Endpoint Naming
```java
// Use nouns, not verbs
@GetMapping("/api/accounts")           // ✅ Good
@GetMapping("/api/getAccounts")        // ❌ Bad

// Use plural for collections
@GetMapping("/api/accounts")           // ✅ Good
@GetMapping("/api/account")            // ❌ Bad

// Use hierarchical structure
@GetMapping("/api/accounts/{id}/transactions")  // ✅ Good
```

#### HTTP Methods
```java
@PostMapping("/api/create")      // Create
@GetMapping("/api/fetch")        // Read
@PutMapping("/api/update")       // Update
@DeleteMapping("/api/delete")    // Delete
```

#### Response Codes
```java
return ResponseEntity.status(HttpStatus.CREATED).body(response);      // 201 Created
return ResponseEntity.ok(response);                                    // 200 OK
return ResponseEntity.noContent().build();                            // 204 No Content
return ResponseEntity.notFound().build();                             // 404 Not Found
return ResponseEntity.badRequest().body(error);                       // 400 Bad Request
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // 500 Error
```

### Testing Standards

#### Test Structure
```java
@SpringBootTest
class AccountsServiceImplTest {
    
    @Autowired
    private IAccountsService accountsService;
    
    @MockBean
    private AccountsRepository accountsRepository;
    
    @Test
    @DisplayName("Should create account successfully")
    void testCreateAccount_Success() {
        // Given
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setMobileNumber("1234567890");
        
        // When
        accountsService.createAccount(customerDto);
        
        // Then
        verify(accountsRepository, times(1)).save(any(Accounts.class));
    }
    
    @Test
    @DisplayName("Should throw exception when customer already exists")
    void testCreateAccount_CustomerExists() {
        // Given
        CustomerDto customerDto = new CustomerDto();
        customerDto.setMobileNumber("1234567890");
        
        when(accountsRepository.findByMobileNumber(anyString()))
            .thenReturn(Optional.of(new Accounts()));
        
        // When & Then
        assertThrows(CustomerAlreadyExistsException.class, 
            () -> accountsService.createAccount(customerDto));
    }
}
```

#### Test Coverage
- **Minimum**: 70% code coverage
- **Target**: 80% code coverage
- **Critical paths**: 100% coverage

Run coverage:
```bash
mvn clean test jacoco:report
# View report at target/site/jacoco/index.html
```

### Git Commit Messages

Follow the Conventional Commits specification:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Examples**:
```
feat(accounts): add API versioning support

Implement versioning strategy for accounts API to support
backward compatibility.

- Add v1 prefix to all endpoints
- Update OpenAPI documentation
- Update tests

Closes #123
```

```
fix(loans): resolve null pointer in loan calculation

Fix NPE when calculating interest for loans with null
outstanding amount.

Fixes #456
```

## 🔄 Pull Request Process

### Before Submitting

1. **Update from upstream**:
   ```bash
   git checkout main
   git fetch upstream
   git merge upstream/main
   ```

2. **Run all tests**:
   ```bash
   mvn clean test
   ```

3. **Check code quality**:
   ```bash
   mvn checkstyle:check
   mvn pmd:check
   ```

4. **Update documentation** if needed

### Submitting PR

1. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Create PR** on GitHub with:
   - **Clear title**: `feat(accounts): add API versioning`
   - **Description**: What, why, and how
   - **Related issues**: `Closes #123`
   - **Screenshots**: For UI changes
   - **Testing notes**: How to test

3. **PR Template**:
   ```markdown
   ## Description
   [Clear description of changes]

   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Breaking change
   - [ ] Documentation update

   ## Related Issues
   Closes #123

   ## How Has This Been Tested?
   - [ ] Unit tests
   - [ ] Integration tests
   - [ ] Manual testing

   ## Checklist
   - [ ] Code follows project style guidelines
   - [ ] Self-review completed
   - [ ] Comments added for complex code
   - [ ] Documentation updated
   - [ ] Tests added/updated
   - [ ] All tests passing
   - [ ] No new warnings
   ```

### PR Review Process

1. **Automated checks** must pass:
   - Build
   - Tests
   - Code quality
   - Security scan

2. **Code review** by maintainer:
   - Code quality
   - Test coverage
   - Documentation
   - Design patterns

3. **Address feedback**:
   - Make requested changes
   - Push updates to same branch
   - Respond to comments

4. **Merge**:
   - Squash and merge (default)
   - Delete branch after merge

## 🎯 Feature Implementation Guide

### Example: Adding API Versioning

This is a **Quick Win** feature. Let's walk through implementation:

#### 1. Create Feature Branch
```bash
git checkout -b feature/api-versioning
```

#### 2. Update Controllers
```java
// Before
@RestController
@RequestMapping("/api")
public class AccountsController { }

// After
@RestController
@RequestMapping("/v1/api")
public class AccountsController { }
```

#### 3. Update Tests
```java
// Update test URLs
mockMvc.perform(get("/v1/api/fetch?mobileNumber=1234567890"))
    .andExpect(status().isOk());
```

#### 4. Update Gateway Routes
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: accounts
          uri: lb://ACCOUNTS
          predicates:
            - Path=/v1/accounts/**
```

#### 5. Update OpenAPI Config
```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("FinBank Accounts API")
            .version("1.0"))
        .servers(Arrays.asList(
            new Server().url("http://localhost:8072/v1")
        ));
}
```

#### 6. Update Documentation
- Update README with new endpoints
- Update Postman collection
- Add versioning documentation

#### 7. Run Tests
```bash
mvn clean test
```

#### 8. Commit Changes
```bash
git add .
git commit -m "feat(all): implement API versioning strategy

Add v1 prefix to all API endpoints for future compatibility.

- Update all controllers with /v1 prefix
- Update gateway routes
- Update OpenAPI documentation
- Update tests and Postman collection

Implements #123"
```

#### 9. Push and Create PR
```bash
git push origin feature/api-versioning
```

## 🏗️ Architecture Guidelines

### Microservice Boundaries

When adding new services, ensure:
- **Single Responsibility**: One business capability
- **Autonomous**: Independent deployment
- **Isolated Data**: Own database
- **API Contract**: Well-defined interfaces
- **Resilient**: Fault tolerance

### Communication Patterns

**Synchronous** (OpenFeign):
```java
@FeignClient(name = "loans", fallback = LoansFallback.class)
public interface LoansFeignClient {
    @GetMapping("/api/fetch")
    LoansDto fetchLoanDetails(@RequestParam String mobileNumber);
}
```

**Asynchronous** (RabbitMQ):
```java
@Bean
public Supplier<AccountEvent> sendAccountEvent() {
    return () -> new AccountEvent(accountNumber, "CREATED");
}
```

### Configuration Management

Use Config Server for:
- Database URLs
- Message broker settings
- Feature flags
- Environment-specific configs

Don't hardcode:
- Credentials (use environment variables)
- URLs
- Port numbers

## 🔒 Security Guidelines

### Secure Coding

1. **Input Validation**:
   ```java
   @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
   private String mobileNumber;
   ```

2. **SQL Injection Prevention**:
   ```java
   // ✅ Good - Using Spring Data JPA
   accountsRepository.findByMobileNumber(mobileNumber);
   
   // ❌ Bad - Raw SQL
   String sql = "SELECT * FROM accounts WHERE mobile = '" + mobile + "'";
   ```

3. **Authentication**:
   - All APIs behind OAuth2
   - Use JWT tokens
   - Never expose internal services directly

4. **Secrets Management**:
   ```yaml
   # ❌ Bad
   spring.datasource.password=root
   
   # ✅ Good
   spring.datasource.password=${DB_PASSWORD}
   ```

## 📚 Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Microservices Patterns](https://microservices.io/)
- [Feature Suggestions](./FEATURE_SUGGESTIONS.md)
- [Implementation Roadmap](./ROADMAP.md)
- [Quick Wins Guide](./QUICK_WINS.md)

## ❓ Questions?

- **Discord**: [Join our Discord](#) (coming soon)
- **GitHub Discussions**: Use the Discussions tab
- **Email**: support@finbank.com

## 🙏 Thank You!

Your contributions make FinBank better for everyone. We appreciate your time and effort!

---

**Happy Coding! 🚀**
