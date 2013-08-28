package org.tmotte.pdfrpt.test;

public class Test {
  public static void main(String[] args) throws Exception {
    if (args.length==0){
      System.err.println("\nERROR: First argument needs to be the name of a class in the org.tmotte.pdfrpt.test package.");
      System.exit(1);return;
    }
    Class c;
    try {
      c=Class.forName(Test.class.getPackage().getName()+"."+args[0]);
    } catch (Exception e) {
      System.err.println("\nERROR: First argument needs to be the name of a class in the org.tmotte.pdfrpt.test package.");
      System.exit(1);return;      
    }
    String a[]=new String[args.length-1];
    System.arraycopy(args, 1, a, 0, args.length-1);
    c.getMethod("main", String[].class).invoke(null, (Object)a);
  }
}