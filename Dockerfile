# 使用 OpenJDK 官方镜像作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将构建的 JAR 文件复制到容器中
COPY target/*.jar app.jar

# 暴露 Spring Boot 默认端口（根据实际项目修改）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
