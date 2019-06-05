# Orders Parser

1. [Сборка](#build)
2. [Запуск приложения](#run)    
3. [Добавление поддержки нового формата входящих данных](#new)
    1. [Пример](#example)

### Сборка <a name="build"></a>
Сборка конечного приложения может быть выполнена командой: 
`mvn clean install`.

### Запуск приложения <a name="run"></a>
Пример команды запуска: `java -jar orders-parser.jar orders1.csv orders2.json`
где `orders1.csv` и `orders2.json` файлы для парсинга.

### Добавление поддержки нового формата входящих данных <a name="new"></a>
Что бы добавить поддержку нового формата данных необходимо создать класс расширенный абстрактным классом `ru.kirienko.ordersparser.reader.OrderItemReader` который должен быть аннотированным с помощью `import org.springframework.stereotype.Component` и `import ru.kirienko.ordersparser.configuration.FileType` с установленным значением `value` указывающим на расширение нового формата, а так же переопределить методы `FlatFileItemReader<Order> itemReader(String filePath)` и `Order lineMapper(OrderLine orderLine)`.

## Пример <a name="example"></a>
```
@FileType("xls")
@Component
public class XlsOrderItemReader extends OrderItemReader {

    @Override
    public FlatFileItemReader<Order> itemReader(String filePath) {
        ...
    }

    @Override
    public Order lineMapper(OrderLine orderLine) {
        ...
    }
}
```