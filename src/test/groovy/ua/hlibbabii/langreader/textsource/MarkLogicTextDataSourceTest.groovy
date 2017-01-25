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
        def s = "Harmonic Convergences\n" +
                "            You're right, Maxim's strong point is that it's totally unsentimental\n" +
                "and ungenteel.\n" +
                "            Another thing I've noticed is that the trajectory of the new men's magazines\n" +
                "and that of contemporary women's magazines seem to be converging, at least\n" +
                "compared with 20 years ago."
        id = textProcessor.processText(s)
        def normalizedText = markLogicTextDataSource.getById(id)
        def expected = '{"caption":{"text":["Harmonic","Convergences"],"normalized":{"0":"harmonic","1":"convergence"}},"paragraphs":[{"text":["You","\\u0027re","right",",","Maxim","\\u0027s","strong","point","is","that","it","\\u0027s","totally","unsentimental"],"normalized":{"0":"you","1":"be","2":"right","5":"\\u0027s","6":"strong","7":"point","8":"be","9":"that","10":"it","11":"be","12":"totally","13":"unsentimental"}},{"text":["and","ungenteel","."],"normalized":{"0":"and","1":"ungenteel"}},{"text":["Another","thing","I","\\u0027ve","noticed","is","that","the","trajectory","of","the","new","men","\\u0027s","magazines"],"normalized":{"0":"another","1":"thing","2":"I","3":"have","4":"notice","5":"be","6":"that","7":"the","8":"trajectory","9":"of","10":"the","11":"new","12":"man","13":"\\u0027s","14":"magazine"}},{"text":["and","that","of","contemporary","women","\\u0027s","magazines","seem","to","be","converging",",","at","least"],"normalized":{"0":"and","1":"that","2":"of","3":"contemporary","4":"woman","5":"\\u0027s","6":"magazine","7":"seem","8":"to","9":"be","10":"converge","12":"at","13":"least"}},{"text":["compared","with","20","years","ago","."],"normalized":{"0":"compare","1":"with","2":"20","3":"year","4":"ago"}}]}'

        assert expected == normalizedText.toJson()
    }

    protected void tearDown() {
        def manager = databaseClient.newJSONDocumentManager();
        manager.delete(id);
    }
}
