# springboot-starter

基于 **Spring Boot 4.1.0** + **JDK 26** 的 RESTful API 后端项目模板，采用分层架构，集成了 JPA、参数校验、全局异常处理、虚拟线程等现代化特性。

## 技术栈

| 技术            | 版本  | 说明                                  |
| --------------- | ----- | ------------------------------------- |
| Java            | 26    | 运行环境                              |
| Spring Boot     | 4.1.0 | 核心框架                              |
| Spring WebMVC   | —     | RESTful Web 服务                      |
| Spring Data JPA | —     | 数据库持久层                          |
| MySQL           | —     | 关系型数据库                          |
| Lombok          | —     | 简化代码注解                          |
| Spotless        | 3.9.0 | 代码格式化（Google Java Format AOSP） |
| Maven           | —     | 项目构建与依赖管理                    |

## 项目结构

```text
src/
├── main/java/com/company/springbootstarter/
│   ├── SpringbootStarterApplication.java  # 应用入口
│   ├── controller/
│   │   ├── HelloController.java           # 示例接口
│   │   └── UserController.java            # 用户 CRUD 接口
│   ├── dto/
│   │   ├── UserCreateRequest.java         # 创建用户请求体
│   │   ├── UserResponse.java              # 用户响应体
│   │   └── UserUpdateRequest.java         # 更新用户请求体
│   ├── entity/
│   │   └── User.java                      # 用户实体（JPA）
│   ├── exception/
│   │   ├── BusinessException.java         # 业务异常基类
│   │   ├── DuplicateResourceException.java # 资源冲突异常 (409)
│   │   ├── GlobalExceptionHandler.java    # 全局异常处理器
│   │   ├── ResourceNotFoundException.java # 资源未找到异常 (404)
│   │   └── UnauthorizedException.java     # 未授权异常 (401)
│   ├── repository/
│   │   └── UserRepository.java            # 用户数据访问层
│   └── service/
│       ├── UserService.java               # 用户服务接口
│       └── impl/
│           └── UserServiceImpl.java       # 用户服务实现
└── main/resources/
    └── application.properties             # 应用配置
```

## 快速开始

### 环境要求

- **JDK 26**
- **Maven 3.8+**
- **MySQL 8.0+**

### 数据库配置

创建数据库：

```sql
CREATE DATABASE springboot_db CHARACTER SET utf8mb4;
```

默认连接配置（可在 `application.properties` 中修改）：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springboot_db?characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
```

### 启动项目

```bash
# 编译并启动
./mvnw spring-boot:run

# 或先打包再运行
./mvnw clean package -DskipTests
java -jar target/springboot-starter-0.0.1-SNAPSHOT.jar
```

应用默认运行在 **8000** 端口。

## API 接口

### Hello 示例

| 方法 | 路径              | 说明                                |
| ---- | ----------------- | ----------------------------------- |
| GET  | `/hello?name=xxx` | 返回问候语，name 可选，默认 "World" |

### 用户管理

| 方法   | 路径              | 说明             |
| ------ | ----------------- | ---------------- |
| POST   | `/api/users`      | 创建用户         |
| GET    | `/api/users`      | 查询用户列表     |
| GET    | `/api/users/{id}` | 根据 ID 查询用户 |
| PUT    | `/api/users/{id}` | 更新用户         |
| DELETE | `/api/users/{id}` | 删除用户         |

#### 请求/响应示例

**创建用户：**

```bash
curl -X POST http://localhost:8000/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "email": "john@example.com"}'
```

**响应 (201 Created)：**

```json
{
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "username": "john",
    "email": "john@example.com"
}
```

**参数校验失败响应 (400) — RFC 7807 Problem Detail：**

```json
{
    "type": "https://api.example.com/errors/validation",
    "title": "Validation Error",
    "status": 400,
    "detail": "请求参数校验失败，请检查输入",
    "errorCode": "VALIDATION_FAILED",
    "fieldErrors": {
        "username": "用户名长度必须在 2-50 之间",
        "email": "邮箱格式不正确"
    }
}
```

## 核心特性

### 1. 分层架构

项目严格按照 **Controller → Service → Repository** 三层架构组织：

- **Controller** — 处理 HTTP 请求/响应，参数校验
- **Service** — 业务逻辑层，接口与实现分离
- **Repository** — 数据访问层，基于 Spring Data JPA

### 2. 统一异常处理

全局异常处理器遵循 **RFC 7807 (Problem Details for HTTP APIs)** 标准，所有错误响应格式统一：

| 异常类型                                  | HTTP 状态码 | 说明             |
| ----------------------------------------- | ----------- | ---------------- |
| `MethodArgumentNotValidException`         | 400         | 参数校验失败     |
| `HttpMessageNotReadableException`         | 400         | JSON 格式错误    |
| `MethodArgumentTypeMismatchException`     | 400         | 参数类型不匹配   |
| `MissingServletRequestParameterException` | 400         | 缺少必需参数     |
| `UnauthorizedException`                   | 401         | 未授权           |
| `AccessDeniedException`                   | 403         | 权限不足         |
| `ResourceNotFoundException`               | 404         | 资源未找到       |
| `NoResourceFoundException`                | 404         | 路径不存在       |
| `HttpRequestMethodNotSupportedException`  | 405         | HTTP 方法不支持  |
| `DuplicateResourceException`              | 409         | 资源冲突（重复） |
| `Exception`（兜底）                       | 500         | 服务器内部错误   |

### 3. 虚拟线程

通过 `spring.threads.virtual.enabled=true` 启用 Java 21+ 虚拟线程，提升高并发场景下的吞吐量。

### 4. 代码格式化

使用 **Spotless** 插件 + **Google Java Format (AOSP 风格)** 自动格式化代码，编译时自动检查：

```bash
# 手动格式化
./mvnw spotless:apply

# 检查格式
./mvnw spotless:check
```

### 5. 参数校验

DTO 使用 Jakarta Validation 注解进行声明式校验：

- `@NotBlank` — 字段不能为空
- `@Email` — 邮箱格式校验
- `@Size` — 字符串长度限制

### 6. Java Records

DTO 使用 Java Record 类型，代码简洁不可变：

```java
public record UserCreateRequest(
    @NotBlank @Size(min = 2, max = 50) String username,
    @NotBlank @Email String email
) {}
```

### 7. UUID 主键

实体使用 UUID 作为主键，由 Hibernate `@UuidGenerator` 自动生成，避免自增 ID 的安全和分布式问题。

## 常用命令

```bash
# 启动项目
./mvnw spring-boot:run

# 运行测试
./mvnw test

# 打包
./mvnw clean package -DskipTests

# 代码格式化
./mvnw spotless:apply

# 查看依赖树
./mvnw dependency:tree
```

## 参考文档

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/4.1.0/reference/)
- [Spring Web MVC](https://docs.spring.io/spring-boot/4.1.0/reference/web/servlet.html)
- [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.0/reference/data/sql.html)
- [RFC 7807 — Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc7807)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
