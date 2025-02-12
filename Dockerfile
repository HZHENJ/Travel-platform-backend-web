# 选择基础镜像，确保版本与本地 Java 版本一致
FROM openjdk:17
WORKDIR /app

# 复制 Spring Boot 打包的 JAR 文件到容器
COPY target/*.jar app.jar

# 容器暴露 8080 端口
EXPOSE 8080

# 运行 Spring Boot 应用
CMD ["java", "-jar", "app.jar"]
