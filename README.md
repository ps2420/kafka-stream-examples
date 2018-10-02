
 # kafka-stream-examples

## Installation location
```
download latest confluent and create two kafka topics test-topic and test-topic2.
Run the class KafkaStreamProducer under src/test/java it will post sample messages to these topics
Run KafkaStreamConsumers.. and play around with joins.. stream, ktable, globalKtable.
```
 
## Kafka Stream Producer
```
1. Mock the FxRateModel messages and post it on test-topic with tenor: test-topic
2. Mock the FxRateModel messages and post it on test-topic2 with tenor: test-topic2

later these topics will be used in KStream-KTable & KStream-GlobalKTable joining..
``` 

## Kafka Stream Consumer
```
Kafka Stream Consumer exposes different methods 
1. KStremIterate
2. KTableIterate
3. KStream.KTable() join
4. KStream.GlobalKTable() join.

run respective methods to print out the messages on console..
``` 

## Kafka commands
```
bin/kafka-topics --list --zookeeper localhost:2181
bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --config retention.ms=1440000000 --topic <topic-name>
bin/kafka-topics --zookeeper localhost:2181 --delete --topic <topic-name>

Adding topic retention message time
bin/kafka-configs --zookeeper localhost:2181 --alter --entity-type topics --entity-name <topic-name> --add-config retention.ms=86400000
```  