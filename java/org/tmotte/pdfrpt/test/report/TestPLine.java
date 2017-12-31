package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PLine;
import org.tmotte.pdfrpt.report.item.PRect;
import org.tmotte.pdfrpt.report.item.PSpacer;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.item.PTextLines;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

public class TestPLine implements ITest {

  public static void main(String[] args) throws Exception {
    new TestPLine().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {
      run(pdf);
    }
  }

  public void run(SimplePDF pdf) throws Exception {
    pdf.setColor(0,0,80);
    Report report=new Report(pdf);
    report.addVertical(
      new PLine(100, 0).setColor(100, 200, 220)
      ,
      new PLine(100, 0).setColor(100, 100, 220).setLineWidth(10)
      ,
      new PLine(100, 0).setColor(30, 90, 0).setLineWidth(2)
      ,
      new PLine(100, 0).setColor(200, 100, 120)
    );
    report.addHorizontal(
      new PLine(0, 100).setColor(20, 100, 40)
      ,
      new PLine(0, 100).setColor(80, 200, 10)
      ,
      new PLine(0, 100).setColor(170, 100, 10).setLineWidth(8)
      ,
      new PLine(0, 100).setColor(230, 10, 190)
    );
    report.print(pdf);
  }


}