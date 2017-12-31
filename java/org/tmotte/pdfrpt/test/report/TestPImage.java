package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PImage;
import org.tmotte.pdfrpt.report.item.PRect;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

/** Tests image rendering. */
public class TestPImage  implements ITest {

  public static void main(String[] args) throws Exception {
    new TestPImage().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {
      Report report=new Report(pdf);
      URL fileURL=new java.io.File("./lib/images/test2.jpg").toURI().toURL();

      PImage img=new PImage(fileURL);
      report.addVertical(new PRect().setLineWidth(4).setColor(180, 90, 20).enclose(1, img));

      PImage img2=new PImage(img).addHeight(-120);
      report.addVertical(new PRect().setLineWidth(3).setColor(100, 200, 50).enclose(1, img2).setTop(4));

      PImage img3=new PImage(img).resize(20, 20);
      report.addVertical(new PRect().setLineWidth(2).setColor(40, 80, 180).enclose(1, img3).setTop(4));

      PImage img4=new PImage(img).setHeight(120).setWidth(120);
      report.addVertical(new PRect().setLineWidth(2).setColor(40, 80, 180).enclose(1, img4).setTop(4));

      report.print(pdf);
    }

  }
}