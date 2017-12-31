package org.tmotte.pdfrpt.test.performance;
import com.itextpdf.text.BaseColor;
import java.io.File;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PLine;
import org.tmotte.pdfrpt.report.item.PRect;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.item.PTextLines;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

/** Provides a load test */
public class TestLoad implements ITest {

  /** Command-line driver to allow whatever kind of load we like. */
  public static void main(String[] args) throws Exception {
    System.out.println("\nStarting");
    new TestLoad().test(
      args.length >0 ?Integer.parseInt(args[0]) :1,
      args.length >1 ?Integer.parseInt(args[1]) :1000,
      true
    );
    System.out.println("\nFinished");
  }

  /** Provides automated verification that this test still basically works. */
  public @Override void test() throws Exception {
    test(1, 1000, false);
  }

  public void test(int iterations, int recordCount, boolean track) throws Exception {
    for (int i=0; i<iterations; i++)
      try (
        SimplePDF pdf=new SimplePDF(
          new FileOutputStream(new File("build", getClass().getName()+".pdf")),
          new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
        )
      ) {
        makeReport(pdf, recordCount);
        if (track)
          System.out.println(" Report "+String.valueOf(i)+" finished.");
      }
  }



  // THE ACTUAL REPORT:
  float colSize1=65, colSize2=65, colSize3=65, colSizeSpacer=20, colSize4;
  FontInfo fontReg, fontBold, fontItal;
  BaseColor companyColor=new BaseColor(110, 110, 217),
            headerBackground=new BaseColor(220, 220, 180),
            headerForeground=new BaseColor(230, 230, 100);

  public void makeReport(SimplePDF pdf, int recordCount) throws Exception {
    java.util.Random ran=new java.util.Random(System.currentTimeMillis());

    fontReg =new FontInfo("Test2Reg.ttf",    new File("./lib/fonts/nobile.ttf").toURI().toURL())
      .setFontSize(9).adjustLineSpacing(-0.5f, -0.5f);
    fontBold=new FontInfo("Test2Bold.ttf",   new File("./lib/fonts/nobile_bold.ttf").toURI().toURL()).setFontSize(9);
    fontItal=new FontInfo("Test2Italic.ttf", new File("./lib/fonts/nobile_italic.ttf").toURI().toURL()).setFontSize(9);
    pdf.setFontInfo(fontReg);
    colSize4=pdf.getWidth()-(3+colSize1+colSize2+colSize3+colSizeSpacer);

    PageCount pageCount=new PageCount();
    ReportItem otherHeader=makePageHeader(pdf, true),
               footer=getFooter(pdf, pageCount);
    Report report=new Report(pdf);
    report.add(makePageHeader(pdf, false));
    for (int i=0; i<recordCount; i++) {
      ReportItem record=new PGroup(
        PGroup.Horizontal
        ,new PText(fontReg, colSize1, ""+i).setLeft(3)
        ,new PText(fontReg, colSize2, ""+getRandom(ran))
        ,new PText(fontReg, colSize3, ""+getRandom(ran)).rightAlign()
        ,new PTextLines(fontReg, colSize4, getParagraph(ran))
          .setLeft(colSizeSpacer)
      )
      .setTop(3);
      report.addVertical(record, pageCount, footer, otherHeader);
    }
    report.addFooterAndPrint(footer, pdf);
  }

  private String getParagraph(java.util.Random ran) {
    StringBuilder sb=new StringBuilder();
    sb.append(">");
    int count=ran.nextInt(7)*ran.nextInt(7)*ran.nextInt(7);
    for (int j=0; j<count; j++) {
      sb.append(j>0 ?" " :"");
      sb.append(String.valueOf(getRandom(ran)));
    }
    sb.append("<");
    return sb.toString();
  }

  private int getRandom(java.util.Random ran) {
    return ran.nextInt(100)*ran.nextInt(1000)* ran.nextInt(10000);
  }
  PGroup makePageHeader(SimplePDF pdf, boolean small) {
    FontInfo titleF =new FontInfo(fontBold, small ?9 :17),
             periodF=new FontInfo(fontReg, small ?9 :10);
    PText
      title=
        new PText(titleF, pdf.getWidth(), "Big Smokin' Report")
          .rightAlign(),
      period=
        new PText(periodF,  pdf.getWidth(), "Period: Jan-Mar 2017")
          .rightAlign();

    return new PGroup()
      .add(
        new PText(new FontInfo(fontBold, 7).setColor(companyColor), pdf.getWidth(), "Very Big Corporation Inc.").center()
        ,title
      )
      .addVertical(
        period
        ,new PLine(pdf.getWidth(), 0).setColor(companyColor).setTop(2).setHeight(1)
        ,makeSecondLevelHeader(pdf).setTop(1)
      );
  }


  private ReportItem makeSecondLevelHeader(SimplePDF pdf) {
    FontInfo headerFont=new FontInfo(fontBold, 12);
    ReportItem[] cols={
      new PText(headerFont, colSize1, "Column 1").setLeft(3),
      new PText(headerFont, colSize2, "Column 2"),
      new PText(headerFont, colSize3, "Column 3").rightAlign(),
      new PText(headerFont, colSize4, "Comments").setLeft(colSizeSpacer)
    };
    final float h=cols[0].getHeight()+4;
    return new PGroup(
        new PRect(pdf.getWidth(), h).setFill()
         .setColor(headerBackground)
       ,new PRect(pdf.getWidth(), h).setOutline()
         .setColor(headerForeground)
       ,new PGroup(PGroup.Horizontal ,cols)
         .setTop(3)
     )
     .setHeight(h+3);
  }


  private ReportItem getFooter(SimplePDF pdf, final PageCount pageCount) {
    return new PGroup(
      PGroup.Vertical,
      new PLine(pdf.getWidth(), 0)
        .setColor(companyColor)
        .setHeight(6)
      ,
      new PText(
         new FontInfo(fontItal, 7).setColor(companyColor), pdf.getWidth(),
         "Page "+PText.REPLACE_CURR_PAGE+" of "+PText.REPLACE_PAGE_COUNT
       ).setPageCount(pageCount).rightAlign()
    );
  }

}