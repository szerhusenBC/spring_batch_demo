package org.zerhusen.batchdemo.master.configuration;

import org.zerhusen.batchdemo.master.BasicPartitioner;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningMasterStepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
public class MasterConfiguration {

	private static final int GRID_SIZE = 100;

	private final JobBuilderFactory jobBuilderFactory;

	private final RemotePartitioningMasterStepBuilderFactory masterStepBuilderFactory;


	public MasterConfiguration(JobBuilderFactory jobBuilderFactory,
							   RemotePartitioningMasterStepBuilderFactory masterStepBuilderFactory) {

		this.jobBuilderFactory = jobBuilderFactory;
		this.masterStepBuilderFactory = masterStepBuilderFactory;
	}

	/*
	 * Configure outbound flow (requests going to workers)
	 */
	@Bean
	public DirectChannel requests() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow outboundFlow(ActiveMQConnectionFactory connectionFactory) {
		return IntegrationFlows
				.from(requests())
				.handle(Jms.outboundAdapter(connectionFactory).destination("requests"))
				.get();
	}

	/*
	 * Configure the master step
	 */
	@Bean
	public Step masterStep() {
		return this.masterStepBuilderFactory.get("masterStep")
				.partitioner("slaveStep", new BasicPartitioner())
				.gridSize(GRID_SIZE)
				.outputChannel(requests())
				.build();
	}

	@Bean
	public Job remotePartitioningJob() {
		return this.jobBuilderFactory.get("remotePartitioningJob")
				.start(masterStep())
				.build();
	}

}