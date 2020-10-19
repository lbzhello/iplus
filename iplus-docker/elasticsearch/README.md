## 使用
```$dockerfile
# docker run -d --name elasticsearch --net somenetwork -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:tag
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.8.1

## 挂载数据目录
 docker run -d --name es -p 9200:9200 -p 9300:9300 -v /d/elasticsearch/data:/usr/share/elasticsearch/data -e "discovery.type=single-node" elasticsearch:7.8.1
```

访问：[http://localhost:9200](http://localhost:9200)
