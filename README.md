# Spring Batch Demo for remote partitioning

## About
This is a demo about [remote partitioning](https://docs.spring.io/spring-batch/4.1.x/reference/html/index-single.html#partitioning) with SpringBoot 2 and Spring Batch.

The demo (in the Docker composing) consists of one master and two slave/worker applications. They are communicating over an ActiveMQ server. It is based on
a [sample by Spring](https://github.com/spring-projects/spring-batch/tree/master/spring-batch-samples/src/main/java/org/springframework/batch/sample/remotepartitioning).

Here is an overview of the components in the Docker composing:

![Composing overview](docs/overview.png?raw=true "Composing overview")

## Requirements
This demo is build with **Maven 3.x**, **Java 11** and **Docker**.

## Usage
1. At first you have to build both applications in order to **create the docker images** with `mvn package` in the root module
2. Then you can start the demo **Docker composing** in the folder **/docker/demo/** with `docker-compose up`
3. You can **start the batch job** by requesting **http://localhost:8080/startjob**
4. Take a look at the **Docker logs** how the remote slaves (workers) are processing the job

## Creator

**Stephan Zerhusen**

* <https://twitter.com/stzerhus>
* <https://github.com/szerhusenBC>

## Copyright and license

The code is released under the [MIT license](LICENSE?raw=true).
