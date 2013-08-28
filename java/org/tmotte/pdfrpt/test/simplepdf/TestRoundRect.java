package org.tmotte.pdfrpt.test.simplepdf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.PageInfo;

/** Tests image rendering. */
public class TestRoundRect {
  public static void main(String[] args) throws Exception {
    SimplePDF pdf=
      new SimplePDF(new FileOutputStream(args[0]), PageInfo.LETTER_PORTRAIT)
        .setMargins(25,25,25,25);      

    pdf.setXY(50,50);
    pdf.drawRectRounded(300,300, 30, 20, 30, 20);
    pdf.drawRectRounded(100,100, 10, 2, 3, 2);
    pdf.close();

  }
}