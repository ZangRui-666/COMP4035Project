package main;

import java.io.File;
import java.util.Scanner;

public class MainRun {
    public static void main(String[] args){
        new MainRun().RunApp();
    }
    public void RunApp(){
        Scanner in = new Scanner(System.in);
        boolean quit = false;
        BPlusTree tree = new BPlusTree();
        while (!quit){
            if(in.hasNext()){
                String[] input = in.next().split(" ");
                if(input.length == 2) {
                    switch (input[0]) {
                        case "btree":
                            switch (input[1]){
                                case "-help":
                                    System.out.println("Usage: btree [fname]");
                                    System.out.println("fname: the name of the data file storing the search key values");
                                    break;
                                default:
                                    try{
                                        File inputFile = new File(input[1]);
                                        if(!inputFile.exists())
                                            System.out.println("The input path is not exist, please check!");
                                        //todo: add btree build function
                                    }catch (Exception e){
                                        System.out.println(e);
                                    }
                            }
                            break;
                        case "insert":
                            tree.put(input[1], 0);
                            break;
                        case "delete":
                            tree.Remove(input[1]);
                            break;
                        default:
                            System.out.println("Invalid input format.");
                    }
                }else if(input.length == 1){
                    switch (input[0]) {
                        case "print":
                            System.out.println(tree);
                            break;
                        case "stats":
                            tree.DumpStatistics();
                            break;
                        case "quit":
                            quit = true;
                            break;
                        default:
                            System.out.println("Invalid input format.");
                    }
                }else if(input.length == 3){
                    tree.Search(input[1], input[2]);
                }else System.out.println("Invalid input format.");
            }
        }
        in.close();
    }
}
