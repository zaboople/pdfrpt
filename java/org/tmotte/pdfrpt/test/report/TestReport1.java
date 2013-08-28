package org.tmotte.pdfrpt.test.report;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PLine;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

public class TestReport1 {

  public static void main(String[] args) throws Exception {
    java.util.Random randomizer=new java.util.Random(System.currentTimeMillis());
    int recordCount=1000;

    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 45)
    );      
    Report report=new Report(pdf);
    PageCount pageCount=new PageCount();
    ReportItem 
      footer=
        new PText(pdf, pdf.getWidth(), "Page "+PText.REPLACE_CURR_PAGE+" of "+PText.REPLACE_PAGE_COUNT)
          .rightAlign()
          .setPageCount(pageCount)
          .setTop(5)
      ,
      header=new PGroup().addVertical(
        new PText(pdf, "TEST REPORT 1 - page "+PText.REPLACE_CURR_PAGE+" of "+PText.REPLACE_PAGE_COUNT)
          .setPageCount(pageCount)
        ,
        new PLine(pdf.getWidth(), 0).setHeight(4).setTop(3)
      );
      
    report.add(header);
    for (int i=0; i<recordCount; i++) {

      //First, a group of vertical items:
      int count=randomizer.nextInt(4)+1;
      PGroup record=new PGroup();
      for (int a=0; a<count; a++)
        record.addVertical(new PText(pdf, "Hi there "+i+"."+a+"v"));

      //Second, a group of horizontal items:
      PGroup hor=new PGroup()
        .add(new PText(pdf, "-----"));
      for (int a=1; a<=3; a++)
        hor.addHorizontal(
          new PText(pdf, 149, ""+randomizer.nextInt(1000000000)).rightAlign()
        );
      record.addVertical(
        hor, new PLine(pdf.getWidth(), 0).setTop(1).setHeight(4)
      );
      
      //Add to report, new page/footer/header as necessary:
      report.addVertical(record, pageCount, footer, header);
    }

    report.footerPrintAndClose(pdf, footer);
  }
 
 
}