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

public class TestFont {

  public static void main(String[] args) throws Exception {
    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
    );      
    Report report=new Report(pdf);
    FontInfo f1=new FontInfo("f1.ttf",  new File("./lib/fonts/nobile.ttf").toURL().openStream(), 100000),
             f2=new FontInfo("f2.ttf",  new File("./lib/fonts/nobile_bold.ttf").toURL()),
             f3=new FontInfo("f3.ttf",  new File("./lib/fonts/nobile_italic.ttf").toURL()),
             f4=new FontInfo("f4.ttf",  new File("./lib/fonts/nobile_bold_italic.ttf").toURL());    
    report.addVertical(
       new PText(new FontInfo(f1).setFontSize(18), "This is some text in a special font.")
      ,new PText(new FontInfo(f2).setFontSize(15), "This is some text in a special font.")
      ,new PText(new FontInfo(f3).setFontSize(12), "This is some text in a special font.")
      ,new PText(f4, "This is some text in a special font.")
    );      
    report.printAndClose(pdf);
  }

}