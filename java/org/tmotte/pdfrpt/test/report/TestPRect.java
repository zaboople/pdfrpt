package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PRect;
import org.tmotte.pdfrpt.report.item.PSpacer;
import org.tmotte.pdfrpt.report.item.PText;
import org.tmotte.pdfrpt.report.item.PTextLines;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.report.Report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

public class TestPRect {

  public static void main(String[] args) throws Exception {
    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
    );      
    new TestPRect().run(pdf);
    pdf.close();
  }
  
  
  public void run(SimplePDF pdf) throws Exception {
    java.util.Random ran=new java.util.Random(System.currentTimeMillis());
    pdf.setColor(0,0,80);
    Report report=new Report(pdf);
    report
      .addHorizontal(
        new PRect()
          .enclose(10, new PText(new FontInfo(pdf.getFontInfo()).setColor(0,0,0), "Hm?"))
        ,
        new PRect()
          .enclose(
            20, 10, 20, 10, 
            new PTextLines(pdf, "What", "Super duper duper", "Wib wib wib", "howdy")
          )
          .addLeft(-1)
      );
    report.addHorizontal(
      new PRect().setFill(200, 200, 170).setRounded(2).enclose(
        3,3,18,9,
        new PRect().setFill(100,200,220).enclose(
          9,23,3,15,
          new PRect().setFill(100,50,75).enclose(
            10,10,10,10,
            new PRect().setFill(30,60,100).enclose(
              20,20,20,20,
              new PRect(30, 30).setFill(20, 100, 20).setRounded(4, 3, 2, 1)
            )
          )
        )
      )
    );
    report.addVertical(
      new PGroup(
        PGroup.Horizontal
        ,
        new PRect()
          .setFill(230, 200, 160)
          .setRounded(3, 3, 3, 3)
          .enclose(
            6,
            new PRect()
              .setColor(250, 250, 0)
              .setLineWidth(7)
              .enclose(
                new PTextLines(
                  new FontInfo().setFontSize(8).adjustLineSpacing(0, 0), 180, 
                  "Here is a lot of text that needs to be broken up so it will fit in a reasonable space, I guess; or maybe"
                 +" that will never work out and we'll just be stuck with text going all over"
                )
              )
          )
        ,
        new PGroup(
          PGroup.Vertical
          ,new PRect(100, 30).setColor(255, 0, 0).setLineWidth(2)
          ,new PRect(100, 60).setColor(255, 100, 0).setLineWidth(5)
          ,new PRect(100, 70).setColor(155, 200, 0).setLineWidth(15).setRounded(2)
          ,new PRect(100, 10).setColor(0, 255, 0)
          ,new PRect(100, 10).setColor(0, 0, 255)
        )
        .setLeft(1)
      ).setTop(10)
    );
    report.addVertical(
      new PGroup(
        PGroup.Horizontal
        ,
        new PGroup(
          PGroup.Vertical
          ,new PRect(100, 10).setFill(255, 0, 0)
          ,new PSpacer().setHeight(1)
          ,new PRect(100, 10).setFill(0, 255, 0)
          ,new PSpacer().setHeight(1)
          ,new PRect(100, 10).setFill(0, 0, 255)
        )
        ,
        new PSpacer().setWidth(10)
        ,
        new PGroup(
          PGroup.Vertical
          ,new PRect(60, 30).setFill(155, 30, 0).setRounded(1.3f)
          ,new PSpacer().setHeight(1)
          ,new PRect(60, 30).setFill(0, 155, 0).setRounded(2.6f)
          ,new PSpacer().setHeight(1)
          ,new PRect(60, 40).setFill(150, 148, 16).setRounded(4)
        )
      ).setTop(10)
    );
    report.print(pdf);
  }
  

}