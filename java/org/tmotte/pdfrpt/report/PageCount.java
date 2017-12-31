package org.tmotte.pdfrpt.report;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

/**
 * Useful for page numbering, e.g. "Page <i>2</i> of <i>30</i>". Example:
 * <pre>
    try (SimplePDF pdf=new SimplePDF(&#0047;*an OutputStream*&#0047;)) {
      Report report=new Report(pdf);

      //Create page count instance:
      PageCount pageCount=new PageCount();

      //Create footer & header that use PageCount:
      ReportItem
        header=new PText(pdf, "[Header] Page "+PText.REPLACE_CURR_PAGE+" of "+PText.REPLACE_PAGE_COUNT)
          .setPageCount(pageCount)
        ,
        footer=new PText(pdf, "[Footer] Page "+PText.REPLACE_CURR_PAGE+" of "+PText.REPLACE_PAGE_COUNT)
          .setPageCount(pageCount);

      //Add items to the Report, incrementing page count and printing the footer/header as necessary:
      report.add(header);
      for (int i=0; i<1000; i++){
        report.addVertical(new PText(pdf, ""+i), pageCount, footer, header);

      //Finish:
      report.addFooterAndPrint(footer, pdf);
    }
   </pre>
 * @see org.tmotte.pdfrpt.report.item.PText#setPageCount(PageCount)
 * @see org.tmotte.pdfrpt.report.item.PTextLines#setPageCount(PageCount)
 * @see Report#addVertical(ReportItem, PageCount, ReportItem, ReportItem)
 */
public class PageCount {
  protected int page=1;

  /**
   * Increments the internal page count, which starts at 1.
   */
  public int inc() {
    return ++page;
  }
  /**
   * Gets the total number of pages.
   */
  public int getTotal() {
    return page;
  }
}