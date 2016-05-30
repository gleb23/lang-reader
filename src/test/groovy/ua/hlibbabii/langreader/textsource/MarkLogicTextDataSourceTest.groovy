package ua.hlibbabii.langreader.textsource
import com.marklogic.client.DatabaseClient
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import ua.hlibbabii.langreader.dao.TextReader

/**
 * Created by hlib on 28.05.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ["classpath:spring/spring.xml"])
class MarkLogicTextDataSourceTest extends GroovyTestCase {

    @Autowired
    private MarkLogicTextDataSource markLogicTextDataSource;

    @Autowired
    private TextProcessor textProcessor;

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private TextReader textReader;

    private String id;

    @Test
    void testProcess() {
//        def s = "Hello, my dear!"
        def s = textReader.getText("Article247_5.txt")
        id = textProcessor.processText(s)
        def normalizedText = markLogicTextDataSource.getById(id)
        def expected = '{"text":["Hello",",","my","dear","!"],"normalized":{"0":"hello","2":"my","3":"dear"}}'

        assert expected == normalizedText.toJson()
        print normalizedText.toJson()
    }

    protected void tearDown() {
        def manager = databaseClient.newJSONDocumentManager();
        manager.delete(id);
    }
}
