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

public class TestPTextLines implements ITest {
  public static void main(String[] args) throws Exception {
    new TestPTextLines().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {

      Report report=new Report(pdf);
      PageCount pc=new PageCount();

      PGroup between=new PGroup(
        PGroup.Horizontal,
        new PSpacer().setLeft(10),
        new PLine(0, 25),
        new PSpacer().setWidth(10)
      );
      PGroup firstRow=new PGroup(
        PGroup.Horizontal
        ,new PTextLines(pdf, "Hello how are", "you - ", "I am ok,", "I guess maybe.").rightAlign()
        ,new PGroup(between)
        ,new PTextLines(pdf, "I am in the", " middle, therefore", "centered").center()
        ,new PGroup(between)
        ,new PTextLines(pdf, "Here is another", "chunk", "of boring", "text")
      );
      report.addVertical(
        firstRow
        ,
        new PTextLines(
            new FontInfo().adjustLineSpacing(0.5f, 10), firstRow.getWidth(),
            "And so I am just the long, long, bunch of text going on and on for way more than I need to,"
          +" not just because I can, but because I'm kind of a jerk, and also because I'm trying to "
          +" test \"mission-critical\" functionality in this silly report. Thank you."
          )
          .setTop(15)
        ,
        new PTextLines(
            new FontInfo().setFontSize(14), firstRow.getWidth(),
            "-Do a text break here:\r\n"
          +"-And then, after a good long while, you know, lagging along, eventually do a break here:\r"
          +"-And then, another here:\n"
          +"-What the heck, let's do one here, but I'd like to keep it lengthy and annoying and blah blah, "
          +"well, shoot, maybe yes, maybe no, maybe whatever, but okay here:\r"
          +"-And finally here:\n\r"
          )
          .setTop(10)
        ,
        new PTextLines(
          pdf,
          "And finally I would like to ",
        "do page "+PTextLines.REPLACE_CURR_PAGE,
        "of "+PTextLines.REPLACE_PAGE_COUNT+"."
        ).setPageCount(pc)
        .setTop(10)
      );

      report.print(pdf);
    }
  }

}