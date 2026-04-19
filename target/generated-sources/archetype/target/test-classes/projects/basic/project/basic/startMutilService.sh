# 方式A：Maven 启动（你现在用的）
mvn spring-boot:run \
-Dspring-boot.run.arguments= \
--serverPort=8081, \
--serverName=benefit-service, \
--dbName=benefit_db

# 方式B：打Jar包后启动（生产环境）
java -jar benefit.jar \
--serverPort=8081 \
--serverName=benefit-service \
--dbName=benefit_db