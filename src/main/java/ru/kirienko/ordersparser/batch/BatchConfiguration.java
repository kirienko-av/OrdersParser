package ru.kirienko.ordersparser.batch;


import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.service.OrderService;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrderService orderService;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              OrderService orderService,
                              ModelMapper modelMapper) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.orderService = orderService;
    }

    @Bean
    public Job job() throws Exception {
        Step step = stepBuilderFactory.get("File-load")
                .<Order, String>chunk(100)
                .reader(itemReader(null))
                .writer(i -> {})
                .build();
        return jobBuilderFactory.get("Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    @StepScope
    ItemStreamReader<Order> itemReader(@Value("#{jobParameters[file_path]}") String filePath) throws Exception {
        return  orderService
                .getOrderItemReaderByFileType(FilenameUtils.getExtension(filePath))
                .getItemReader(filePath);
    }
}
