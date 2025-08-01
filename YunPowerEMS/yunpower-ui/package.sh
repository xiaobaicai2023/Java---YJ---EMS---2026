#!/bin/bash
# 定义参数
image_name="app-nginx"
container_name="app"
# 编译项目 yarn可替换为项目所用包管理器
yarn &&  yarn build
# 停止正在运行的容器并移除
docker stop ${container_name} && docker rm ${container_name}
# 以时间戳为tag
tag=${image_name}:$(date "+%Y%m%d%H%M%S")
# 编译docker镜像
docker build -t ${tag} .
# 启动容器
docker run -d --name ${container_name} --restart=always -p 800:80 ${tag}