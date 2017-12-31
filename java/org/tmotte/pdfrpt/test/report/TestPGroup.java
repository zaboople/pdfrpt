package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.item.PTextLines;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.test.ITest;
import org.tmotte.pdfrpt.test.Test;

public class TestPGroup implements ITest {

  public static void main(String[] args) throws Exception {
    new TestPGroup().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      ).setFontInfo(
        new FontInfo().adjustLineSpacing(2, 2)
      )
    ) {
      run(pdf);
    }
  }

  public void run(SimplePDF pdf) throws Exception {
    pdf.setColor(0,0,80);
    new Report(pdf)
      .addVerticalCenter(
        new PText(pdf, "I am dead in the middle top, but only because I was added first.")
      )
      .addVerticalRight(
        new PText(pdf, "I'm over on the right, man.")
        ,
        new PTextLines(
          new FontInfo().adjustLineSpacing(0.5f, 0.5f),
          "Yeah", "well I am too", "if that matters."
        )
      )
      .add(
        PGroup.HCenter, PGroup.VCenter
        ,
        new PText(pdf, "I'm dead center, baby.")
      )
      .add(
        PGroup.HLeft, PGroup.VBottom
        ,
        new PText(pdf, "I'm down here at the bottom left.")
      )
      .add(
        PGroup.HRight, PGroup.VBottom
        ,
        new PTextLines(
          new FontInfo().adjustLineSpacing(0,0),
          "I'm down here at the bottom right,", "but I have multiple", "lines."
        ).rightAlign()
      )
      .print(pdf);
  }


}