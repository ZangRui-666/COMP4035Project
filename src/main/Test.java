package main;

public class Test {
    public static void main(String[] args) {
        new Test().test();
    }

    public void test() {
        BPlusTree tree = new BPlusTree();
        tree.put("ad", 1);
        System.out.println(tree);
        tree.DumpStatistics();
        tree.put("bd", 2);
        tree.put("cd", 2);
        tree.put("dd", 2);
        tree.put("aa", 2);
        System.out.println(tree);
        tree.DumpStatistics();
        tree.put("ab", 2);
        tree.put("ac", 2);
        tree.put("cd", 2);
        tree.put("cf", 2);
        tree.put("cr", 2);
        tree.put("ee", 2);
        System.out.println(tree);
        tree.DumpStatistics();
        tree.put("gg", 2);
        tree.put("hh", 2);
        tree.put("ll", 2);
        System.out.println(tree);
        tree.put("ua", 1);
        tree.put("ue", 1);
        tree.put("ub", 1);
        tree.put("up", 1);
        System.out.println(tree);
        tree.DumpStatistics();
        tree.put("gh", 1);
        tree.put("gg", 1);
        tree.put("ga", 1);
        tree.put("fa", 1);
        tree.put("fb", 1);
        tree.put("fc", 1);
        tree.put("fd", 1);
        tree.put("fe", 1);
        tree.put("ff", 1);
        tree.put("fo", 1);
        System.out.println(tree);
        tree.DumpStatistics();
        tree.put("oo", 1);
        tree.put("oa", 1);
        tree.put("oc", 1);
        tree.put("ob", 1);
        tree.put("op", 1);
        tree.put("ol", 1);
        tree.put("ok", 1);
        tree.put("or", 1);
        tree.put("io", 1);
        tree.put("ia", 1);
        tree.put("ic", 1);
        tree.put("ib", 1);
        tree.put("ip", 1);
        tree.put("il", 1);
        tree.put("ik", 1);
        tree.put("ir", 1);
        tree.put("eo", 1);
        tree.put("ea", 1);
        tree.put("ec", 1);
        tree.put("eb", 1);
        tree.put("ep", 1);
        tree.put("el", 1);
        tree.put("ek", 1);
        tree.put("er", 1);
        tree.put("jo", 1);
        tree.put("ja", 1);
        tree.put("jc", 1);
        tree.put("jb", 1);
        tree.put("jp", 1);
        tree.put("jl", 1);
        tree.put("jk", 1);
        tree.put("jr", 1);
        tree.put("ko", 1);
        tree.put("ka", 10);
        tree.put("kc", 1);
        tree.put("kb", 1);
        tree.put("kp", 1);
        tree.put("kl", 1);
        tree.put("kk", 1);
        tree.put("kr", 1);
        tree.put("ed", 1);
        tree.put("ee", 1);
        tree.put("ef", 10);
        tree.put("eg", 1);
        tree.put("eh", 1);
        tree.put("ei", 1);
        tree.put("ej", 1);
        tree.put("ek", 1);
        System.out.println(tree);
        testGet(tree);


    }

    void testGet(BPlusTree tree) {
        //for test purpose, these tow value are set to 10 while others are 1
        System.out.println(tree.search("ka", "kp"));
        System.out.println(tree.search("ef", "ei"));
    }

}
