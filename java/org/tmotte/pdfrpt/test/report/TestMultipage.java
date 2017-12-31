package org.tmotte.pdfrpt.test.report;
import java.io.File;
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
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

public class TestMultipage implements ITest {

  public static void main(String[] args) throws Exception {
    new TestMultipage().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 45)
      )){
      test(pdf);
    }
  }

  private void test(SimplePDF pdf) throws Exception {
    java.util.Random randomizer=new java.util.Random(System.currentTimeMillis());
    int recordCount=1000;

    Report report=new Report(pdf.getWidth(), pdf.getHeight());
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

    report.addVertical(header);
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

    report.addFooterAndPrint(footer, pdf);
  }


}