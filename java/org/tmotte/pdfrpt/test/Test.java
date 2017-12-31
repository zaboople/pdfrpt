package org.tmotte.pdfrpt.test;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.tmotte.pdfrpt.test.simplepdf.*;
import org.tmotte.pdfrpt.test.report.*;
import org.tmotte.pdfrpt.test.performance.*;
import java.util.function.Supplier;

/**
 * Graphical test automation is hard. So, all of our tests are automatic,
 * and of course if they blow up, great, but one still needs to do a visual
 * inspection. All tests dump output into the build/ directory.
 */
public class Test {
  public static void main(String[] args) throws Exception {
    if (args.length==0)
      testAll();
     else
      testOne(args);
  }


  private static void testAll() throws Exception {
    for (Supplier<ITest> supplier: allTests) {
      ITest test=supplier.get();
      System.out.println("Test: "+test);
      test.test();
    }
  }


  private static void testOne(String[] args) throws Exception {

    // 1) Get name of class:
    Class<?> c;
    try {
      Package thisPackage=Test.class.getPackage();
      c=Class.forName(thisPackage.getName()+"."+args[0]);
    } catch (Exception e) {
      System.err.println("\nERROR: Either provide no arguments, or the name of a class under the org.tmotte.pdfrpt.test package.");
      System.err.println("  (Attempted to create class and got: "+e+")");
      System.exit(1);return;
    }

    // 2) Pass remaining arguments to it:
    String a[]=new String[args.length-1];
    System.arraycopy(args, 1, a, 0, args.length-1);
    c.getMethod("main", String[].class).invoke(null, (Object)a);

  }

  // This avoids running the classloader for all tests and speeds up boot
  // when we only need one:
  private final static List<Supplier<ITest>> allTests=new ArrayList<>();
  static {
    // simplepdf.*:
    allTests.add(()->new TestImage());
    allTests.add(()->new TestRoundRect());

    // performance.*:
    allTests.add(()->new TestLoad());

    // report.*:
    allTests.add(()->new TestCentered());
    allTests.add(()->new TestFont());
    allTests.add(()->new TestFontSizes());
    allTests.add(()->new TestOffsets());
    allTests.add(()->new TestPImage());

    allTests.add(()->new TestPGroup());
    allTests.add(()->new TestPLine());
    allTests.add(()->new TestPRect());
    allTests.add(()->new TestPText());
    allTests.add(()->new TestPTextLines());

    allTests.add(()->new TestPTextLines2());
    allTests.add(()->new TestMultipage());
    allTests.add(()->new TestSplit());
    allTests.add(()->new TestState());
  }

}