package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainRun {
    public static void main(String[] args)   {
        new MainRun().RunApp();
    }

    public void RunApp()  {
        Scanner in = new Scanner(System.in);
        boolean quit = false;
        BPlusTree tree = new BPlusTree();
        System.out.print("> ");
        while (!quit) {
            if (in.hasNextLine()) {
                String[] input = in.nextLine().split(" ");
                if (input.length == 2) {
                    switch (input[0]) {
                        case "btree":
                            switch (input[1]) {
                                case "-help":
                                    System.out.println("Usage: btree [fname]");
                                    System.out.println("fname: the name of the data file storing the search key values");
                                    break;
                                default:
                                    try {
                                        File inputFile = new File(input[1]);
                                        if (!inputFile.exists())
                                            System.out.println("The input path does not exist, please check!");
                                            //todo: add btree build function
                                        else {
                                            System.out.println("Building an initial B+-tree... Launching the B+-tree test program...");
                                            tree = new BPlusTree(inputFile);
                                            System.out.println("Waiting for your commands: ");
                                        }
                                    } catch (FileNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }

                            }
                            break;
                        case "insert":
                            tree.put(input[1], 0);
                            System.out.println("The key " + input[1] + " has been inserted in the B+-tree!");
                            break;
                        case "delete":
                            tree.Remove(input[1]);
                            break;
                        default:
                            System.out.println("Invalid input format.");
                    }
                } else if (input.length == 1) {
                    switch (input[0]) {
                        case "print":
                            System.out.println(tree);
                            break;
                        case "stats":
                            tree.DumpStatistics();
                            break;
                        case "quit":
                            quit = true;
                            System.out.println("The program is terminated.");
                            break;
                        default:
                            System.out.println("Invalid input format.");
                    }
                } else if (input.length == 3) {
                    if (input[0].equals("search"))
                        System.out.println(tree.Search(input[1], input[2]));
                    else System.out.println("Invalid input format.");
                } else System.out.println("Invalid input format.");
            }
            System.out.print("> ");
        }
        in.close();
    }
}
