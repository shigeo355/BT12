# Sử dụng trực tiếp Tomcat với project web
FROM tomcat:11.0-jre17

# Xóa app mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy toàn bộ project vào webapps
COPY . /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]