package org.tmotte.pdfrpt.test.report;
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

public class TestPTextLines2 {


  public static void main(String[] args) throws Exception {
    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
    );      
    Report report=new Report(pdf);
    {
      report.addVertical(new PText(pdf, "TEST: Text too long to fit on line, various line sizes:"));
      PText header=new PText(pdf, "TEST continued...");
      for (float lineWidth=50; lineWidth<200; lineWidth+=3) {
        ReportItem lines=
          new PTextLines(
            pdf, lineWidth, 
            "hello 01234567890123456789012345678901234 yes something besides "
           +" 5678901234567890123456789012345678901234567890123456789012345678901234567890 no"
          ).setTop(1).setLeft(1).addHeight(0).addWidth(1);
        ReportItem all=new PGroup(
          lines, new PRect(lines.getWidth(), lines.getHeight())
        ).setTop(1);
        report.addVertical(all, null, null, header);
      }
    }

    report.printAndClose(pdf);
  }
  
}