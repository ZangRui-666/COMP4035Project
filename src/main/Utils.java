package main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static List<String> SplitToNode(String line){
        List<String> list= new ArrayList<>();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(line);
        while(m.find()){
            list.add(m.group().substring(1, m.group().length()-1));
        }
        return list;
    }

    public static int binarySearch(List<String> list, String key) {
        int low = 0;
        int high = list.size() - 1;
        int midPos = 0;
        while (low <= high) {
            midPos = (low + high) / 2;
            String midVal = list.get(midPos);
            int cmp = midVal.compareTo(key);
            if (cmp < 0)
                low = midPos + 1;
            else if (cmp > 0)
                high = midPos - 1;
            else
                return midPos; // key found
        }
        return -(low + 1);  // key not found
    }
}
