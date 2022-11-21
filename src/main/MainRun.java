package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MainRun {
    BPlusTree tree;

    public static void main(String[] args) {
        new MainRun().RunApp();
    }

    public void RunApp() {
        Scanner in = new Scanner(System.in);
        tree = new BPlusTree();
        System.out.print("> ");
        while (true) {
            if (in.hasNextLine()) {
                String inputCommand = in.nextLine();
                String[] input = inputCommand.split(" ");
                if (input.length > 1) {
                    switch (input[0].toLowerCase()) {
                        case "tree":
                            String remainStr = inputCommand.substring(input[0].length() + 1);
                            System.out.println("This is the remian Str :" + remainStr);
                            if ("-help".equals(remainStr)) {
                                System.out.println("Usage: btree [fname]");
                                System.out.println("fname: the name of the data file storing the search key values");
                            } else if (input[1].equalsIgnoreCase("save") && input.length == 3) {

                            } else {
                                try {
                                    File inputFile = new File(remainStr);
                                    if (!inputFile.exists())
                                        System.out.println("The input path does not exist, please check!");
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
                        case "save":
                            if (input.length != 2)
                                System.out.println("Invalid input format.");
                            else {
                                try {
                                    tree.Save(input[1]);
                                } catch (IOException e) {
                                    System.out.println("save failed");
                                }
                            }
                            break;
                        case "read":
                            if (input.length != 2) {
                                System.out.println("Invalid input format.");
                            } else {
                                try {
                                    System.out.println(input[1]);
                                    this.tree = (BPlusTree) this.tree.ReadTree(input[1]);
                                } catch (IOException | ClassNotFoundException e) {
                                    System.out.println("read failed");
                                }
                            }
                            break;
                        case "insert":
                            if (input.length != 2) {
                                System.out.println("Invalid input format.");
                            } else {
                                if (tree.Insert(input[1], 0))
                                    System.out.println("The key " + input[1] + " has been inserted in the B+-tree!");
                                else
                                    System.out.println("The key " + input[1] + " is duplicated, you can use update command to update the valuein");
                            }
                            break;
                        case "delete":
                            if (input.length != 2) {
                                System.out.println("Invalid input format.");
                            } else {
                                boolean result = tree.Remove(input[1]);
                                if (!result) {
                                    System.out.println("The key: " + input[1] + " is not in the B+-tree.");
                                } else System.out.println("The key " + input[1] + " has been deleted in the B+-tree.");
                            }
                            break;

                        case "update":
                            if (input.length != 3) {
                                System.out.println("Invalid input format.");
                            } else {
                                if (tree.Update(input[1], input[2]))
                                    System.out.println("Update successfully");
                                else System.out.println("Key not found");
                            }
                            break;
                        case "search":
                            if (input.length > 3) {
                                System.out.println("Invalid input format.");
                            } else if (input.length == 3)
                                if (input[1].compareTo(input[2]) > 0) {
                                    System.out.println("key1 should not be larger than key2");
                                } else
                                    System.out.println(tree.Search(input[1], input[2]));
                            else
                                System.out.println(tree.Search(input[1], input[1]));
                            break;
                        case "fuzzysearch":
                            if (input.length != 2)
                                System.out.println("Invalid input format.");
                            else {
                                System.out.println(tree.PrefixSearch(input[1]));

                            }
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
                            System.out.println("The program is terminated.");
                            in.close();
                            return;
                        case "destroy":
                            tree.ClearTree();
                            System.out.println("The tree was cleared");
                            break;
                        default:
                            System.out.println("Invalid input format.");
                            break;
                    }
                } else System.out.println("Invalid input format.");
            }
            System.out.print("> ");
        }
    }
}
