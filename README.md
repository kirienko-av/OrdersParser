# Orders Parser

1. [Сборка](#build)
2. [Запуск приложения](#run)    
3. [Добавление поддержки нового формата входящих данных](#new)
    1. [Пример](#example)

### Сборка <a name="build"></a>
Сборка конечного приложения может быть выполнена командой: 
`mvn clean install`.

### Запуск приложения <a name="run"></a>
Пример команды запуска: `java -jar orders-parser.jar orders.csv orders.json`
где `orders.csv` и `orders.json` файлы для парсинга.

### Добавление поддержки нового формата входящих данных <a name="new"></a>
Что бы добавить поддержку нового формата данных необходимо создать 
 аннотированный, `import org.springframework.stereotype.Component` и `ru.kirienko.ordersparser.annotation.FileType` со значением `value` указывающим на расширение нового формата, класс, а так же имплементирующий интерфейс `ru.kirienko.ordersparser.parser.OrderParser`.

#### Пример <a name="example"></a>
```
@FileType("xls")
@Component
public class XlsOrderParser implements OrderParser {

    @Override
    public Stream<Order> lines(Path filePath) {
        ...
    }    
}
```