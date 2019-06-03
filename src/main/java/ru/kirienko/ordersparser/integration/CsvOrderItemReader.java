package ru.kirienko.ordersparser.integration;

import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.FileSystemResource;

@FileType("csv")
public class CsvOrderItemReader implements OrderItemReader{
    @Override
    public ItemStreamReader<String> getItemReader(String filePath) {
            FlatFileItemReader<String> reader = new FlatFileItemReader<>();
            final FileSystemResource fileResource = new FileSystemResource(filePath);
            reader.setResource(fileResource);
            reader.setLineMapper((line, lineNumber) -> "csv: " + lineNumber + " " + line + " " + filePath);
            return reader;
    }
}
