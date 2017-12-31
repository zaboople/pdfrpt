package org.tmotte.pdfrpt.test.simplepdf;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

public class TestRoundRect implements ITest {

  public static void main(String[] args) throws Exception {
    new TestRoundRect().test();
  }

  public @Override void test() throws Exception {
    FileOutputStream output=new FileOutputStream(new File("build", getClass().getName()+".pdf"));
    SimplePDF pdf=
      new SimplePDF(output, PageInfo.LETTER_PORTRAIT)
        .setMargins(25,25,25,25);

    pdf.setXY(50,50);
    pdf.drawRectRounded(300,300, 30, 20, 30, 20);
    pdf.drawRectRounded(100,100, 10, 2, 3, 2);
    pdf.close();

  }
}