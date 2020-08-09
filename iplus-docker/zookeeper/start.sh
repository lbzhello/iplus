#!/bin/bash

# author lbzhello@qq.com
# create 2019-09-26
# zookeeper 后台启动脚本，持续在前台运行，不然会退出

/opt/zookeeper/bin/zkServer.sh start

while true; do echo hello world; sleep 1; done