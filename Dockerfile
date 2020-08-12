FROM amazoncorretto:11

USER root

RUN yum update -y && \
    yum install -y unzip

RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
RUN unzip awscliv2.zip
RUN ./aws/install

VOLUME /data01

RUN mkdir -p /microservice/logs

COPY target/image-service*.jar /microservice/service.jar

EXPOSE 8080
ENTRYPOINT java $JVM_MEM_OPTS $JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar /microservice/service.jar