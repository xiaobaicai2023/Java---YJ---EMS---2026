FROM nginx:latest

# 将项目根目录下dist文件夹下的所有文件复制到镜像中 /usr/share/nginx/html/ 目录下
COPY dist/ /usr/share/nginx/html/
# default.conf 为nginx默认配置文件
COPY default.conf /etc/nginx/conf.d/default.conf