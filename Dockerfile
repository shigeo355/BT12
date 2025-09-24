# Stage 1: build WAR với Ant và JDK 24
FROM ant:latest AS build
WORKDIR /app
COPY . .
# Giả sử build file mặc định là build.xml
RUN ant war

# Stage 2: run với Tomcat 11 và JDK 24
FROM tomcat:11.0-jdk24

# Xóa app mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR đã build vào ROOT
# Giả sử file WAR nằm trong thư mục dist/ hoặc build/
COPY --from=build /app/dist/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]