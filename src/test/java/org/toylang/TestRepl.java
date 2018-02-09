package org.toylang;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.toylang.util.Settings;

// this file has been automatically generated. Do not edit
public class TestRepl {

    private static final java.io.PrintStream sout = System.out;
    private static final java.io.PrintStream serr = System.out;

    @BeforeClass
    public static void before() {
        org.toylang.util.Utility.buildBuiltins();
        Settings.set("REPL", true);
    }

    @AfterClass
    public static void after() {
        System.setOut(sout);
        System.setErr(serr);
        Settings.set("REPL", false);
    }
    @Test
    public void comment() throws Throwable {
        ReplTestRunner.doTest("testData/repl/comment.repl");
    }
    @Test
    public void constants_pi() throws Throwable {
        ReplTestRunner.doTest("testData/repl/constants_pi.repl");
    }
    @Test
    public void constant_decimal() throws Throwable {
        ReplTestRunner.doTest("testData/repl/constant_decimal.repl");
    }
    @Test
    public void constant_num() throws Throwable {
        ReplTestRunner.doTest("testData/repl/constant_num.repl");
    }
    @Test
    public void constant_str() throws Throwable {
        ReplTestRunner.doTest("testData/repl/constant_str.repl");
    }
    @Test
    public void dict() throws Throwable {
        ReplTestRunner.doTest("testData/repl/dict.repl");
    }
    @Test
    public void errors() throws Throwable {
        ReplTestRunner.doTest("testData/repl/errors.repl");
    }
    @Test
    public void expr_add() throws Throwable {
        ReplTestRunner.doTest("testData/repl/expr_add.repl");
    }
    @Test
    public void function() throws Throwable {
        ReplTestRunner.doTest("testData/repl/function.repl");
    }
    @Test
    public void imp() throws Throwable {
        ReplTestRunner.doTest("testData/repl/imp.repl");
    }
    @Test
    public void lists() throws Throwable {
        ReplTestRunner.doTest("testData/repl/lists.repl");
    }
    @Test
    public void object() throws Throwable {
        ReplTestRunner.doTest("testData/repl/object.repl");
    }
    @Test
    public void object_field() throws Throwable {
        ReplTestRunner.doTest("testData/repl/object_field.repl");
    }
    @Test
    public void object_function() throws Throwable {
        ReplTestRunner.doTest("testData/repl/object_function.repl");
    }
    @Test
    public void types() throws Throwable {
        ReplTestRunner.doTest("testData/repl/types.repl");
    }
    @Test
    public void variable() throws Throwable {
        ReplTestRunner.doTest("testData/repl/variable.repl");
    }

}
