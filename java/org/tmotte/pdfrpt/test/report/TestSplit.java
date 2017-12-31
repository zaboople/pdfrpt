package org.tmotte.pdfrpt.test.report;
import java.io.File;
import java.io.FileOutputStream;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.PageInfo;
import org.tmotte.pdfrpt.report.item.PLine;
import org.tmotte.pdfrpt.report.item.PRect;
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

public class TestSplit implements ITest {

  public static void main(String[] args) throws Exception {
    new TestSplit().test();
  }

  public @Override void test() throws Exception {
    try (
      SimplePDF pdf=new SimplePDF(
        new FileOutputStream(new File("build", getClass().getName()+".pdf")),
        new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
      )) {
      pdf.getFontInfo().setFontSize(15);
      Report report=new Report(pdf);


      { //TEST 1
        FontInfo line1Font=new FontInfo().setFontSize(13),
                restOfFont=new FontInfo().setFontSize(9);
        float gap=40, lineWidth=180;
        String[] myString=line1Font.split(
          lineWidth-gap,
          "Hello, this is a very long line of semi-coherent text that makes me hurl. What do you think of that? Ha ha, ha whatever. "
          +"Yet - well, I had this idea, kind of a bad one, but an idea, you know, that people would not care about the nature of "
          +"how extrapolations perform excitedly at such high frequencies, and just accept internal fluctuation as the status quo."
        );


        //Create a group with the paragraph:
        ReportItem parGroup=new PGroup(
          new PGroup(
            PGroup.Vertical
          ,new PGroup(
              PGroup.Horizontal
              ,new PLine(gap-5, 0).setTop(4).setWidth(gap)
              ,new PText(line1Font, lineWidth-gap, myString[0])
            )
            ,
            new PTextLines(restOfFont, lineWidth, myString[1])
          ).setTop(5).setLeft(5).addHeight(5).addWidth(5) //FIXME set padding?
        );

        //Now do the whole thing:
        report.addVertical(
          new PText(pdf,
            "TEST 1: Everything fits within "+lineWidth+" px, but first line starts with a "
            +gap+"px gap:"
          )
        ,new PSpacer().setHeight(5)
        ,new PGroup(
            parGroup,
            new PRect(parGroup.getWidth(), parGroup.getHeight())
          )
        );
      }
      {
        float lineWidth=70;
        report.addVertical(
          new PTextLines(
            pdf
          ,"TEST 2: We will attempt to split very long words at "+lineWidth+"px"
          ,"Line below me is "+lineWidth+"px wide"
          ).setTop(40)
          ,
          new PLine(lineWidth, 0).setTop(5).setHeight(5)
        );

        FontInfo nextFont=new FontInfo().setFontSize(15);
        String[] myString=nextFont.split(
          "GABaBaDaFeLePu AHAHAHAHAHAHAHAH FAFAAFAFAFAFAFAFAFAFAFAF"
          ,lineWidth
        );
        report.addVertical(
          new PTextLines(nextFont, myString[0], myString[1])
        );
      }
      report.print(pdf);
    }
  }

}