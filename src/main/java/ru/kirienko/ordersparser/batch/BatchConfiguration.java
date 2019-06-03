package ru.kirienko.ordersparser.batch;


import com.sun.org.apache.xpath.internal.operations.Bool;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kirienko.ordersparser.integration.FileType;
import ru.kirienko.ordersparser.integration.OrderItemReader;
import ru.kirienko.ordersparser.service.OrderItemReaderService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrderItemReaderService orderItemReaderService;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              OrderItemReaderService orderItemReaderService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.orderItemReaderService = orderItemReaderService;
    }

    @Bean
    public Job job() throws Exception {
        Step step = stepBuilderFactory.get("File-load")
                .<String, String>chunk(100)
                .reader(itemReader(null))
                .writer(i -> i.forEach(System.out::println))
                .build();
        return jobBuilderFactory.get("Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    @StepScope
    ItemStreamReader<String> itemReader(@Value("#{jobParameters[file_path]}") String filePath) throws Exception {
        return  orderItemReaderService
                .getOrderItemReaderByFileType(FilenameUtils.getExtension(filePath))
                .getItemReader(filePath);
    }
}
