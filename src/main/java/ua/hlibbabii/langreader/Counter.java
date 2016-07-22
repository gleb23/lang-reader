package ua.hlibbabii.langreader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hlib on 11.06.16.
 */
public class Counter {

    Map<Integer, Boolean> occurences = new HashMap<Integer, Boolean>() {{
        put(1, false);
        put(5, false);
        put(7, true);
        put(9, true);
        put(10, true);
        put(12, true);
        put(15, true);
        put(23, true);
    }};
    private int currentText = 2000;

    public static double logb(double a, double b) {
        return Math.log(a) / Math.log(b);
    }

    public static double log2(double a) {
        return logb(a, 2);
    }

    public static void main(String[] args) {
        System.out.println(new Counter().count1());
    }

    private double count1() {
        List<Integer> all = new ArrayList<>(occurences.keySet());
        Collections.sort(all);
        double allSum = 0;
        double knownSum = 0;
        for (int i = 0; i < all.size(); i++) {
            int textNumber = all.get(all.size() - 1 - i);
            double val = 1.0 / (1 + log2(currentText - textNumber)) / (i + 1);
            if (occurences.get(textNumber)) {
                knownSum += val;
            }
            allSum += val;
            System.out.println(val);
        }
        return knownSum / allSum;
    }
}