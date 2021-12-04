# kafka-losing-messages

Project for testing an error-prone kafka consumer.

### 1. Start the postgres and kafka environment
```shell
cd kafka-losing-messages/docker
docker-compose  -f postgres-env.yml up -d
docker-compose  -f kafka-env.yml up -d

# or
./startPostgresEnv.sh
./startKafkaEnw.sh
```
### 2. Now you are ready to start the kafka-losing-messages application



### 3. Producing messages to a topic
Run the Producer class

### 4. SQL to see and truncate the content of the consumer DB
```sql
select d.id, d.message, d.time from my_data d order by d.time;
truncate my_data;
```
