package org.toylang.core;

import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.Statement;
import org.toylang.compiler.Compiler;

import java.io.File;
import java.io.IOException;

public class Application {


    public static void main(String[] args) throws Exception {
        try {
            compile("D:/IdeaProjects/Toylang/src/main/toylang/toylang/lang/");

            System.out.println("----------------------------------------------");

            compile("D:/IdeaProjects/Toylang/scripts/test/");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void compile(String path) throws IOException {
        Compiler compiler;
        ToyParser parser;
        ToyTree tree;
        File file = new File(path);
        for(File f : file.listFiles()) {
            parser = new ToyParser(f.getPath());
            tree = parser.parse();
            compiler = new Compiler(f.getPath(), f.getName().replace(".tl", ""), tree);
            compiler.compile();
        }
    }
}
