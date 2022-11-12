# kafka
 
  .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
   .\bin\windows\kafka-server-start.bat .\config\server.properties
    .\bin\windows\connect-distributed.bat .\config\connect-distributed.properties
	 .\bin\elasticsearch-service-x64.exe
	GET localhost:8083/connector-plugins
	
	.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic stock --from-beginning
	.\bin\windows\kafka-console-consumer.bat --bootstrap-server=localhost:9092 --topic mysql.stock --from-beginning
	
	
	POST:  http://localhost:8083/connectors
	{
"name": "mysql-stock-connector",
"config": {
"connector.class": "JdbcSourceConnector",
"connection.url": "jdbc:mysql://127.0.0.1:3306/spring?user=root",
"connection.password": "password",
"mode": "incrementing",
"incrementing.column.name": "id",
"table.whitelist": "spring.stock",
"validate.non.null": "false",
"topic.prefix": "mysql.",
"name": "mysql-stock-connector"
}
}

GET http://localhost:8083/connectors/mysql-stock-connector/status
DELETE  http://localhost:8083/connectors/mysql-stock-connector

{
"name":"elasticsearch_stock_sink",
"config":{
"connector.class":"io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
"connection.url":"http://localhost:9200",
"tasks.max":"1",
"topics":"mysql-stock",
"name":"elasticsearch_stock_sink",
"type.name":"_doc",
"key.ignore":"true"
}
}
