FROM liubaozhu/ubuntu

WORKDIR /home/zookeeper
RUN mkdir -p /home/zookeeper

ADD . .

RUN bash /home/zookeeper/build.sh

CMD [ "sh", "/home/zookeeper/start.sh" ]