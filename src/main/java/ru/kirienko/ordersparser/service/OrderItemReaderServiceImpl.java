package ru.kirienko.ordersparser.service;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.springframework.stereotype.Service;
import ru.kirienko.ordersparser.integration.FileType;
import ru.kirienko.ordersparser.integration.OrderItemReader;

@Service
public class OrderItemReaderServiceImpl implements OrderItemReaderService {
    @Override
    public OrderItemReader getOrderItemReaderByFileType(String fileType) throws Exception {
        ClassInfoList classInfos = new ClassGraph().enableAllInfo().whitelistPackages("ru.kirienko.ordersparser")
                .scan().getClassesWithAnnotation(FileType.class.getName())
                .filter(c -> c.getAnnotationInfo(FileType.class.getName())
                        .getParameterValues()
                        .getValue("value").equals(fileType));

        if (classInfos.size() > 1)
            throw new Exception("Не удается определить класс");

        return classInfos.stream().findFirst()
                .map(classInfo -> classInfo.loadClass(OrderItemReader.class))
                .orElseThrow(() -> new Exception("Не удалось загрузить класс"))
        .newInstance();
    }
}
