package main;

public class SearchResult {
    String key;
    int value;

    public SearchResult(String key, int value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString(){
        return "(" + key + "," + value + ")";
    }
}
