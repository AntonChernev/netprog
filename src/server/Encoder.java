package server;

import java.util.ArrayList;
import java.util.HashMap;

public class Encoder {
    private HashMap<String, Integer> strToInt;
    private ArrayList<String> intToStr;
    private ArrayList<String> types;
    private int lastInt;

    public Encoder() {
        strToInt = new HashMap<>();
        intToStr = new ArrayList<>();
        types = new ArrayList<>();
        lastInt = 1;
    }

    public int encode(String word, String type) {
        if(strToInt.containsKey(word)) { return strToInt.get(word); }
        else {
            strToInt.put(word, lastInt);
            intToStr.add(word);
            types.add(type);
            return lastInt++;
        }
    }

    public String getType(int number) {
        number--;
        if(0 <= number && number < types.size()) { return types.get(number); }
        else { return null; }
    }

    public String decode(int number) {
        number--;
        if(0 <= number && number < intToStr.size()) { return intToStr.get(number); }
        else { return null; }
    }
}
