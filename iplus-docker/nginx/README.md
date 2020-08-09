## Nginx Docker 使用

使用 docker pull nginx

#### 启动 MySql

```bash
# 获取 nginx 镜像
docker pull nginx

# 后台启动 nginx
docker run --name some-nginx -d -p 8080:80 nginx

# e.g. 
docker run --name nginx -d -p 8080:80 nginx
```

#### docker-compose

```yaml
web:
    image: nginx
    container_name: nginx
    volumes:
      - ../caiwei-react/build:/usr/share/nginx/html
    ports:
      - "80:80"
```
