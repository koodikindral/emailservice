FROM openjdk:8
COPY build/install/emailservice /emailservice
WORKDIR /emailservice
COPY config.yml config.yml
CMD ["bin/emailservice", "server", "config.yml"]