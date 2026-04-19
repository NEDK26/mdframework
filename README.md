# mdframework

个人 Spring Boot 单体项目脚手架，封装了常用基础设施，新项目直接从 Maven 原型生成，跳过重复搭建工作。

## 技术栈

| 层 | 技术 |
|---|---|
| 框架 | Spring Boot 4.0.5 / Java 21 |
| 数据库 | MySQL + MyBatis-Plus 3.5.9 |
| 缓存 | Redis（JSON 序列化） |
| 消息队列 | RabbitMQ |
| 接口文档 | SpringDoc OpenAPI（Swagger UI） |

## 内置能力

- **统一响应**：`CommonResult<T>` 封装所有接口返回值，错误码定义在 `ResultCode` 枚举
- **全局异常**：`GlobalExceptionHandler` 三层拦截（业务异常 / 参数校验 / 兜底异常）
- **Token 鉴权**：基于 Redis 的无侵入式登录拦截，滑动过期，支持白名单配置
- **请求上下文**：`UserContext`（ThreadLocal）在全链路传递当前用户 ID
- **自动填充**：`BaseEntity` + `MetaObjectHandler` 自动写入 `createTime` / `updateTime` / `deleted`
- **逻辑删除**：`@TableLogic` 开箱即用，查询自动过滤已删除记录
- **分页插件**：MyBatis-Plus `PaginationInnerInterceptor` 已注册
- **多环境配置**：`dev`（本地默认）/ `prod`（环境变量注入，不存明文密码）

## 项目结构

```
src/main/java/com/nedk/example/
├── common/
│   ├── config/         # RedisConfig / MybatisPlusConfig / WebMvcConfig（跨域+拦截器）
│   ├── constant/       # SystemConstants（分页默认值、Token 常量等）
│   ├── context/        # UserContext（ThreadLocal 存当前用户 ID）
│   ├── exception/      # BusinessException / GlobalExceptionHandler
│   ├── handler/        # MetaObjectHandler（MP 自动填充）
│   ├── interceptor/    # LoginInterceptor（Token 校验）
│   ├── result/         # CommonResult / ResultCode / IResultCode
│   └── utils/          # RedisUtil / TokenUtil
├── controller/         # （空，业务层）
├── mapper/             # （空，数据访问层）
├── model/
│   ├── dto/            # UserLoginDTO（示例）
│   ├── entity/         # BaseEntity / User（示例）
│   └── vo/             # （空，响应对象）
└── service/            # （空，业务逻辑层）
```

## 使用方式

### 从 Maven 原型新建项目（推荐）

**第一步：安装原型到本地仓库**

```bash
# 在本项目根目录执行
./mvnw archetype:create-from-project -Darchetype.properties=archetype.properties

# 进入生成的原型目录并安装
cd target/generated-sources/archetype
mvn install
```

**第二步：从原型生成新项目**

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.nedk.framework \
  -DarchetypeArtifactId=nedk-framework-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.yourcompany \
  -DartifactId=yourproject \
  -Dpackage=com.yourcompany.yourproject \
  -DinteractiveMode=false
```

### 直接克隆本项目使用

```bash
git clone <repo-url>
# 修改 pom.xml 中的 groupId / artifactId
# 修改 ExampleApplication.java 的类名和包名
```

## 本地开发

**前置要求：** MySQL、Redis、RabbitMQ 在本地运行（默认端口）

```bash
# 直接启动（dev profile，默认端口 8080，数据库名 dev_db）
./mvnw spring-boot:run

# 指定数据库名
./mvnw spring-boot:run -Dspring-boot.run.arguments="--dbName=your_db"

# 打包
./mvnw clean package -DskipTests

# 生产环境启动
java -jar target/yourproject.jar \
  --spring.profiles.active=prod \
  --DB_HOST=xxx --DB_NAME=xxx --DB_USERNAME=xxx --DB_PASSWORD=xxx \
  --REDIS_HOST=xxx --REDIS_PASSWORD=xxx \
  --RABBITMQ_HOST=xxx --RABBITMQ_USERNAME=xxx --RABBITMQ_PASSWORD=xxx
```

接口文档地址：`http://localhost:8080/swagger-ui.html`

## 鉴权说明

白名单路径（无需 Token）：`/user/login`、`/user/register`、Swagger 相关路径。

其余所有接口需在请求头携带：
```
Authorization: Bearer {token}
```

Token 通过 `TokenUtil.generateToken(userId)` 生成，有效期 7 天，每次请求后自动续期。

在 Service 层获取当前登录用户：
```java
Long userId = UserContext.get();
```

退出登录：
```java
tokenUtil.removeToken(token);
```

## 更新原型

修改源代码后，重新执行安装步骤（第一步）即可覆盖本地仓库中的旧版本。
