package main;


import main.util.ArgReader;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }

        ArgReader argReader = new ArgReader(args);
    }
}
