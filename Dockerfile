# Stage 1: build WAR với Ant và JDK 17
FROM openjdk:17 AS build

# Cài đặt Ant
RUN apt-get update && apt-get install -y ant

WORKDIR /app
COPY . .
# Giả sử build file mặc định là build.xml
RUN ant war

# Stage 2: run với Tomcat 11 và JDK 17
FROM tomcat:11.0-jre17

# Xóa app mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR đã build vào ROOT
# Thử các đường dẫn phổ biến cho file WAR từ Ant
COPY --from=build /app/dist/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]