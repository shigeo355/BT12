# Stage 1: build WAR với Ant và JDK 17
FROM eclipse-temurin:17 AS build

# Cài đặt Ant
RUN apt-get update && apt-get install -y ant

WORKDIR /app
COPY . .

# Kiểm tra các target có sẵn trong build.xml
RUN ant -p

# Thử các target phổ biến
RUN ant compile || ant build || ant dist || ant package || ant all

# Stage 2: run với Tomcat 11 và JDK 17
FROM tomcat:11.0-jre17

# Xóa app mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR đã build vào ROOT (thử các đường dẫn khác nhau)
COPY --from=build /app/dist/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]