package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

public class TestOffsets {

  public static void main(String[] args) throws Exception {
    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
    );      
    new TestOffsets().run(pdf);
    pdf.close();
  }
  
  
  public void run(SimplePDF pdf) throws Exception {
    java.util.Random ran=new java.util.Random(System.currentTimeMillis());
    pdf.setColor(0,0,80);
    Report report=new Report(pdf);
    report
      .addHorizontal(
        new PText(pdf, "Hm?").setLeft(10),
        new PText(pdf, "What").setLeft(10)
      )
      .addVertical(
        new PText(pdf, "Hello"),
        new PText(pdf, "Again").setTop(12),
        new PText(pdf, "Again and Again").setTop(40).setLeft(10)
      )
      .addHorizontal(
        new PText(pdf, "Spaces before me").setTop(3).setLeft(4)
      );
    report.print(pdf);
  }
  

}