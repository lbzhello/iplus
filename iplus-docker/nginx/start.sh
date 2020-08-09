#!/usr/bin/env bash

# author lbzhello@qq.com
# create 2019-10-11

# docker 启动 nginx 容器

docker run --name nginx -d -p 80:80 \
    -v ../caiwei-react/build:/usr/share/nginx/html \
    -v ./nginx/conf:/etc/nginx/conf.d \
    nginx

# 一行
# docker run --name nginx -d -p 80:80 -v ../caiwei-react/build:/usr/share/nginx/html -v ./nginx/conf:/etc/nginx/conf.d nginx