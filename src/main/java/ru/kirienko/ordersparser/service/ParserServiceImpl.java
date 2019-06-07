package ru.kirienko.ordersparser.service;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.kirienko.ordersparser.annotation.FileType;
import ru.kirienko.ordersparser.dto.OrderDTO;
import ru.kirienko.ordersparser.parser.OrderParser;

import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    private ApplicationContext context;


    @Override
    public Stream<OrderDTO> getOrderStream(String filePath) {
        ClassInfoList classInfos = new ClassGraph().enableAllInfo().scan()
                .getClassesWithAnnotation(FileType.class.getName())
                .filter(classInfo -> classInfo.implementsInterface(OrderParser.class.getName()))
                .filter(classInfo -> classInfo.getAnnotationInfo(FileType.class.getName())
                        .getParameterValues().getValue("value")
                        .equals(FilenameUtils.getExtension(filePath))
                );

        if (classInfos.size() != 1)
            System.out.println("No reader class found for extension " + FilenameUtils.getExtension(filePath));

        OrderParser orderParser = context.getBean(classInfos.get(0)
                .loadClass(OrderParser.class));
        return orderParser.getOrderStream(Paths.get(filePath));
    }
}
