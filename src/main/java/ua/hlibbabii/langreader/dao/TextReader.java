package ua.hlibbabii.langreader.dao;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by hlib on 27.05.16.
 */
@Repository
public class TextReader {

    @Value("${text.directory}")
    private String textDirectory;

    public String getText(String textId) throws TextRepositoryException {
        try {
            String text = IOUtils.toString(new FileInputStream(textDirectory + "/" + textId));
            return text;
        } catch (IOException e) {
            throw new TextRepositoryException("Can't get text with textId: " + textId, e);
        }
    }
}
