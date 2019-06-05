package ru.kirienko.ordersparser.service;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.kirienko.ordersparser.configuration.FileType;
import ru.kirienko.ordersparser.domain.OrderValidation;
import ru.kirienko.ordersparser.reader.OrderItemReader;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ApplicationContext context;

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
                .map(cls -> context.getBean(cls))
                .orElseThrow(() -> new Exception("Не удалось загрузить класс"));
    }

    @Override
    public OrderValidation validation(String fieldName, String value) {
        final OrderValidation orderValidation = new OrderValidation(fieldName, value);
        if (value == null) {
            orderValidation.setDescription("Поле " + fieldName + " обязательное для заполнения");
        } else {
            switch (fieldName) {
                case "id":
                    if (!value.matches("^\\d+$"))
                        orderValidation.setDescription("Поле " + fieldName + " не соответствует маске ^\\d+$");
                    break;
                case "amount":
                    if (!value.matches("^\\d+\\.?\\d+$"))
                        orderValidation.setDescription("Поле " + fieldName + " не соответствует маске ^\\d+\\.?\\d+$");
                    break;
                case "currency":
                    if (!value.matches("^[A-Z]+$"))
                        orderValidation.setDescription("Поле " + fieldName + " не соответствует маске ^[A-Z]+$");
                    break;
                case "comment":
                    if (!value.matches("^\\S+(.*\\S+)*$"))
                        orderValidation.setDescription("Поле " + fieldName + " не соответствует маске ^\\S+(.*\\S+)*$");
                    break;
                default:
                    orderValidation.setDescription("Не удалось расспознать назначение данных");
                    break;

            }
        }
        if (orderValidation.getDescription() == null)
            orderValidation.setDescription("OK");
        return orderValidation;
    }
}
