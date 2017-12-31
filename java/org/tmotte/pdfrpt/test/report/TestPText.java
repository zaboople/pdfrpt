package org.tmotte.pdfrpt.test.report;
import com.itextpdf.text.Image;
import java.io.File;
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
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

public class TestPText implements ITest {

  public static void main(String[] args) throws Exception {
    new TestPText().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {

      FontInfo font=new FontInfo().adjustLineSpacing(.5f, .5f);
      Report report=new Report(pdf);
      PageCount pc=new PageCount();


      FontInfo[] fonts={
        new FontInfo(font, 6).setColor(randomC(), randomC(), randomC()),
        new FontInfo(font, 7).setColor(randomC(), randomC(), randomC()),
        new FontInfo(font, 8).setColor(randomC(), randomC(), randomC()),
        new FontInfo(font, 9).setColor(randomC(), randomC(), randomC()),
        new FontInfo(font, 10).setColor(randomC(), randomC(), randomC()),
      };
      ReportItem footer=new PGroup(
        new PLine(pdf.getWidth(), 0).setColor(244, 220, 200)
        ,new PText(new FontInfo(font, 9), "/Page "+PText.REPLACE_CURR_PAGE+" "+FontInfo.allChars).setPageCount(pc)
      );
      java.util.Random random=new java.util.Random(System.currentTimeMillis());
      for (int i=0; i<10000; i++)
        report.addVertical(
          new PText(fonts[random.nextInt(4)], "MgyP"+i),
          pc,
          footer,
          null
        );
      report.addFooterAndPrint(footer, pdf);
    }
  }

  static java.util.Random random=new java.util.Random(System.currentTimeMillis());
  private static int randomC(){
    return random.nextInt(255);
  }
}