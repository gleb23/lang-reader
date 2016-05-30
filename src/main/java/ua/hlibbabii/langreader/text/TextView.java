package ua.hlibbabii.langreader.text;

import java.util.Date;

/**
 * Created by hlib on 26.05.16.
 */
public class TextView {
    private int textViewId;
    private int userId;
    private Date dateTime;
    private String textId;

    /* transient fields, they can be restored from datasource */
    private NormalizedText normalizedText;
    private int textNumber;

    public TextView() {
    }

    public TextView(int textViewId, int userId, Date dateTime,
                    String textId, NormalizedText normalizedText, int textNumber) {
        this.textViewId = textViewId;
        this.userId = userId;
        this.dateTime = dateTime;
        this.normalizedText = normalizedText;
        this.textId = textId;
        this.textNumber = textNumber;
    }

    public int getTextViewId() {
        return textViewId;
    }

    public void setTextViewId(int textViewId) {
        this.textViewId = textViewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public NormalizedText getNormalizedText() {
        return normalizedText;
    }

    public void setNormalizedText(NormalizedText normalizedText) {
        this.normalizedText = normalizedText;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
    }

    public int getTextNumber() {
        return textNumber;
    }
}
