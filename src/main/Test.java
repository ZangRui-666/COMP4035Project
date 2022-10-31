package main;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        new Test().test();
    }
    public void test() {
        BPlusTree tree = new BPlusTree();
        tree.put("ad",1);
        tree.put("bd",2);
        tree.put("cd",2);
        tree.put("dd",2);
        tree.put("aa",2);
        System.out.println(tree);
        tree.put("ab",2);
        tree.put("ac",2);
        tree.put("cd",2);
        tree.put("cf",2);
        tree.put("cr",2);
        tree.put("ee",2);
        System.out.println(tree);
        tree.put("gg",2);
        tree.put("hh",2);
        tree.put("ll",2);
        System.out.println(tree);
        tree.put("ua",1);
        tree.put("ue",1);
        tree.put("ub",1);
        tree.put("up",1);
        System.out.println(tree);
        tree.put("gh",1);
        tree.put("gg",1);
        tree.put("ga",1);
        System.out.println(tree);


    }

    public void testSearch(){
        System.out.println(Utils.binarySearch(Arrays.asList("1","3","5","8"), "0"));
        System.out.println(Utils.binarySearch(Arrays.asList("1","3","5","8"), "2"));
        System.out.println(Utils.binarySearch(Arrays.asList("1","3","5","8"), "4"));
        System.out.println(Utils.binarySearch(Arrays.asList("1","3","5","8"), "6"));
        System.out.println(Utils.binarySearch(Arrays.asList("1","3","5","8"), "9"));




    }

}
