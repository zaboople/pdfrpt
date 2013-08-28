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

public class TestFontSizes {

  public static void main(String[] args) throws Exception {
    java.util.Random random=new java.util.Random(System.currentTimeMillis());

    SimplePDF pdf=new SimplePDF(
      new FileOutputStream(args[0]), 
      new PageInfo(PageInfo.LETTER_PORTRAIT, 25)
    );      
    Report report=new Report(pdf);
    
    PLine theLine=new PLine(pdf.getWidth(), 0).setColor(220, 220, 220).setLineWidth(1.5f);
    
    int biggestFont=30;
    FontInfo[] fontSizes=new FontInfo[biggestFont+1];
    FontInfo adjustTo=new FontInfo().setFontSize(biggestFont).adjustLineSpacing(3, 4);
    for (int i=1; i<fontSizes.length; i++)
      fontSizes[i]=new FontInfo()
        .setFontSize(i)
        .alignBaselineTo(adjustTo)
        .setColor(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    
    report.addVertical(new PGroup(theLine));
    while (true) {
      int q=0;
      PText[] items=new PText[7];
      for (int z=0; z<items.length; z++) 
        items[z]=new PText(fontSizes[biggestFont-random.nextInt(24)], "ghetto! ");
      boolean success=
        report.addVerticalIfPageFits(
          new PGroup(
            PGroup.Vertical, 
            new PGroup(PGroup.Horizontal, items), 
            new PGroup(theLine)
          )
        );
      if (!success)
        break;
    }
    report.printAndClose(pdf);
  }

}