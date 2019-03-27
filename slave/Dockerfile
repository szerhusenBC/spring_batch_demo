FROM openjdk:11-oracle
MAINTAINER Stephan Zerhusen <stephan.zerhusen@gmail.com>

# needed for wait-for script
RUN ["yum", "-y", "install", "nc"]

# add wait-for script
ADD wait-for.sh /usr/share/wait-for
RUN ["chmod", "755", "/usr/share/wait-for"]

# Add the service itself
ARG JAR_FILE
COPY target/${JAR_FILE} /usr/share/app/spring_batch_demo_slave.jar

ENTRYPOINT ["/usr/share/wait-for", "demo-activemq:61616", "--timeout=300", "--", "java", "-jar", "/usr/share/app/spring_batch_demo_slave.jar", "--spring.profiles.active=docker"]
