package ua.hlibbabii.langreader.text;

/**
 * Created by hlib on 28.05.16.
 */
public class TextInfo {
    private String id;

    public TextInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TextInfo{" +
                "id='" + id + '\'' +
                '}';
    }
}
