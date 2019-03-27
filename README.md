# Spring Batch Demo for remote partitioning

## About
This is a demo about [remote partitioning](https://docs.spring.io/spring-batch/4.1.x/reference/html/index-single.html#partitioning) with SpringBoot 2 and Spring Batch.

## Requirements
This demo is build with **Maven 3.x**, **Java 11** and **Docker**.

## Usage
1. At first you have to build both applications in order to **create the docker images** with `mvn package`
2. Then you can start the demo **Docker composing** in the folder **/docker/demo/** with `docker-compose up`
3. You can **start the batch job** by requesting **http://localhost:8080/startjob**
4. Take a look at the **Docker logs** how the remote slaves (workers) are processing the job#
