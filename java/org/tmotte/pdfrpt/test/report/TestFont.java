package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

public class TestFont implements ITest {

  public static void main(String[] args) throws Exception {
    new TestFont().test();
  }

  public @Override void test() throws Exception {
    try (
        SimplePDF pdf=new SimplePDF(
          new FileOutputStream(new File("build", getClass().getName()+".pdf")),
          new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )){
      Report report=new Report(pdf);
      FontInfo f1=new FontInfo("f1.ttf",  new File("./lib/fonts/nobile.ttf").toURI().toURL().openStream(), 100000),
              f2=new FontInfo("f2.ttf",  new File("./lib/fonts/nobile_bold.ttf").toURI().toURL()),
              f3=new FontInfo("f3.ttf",  new File("./lib/fonts/nobile_italic.ttf").toURI().toURL()),
              f4=new FontInfo("f4.ttf",  new File("./lib/fonts/nobile_bold_italic.ttf").toURI().toURL());
      report.addVertical(
        new PText(new FontInfo(f1).setFontSize(18), "This is some text in a special font.")
        ,new PText(new FontInfo(f2).setFontSize(15), "This is some text in a special font.")
        ,new PText(new FontInfo(f3).setFontSize(12), "This is some text in a special font.")
        ,new PText(f4, "This is some text in a special font.")
      );
      report.print(pdf);
    }
  }

}