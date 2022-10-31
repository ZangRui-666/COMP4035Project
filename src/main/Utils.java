package main;

import java.util.List;

public class Utils {

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
