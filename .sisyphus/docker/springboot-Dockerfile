# 后端 Dockerfile
# 多阶段构建，减小镜像体积

# 阶段 1: 构建
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# 复制 Maven 配置文件
COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn

# 下载依赖（利用 Docker 缓存）
RUN ./mvnw dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN ./mvnw clean package -DskipTests -B

# 阶段 2: 运行
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 创建非 root 用户
RUN addgroup -S spring && adduser -S spring -G spring

# 复制构建产物
COPY --from=builder /app/target/*.jar app.jar

# 创建文件上传目录
RUN mkdir -p /app/files && chown -R spring:spring /app

USER spring:spring

# 暴露端口
EXPOSE 9090

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
