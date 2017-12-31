package org.tmotte.pdfrpt.test.report;
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

/** This combines centered text with uncentered but well-arranged shapes */
public class TestCentered implements ITest {
  public static void main(String[] args) throws Exception {
    new TestCentered().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {

      FontInfo mainFont=new FontInfo().adjustLineSpacing(8,8);
      ReportItem waka=new PGroup(
          new PTextLines(new FontInfo().setFontSize(20), "Hi", "waka,", "waka")
            .center().setWidth(80).setTop(10).setLeft(10)
        ).addHeight(10).addWidth(10);
      new Report(pdf)
        .addVertical(
          new PText(mainFont, "Everything centered in an area 100px wide, like the line below:"),
          new PGroup().addVertical(
            new PLine(100, 0).setColor(100,200,30).setLineWidth(6).setTop(10)
            ,new PText(mainFont, 100, "Hello...").center()
            ,new PTextLines(
                mainFont, 100,
                "Um hello yes I am a big hunk of text and I'm enjoying printing myself out so nicely"
              ).center()
            ,new PLine(100, 0).setTop(1)
            ,new PRect(60,20).setColor(112,24,60).setFill().setPosition(20, 10)
            ,new PRect(20,60).setOutline().setPosition(40, 10)
            ,new PGroup(
              PGroup.Vertical
              ,new PText(new FontInfo().setFontSize(6), 100, "Whatever,").center().setTop(3)
              ,new PText(new FontInfo().setFontSize(7), 100, "Whatever").center().setTop(1)
              ,new PText(new FontInfo().setFontSize(8), 100, "What").center().setTop(1)
            )
            ,new PLine(100, 0).setLineWidth(12)
            ,new PGroup(
                new PRect(waka.getWidth(),waka.getHeight()).setFill().setColor(120, 160, 220)
              ,waka
            )
          )
          .setLeft(100)
        )
        .print(pdf);
    }
  }

}