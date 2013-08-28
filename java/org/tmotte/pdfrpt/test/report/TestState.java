package org.tmotte.pdfrpt.test.report;
import java.io.FileOutputStream;
import java.net.URL;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PLine;
import org.tmotte.pdfrpt.report.item.PSpacer;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.item.PTextLines;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

public class TestState {

  static java.util.Random random=new java.util.Random(System.currentTimeMillis());
  public static void main(String[] args) throws Exception {
    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
    );      
    FontInfo mainFont=pdf.getFontInfo().adjustLineSpacing(.5f, .5f).setColor(100, 155, 255);
    Report report=new Report(pdf);
    
    report.addVertical(new PText(new FontInfo().setFontSize(10), "Hi there"));
    report.addVertical(new PText(new FontInfo().setColor(120, 70, 30), "Hi there"));
    report.addVertical(new PText(mainFont, "Hi there"));    
    report.addVertical(new PText(mainFont, "Hi there"));    
    report.addVertical(new PText(new FontInfo().setColor(120, 70, 30).setFontSize(9), "Hi there"));
    report.addVertical(new PText(mainFont, "Hi there"));    
    report.addVertical(new PText(new FontInfo(), "Hi there"));    
    report.newPage();
    report.addVertical(new PText(new FontInfo(), "Hi there"));    
    report.addVertical(new PText(mainFont, "Hi there"));    
    
    report.printAndClose(pdf);
  }
  private static int randomC(){
    return random.nextInt(255);
  }
}