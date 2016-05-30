package ua.hlibbabii.langreader.dao;

import java.io.IOException;

/**
 * Created by hlib on 27.05.16.
 */
public class TextRepositoryException extends Exception {
    public TextRepositoryException(String s, IOException e) {
        super(s, e);
    }
}
