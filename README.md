# Chess game over Kafka

A chess game based on the public project [2P-Chess](https://github.com/wanfungchui/2P-Chess). The two opponents can play remotely using 
different instances of the game. After a user logs in, he can either create a table and wait for an opponent or join an existing table and 
play with an other user. Uses [Apache Kafka](https://kafka.apache.org/) to send moves and chat messages between the two players. Uses 
[RESTful web services](https://javaee.github.io/tutorial/jaxrs001.html) located at the project 
[chess-ws](https://github.com/Thanoschal/chess-ws) to support the game. The server project chess-ws is running using 
[Apache Tomcat](http://tomcat.apache.org/) and it's connected with a [MySQL](https://www.mysql.com/) database. We access and manage the
database using [phpmyadmin](https://www.phpmyadmin.net/). The Kafka server, the Apache Tomcat and the MySQL database are installed at the 
same VM with a static IP. <br/> <br/> ![image](https://github.com/patschris/ChessOverKafka/blob/master/AppAchitecture.png)
