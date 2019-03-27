package de.ewe.tk.replicare.batchdemo.slave.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
public class SlaveConfiguration {

   private final RemotePartitioningWorkerStepBuilderFactory slaveStepBuilderFactory;

   public SlaveConfiguration(RemotePartitioningWorkerStepBuilderFactory slaveStepBuilderFactory) {
      this.slaveStepBuilderFactory = slaveStepBuilderFactory;
   }

   /*
    * Configure inbound flow (requests coming from the master)
    */
   @Bean
   public DirectChannel requests() {
      return new DirectChannel();
   }

   @Bean
   public IntegrationFlow inboundFlow(ActiveMQConnectionFactory connectionFactory) {
      return IntegrationFlows
            .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("requests"))
            .channel(requests())
            .get();
   }

   /*
    * Configure the slave step
    */
   @Bean
   public Step slaveStep() {
      return this.slaveStepBuilderFactory.get("slaveStep")
            .inputChannel(requests())
            .tasklet(tasklet(null))
            .build();
   }

   @Bean
   @StepScope
   public Tasklet tasklet(@Value("#{stepExecutionContext['partition']}") String partition) {
      return (contribution, chunkContext) -> {
         System.out.println("processing " + partition);
         return RepeatStatus.FINISHED;
      };
   }
}