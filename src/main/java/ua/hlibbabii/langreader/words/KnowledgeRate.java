package ua.hlibbabii.langreader.words;

/**
 * Created by hlib on 29.05.16.
 */
public class KnowledgeRate {
    public static final KnowledgeRate NO = new KnowledgeRate(0.0);
    public static final KnowledgeRate LOW = new KnowledgeRate(0.2);
    public static final KnowledgeRate AVERAGE = new KnowledgeRate(0.5);
    public static final KnowledgeRate HIGH = new KnowledgeRate(0.8);

    private double value;

    public KnowledgeRate(double value) {
        checkRange(value);
        this.value = value;
    }

    private void checkRange(double value) {
        if (value < 0 && value > 1) {
            throw new IllegalArgumentException("Value must be in range [0, 1], but is " + value);
        }
    }

    public double getValue() {
        return value;
    }
}
