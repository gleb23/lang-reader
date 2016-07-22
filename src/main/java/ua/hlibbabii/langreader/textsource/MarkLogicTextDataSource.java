package ua.hlibbabii.langreader.textsource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.document.DocumentDescriptor;
import com.marklogic.client.document.DocumentUriTemplate;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.extra.gson.GSONHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StructuredQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.hlibbabii.langreader.text.NormalizedText;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hlib on 27.05.16.
 */
@Repository
public class MarkLogicTextDataSource implements TextDataSource {

    @Autowired
    private DatabaseClient databaseClient;

    private JSONDocumentManager jsonDocumentManager;
    private QueryManager queryManager;
    private QueryManager documentNumberQueryManager;

    @PostConstruct
    public void init() {
        jsonDocumentManager = databaseClient.newJSONDocumentManager();
        queryManager = databaseClient.newQueryManager();
        documentNumberQueryManager = databaseClient.newQueryManager();
        documentNumberQueryManager.setPageLength(1);
    }

    @Override
    public String save(NormalizedText normalizedParagraph) {
        JsonElement normalizedTextJson = new Gson().toJsonTree(normalizedParagraph, NormalizedText.class);
        DocumentUriTemplate documentUriTemplate = jsonDocumentManager.newDocumentUriTemplate("json");
        documentUriTemplate.setDirectory("/texts/");
        DocumentDescriptor documentDescriptor = jsonDocumentManager.create(documentUriTemplate, new GSONHandle
                (normalizedTextJson));
        return documentDescriptor.getUri();
    }

    @Override
    public NormalizedText getById(String id) {
        GSONHandle gsonHandle = jsonDocumentManager.read(id, new GSONHandle());
        JsonElement jsonElement = gsonHandle.get();
        NormalizedText normalizedParagraph = new Gson().fromJson(jsonElement, NormalizedText.class);
        return normalizedParagraph;
    }

    @Override
    public Set<String> getAllTextIds() {
        StructuredQueryBuilder structuredQueryBuilder = queryManager.newStructuredQueryBuilder();
        SearchHandle searchHandle = queryManager.search(structuredQueryBuilder.directory(true, "/texts/"), new
                SearchHandle());
        return Arrays.asList(searchHandle.getMatchResults()).stream().map(m -> m.getUri()).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAllTextSnippets() {
        StructuredQueryBuilder structuredQueryBuilder = queryManager.newStructuredQueryBuilder();
        SearchHandle searchHandle = queryManager.search(structuredQueryBuilder.directory(true, "/texts/"), new
                SearchHandle());
        return Arrays.asList(searchHandle.getMatchResults())
                     .stream()
                     .map(m -> m.getFirstSnippetText())
                     .collect(Collectors.toSet());
    }


    @Override
    public long getNumberOfAvailableTexts() {
        StructuredQueryBuilder structuredQueryBuilder = queryManager.newStructuredQueryBuilder();
        SearchHandle search = documentNumberQueryManager.search(structuredQueryBuilder.directory(true, "/texts/"),
                new SearchHandle());
        return search.getTotalResults();
    }

    @Override
    public int removeAllTexts() {
        StructuredQueryBuilder structuredQueryBuilder = queryManager.newStructuredQueryBuilder();
        SearchHandle searchHandle = queryManager.search(structuredQueryBuilder.directory(true, "/texts/"), new
                SearchHandle());
        int textsToRemove = searchHandle.getMatchResults().length;
        Arrays.asList(searchHandle.getMatchResults()).stream().forEach(m -> jsonDocumentManager.delete(m.getUri()));
        return textsToRemove;
    }

    @Override
    public List<String> search(String searchPhrase) {
        StructuredQueryBuilder structuredQueryBuilder = queryManager.newStructuredQueryBuilder();
        SearchHandle searchHandle = queryManager.search(structuredQueryBuilder.directory(true, "/texts/"), new
                SearchHandle());
        List<String> snippets = Arrays.asList(searchHandle.getMatchResults())
                                      .stream()
                                      .map(m -> m.getSnippets())
                                      .flatMap(m -> Arrays.asList(m).stream())
                                      .map(m -> m.getTextContent())
                                      .collect(Collectors.toList());
        return snippets;
    }
}
