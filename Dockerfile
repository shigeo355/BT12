FROM tomcat:11.0-jre17

# Copy project
COPY . /usr/local/tomcat/webapps/BT12ex1_1_Copy/

# Kiểm tra cấu trúc
RUN echo "=== Cấu trúc ứng dụng ==="
RUN find /usr/local/tomcat/webapps/BT12ex1_1_Copy/ -type f | head -20

# Khởi động Tomcat với logging chi tiết
EXPOSE 8080
CMD ["catalina.sh", "run"]