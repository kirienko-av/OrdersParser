package ru.kirienko.ordersparser.integration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import ru.kirienko.ordersparser.domain.Order;

import java.io.File;
import java.util.concurrent.Executors;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfiguration {

    private final Job job;
    private final JobLauncher jobLauncher;

    @Autowired
    public IntegrationConfiguration(Job job, JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Bean
    public MessageChannel filesChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel printChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow fromFile() {
        return IntegrationFlows.from(filesChannel())
                .split()
                .channel(c -> c.executor(Executors.newCachedThreadPool()))
                .transform(String.class, this::fileReader)
                .transform(fileMessageToJobRequest())
                .handle(jobLaunchingMessageHandler())
                .handle(Message::getPayload)
                .get();
    }

    @Bean
    public IntegrationFlow fromBatch() {
        return IntegrationFlows.from("printChannel")
                .bridge()
                .get();
    }

    public File fileReader(String message) {
        return new File(message);
    }

    @Bean
    public FileMessageToJobRequest fileMessageToJobRequest() {
        FileMessageToJobRequest fileMessageToJobRequest = new FileMessageToJobRequest();
        fileMessageToJobRequest.setFileParameterName("file_path");
        fileMessageToJobRequest.setJob(job);
        return fileMessageToJobRequest;
    }

    @Bean
    public JobLaunchingMessageHandler jobLaunchingMessageHandler() {
        return new JobLaunchingMessageHandler(jobLauncher);
    }

    @ServiceActivator(inputChannel = "printChannel")
    public void printOrder(Order order) {
        System.out.println(order);
    }
}
