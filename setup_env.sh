#!/bin/bash

echo "🚀 开始安装 MySQL..."

# 更新系统并安装 MySQL 服务器
sudo apt update -y
sudo apt install mysql-server -y

# 启动 MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# 设置 root 账户密码
echo "🔐 配置 MySQL Root 用户密码..."
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';"
sudo mysql -e "FLUSH PRIVILEGES;"

# 创建数据库 travel
echo "🛠 创建 travel 数据库..."
sudo mysql -u root -p"123456" -e "CREATE DATABASE IF NOT EXISTS travel;"

# 允许远程访问
echo "🌍 允许远程访问 MySQL..."
sudo sed -i "s/bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/mysql.conf.d/mysqld.cnf
sudo systemctl restart mysql
sudo mysql -u root -p"123456" -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456'; FLUSH PRIVILEGES;"

# 开放 MySQL 端口
echo "🔓 开放 MySQL 端口 3306..."
sudo ufw allow 3306

echo "✅ MySQL 安装和配置完成！"
