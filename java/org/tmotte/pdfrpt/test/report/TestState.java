package org.tmotte.pdfrpt.test.report;
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

public class TestState implements ITest {

  public static void main(String[] args) throws Exception {
    new TestState().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {
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

      report.print(pdf);
    }
  }
}