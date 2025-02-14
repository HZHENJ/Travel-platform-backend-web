#!/bin/bash

echo "🔴 停止旧的 Spring Boot 进程..."
pkill -f 'java -jar' || true

echo "🚀 启动新版本..."
nohup java -jar ~/backend-web/backend.jar --spring.profiles.active=prod > ~/backend-web/backend.log 2>&1 &
echo "✅ 后端已启动！"

# 显示日志（可选）
tail -f ~/backend-web/backend.log
