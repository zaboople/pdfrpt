package org.tmotte.pdfrpt.report.item;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.SimplePDF;

/**
 * Acts as a base class for shared functionality in PText and PTextLines. This class is mainly exposed for documentation purposes,
 * since it is not particularly useful for the typical programmer.
 */
public abstract class AbstractText extends ReportItem {

  /** 
   * Embed this String in the text of a PText or PTextLines instance and it will be replaced with the total 
   * page count for the report. This requires the use of a PageCount object. 
   * @see #setPageCount(PageCount)
   */
  public final static String REPLACE_PAGE_COUNT="##PageCount##";
  /** 
   * Embed this String in the text of a PText or PTextLines instance and it will be replaced with the current page number. 
   * Note that this relies on an internal AbstractText counter that increments every time the instance is printed; as a result, 
   * it only works properly when the same AbstractText is placed <i>exactly once</i> on every page of a Report (use multiple
   * instances to put page numbers in multiple places on a page). 
   * <p>Also, <code>REPLACE_CURR_PAGE</code> has no effect unless a PageCount object has been assigned to this instance, 
   * even though PageCount is not relied on for this functionality; admittedly, this is strange, but it helps us avoid
   * the need to check every instance for the use of <code>REPLACE_CURR_PAGE</code>.
   * </p>
   *  @see #setPageCount(PageCount)
   */
  public final static String REPLACE_CURR_PAGE ="##CurrentPage##";

  protected FontInfo fontInfo;
  protected boolean rightAlign, 
                    center;
  protected PageCount pageCounter;
  protected short currPage=0;

  protected AbstractText(FontInfo fontInfo) {
    this.fontInfo=fontInfo;
  }

  ////////////
  // PRINT: //
  ////////////
  
  protected void print(SimplePDF pdf, String t) {
    if (rightAlign) 
      pdf.drawToLeft(t);
    else
    if (center) 
      pdf.drawCentered(t, getWidth());
    else
      pdf.draw(t);
  }
  protected String replacePageVars(String t) {
    int x;
    x=t.indexOf(REPLACE_PAGE_COUNT);
    if (x>-1) 
      t=t.replace(REPLACE_PAGE_COUNT, String.valueOf(pageCounter.getTotal()));
    x=t.indexOf(REPLACE_CURR_PAGE);
    if (x>-1) 
      t=t.replace(REPLACE_CURR_PAGE, String.valueOf(++currPage));
    return t;
  }

  /** 
   * Sets the PageCount instance for this instance. For convenience this method is overridden by both PText and PTextLines.
   * @see #REPLACE_PAGE_COUNT
   * @see #REPLACE_CURR_PAGE
   */
  public AbstractText setPageCount(PageCount pc) {
    pageCounter=pc;
    return this;
  }
  

  ////////////////
  // INTERNALS: //
  ////////////////
  

 
  protected final void pushPrint(SimplePDF pdf) {
    pdf.saveState();
    pdf.setFontInfo(fontInfo);
  }
  protected static void pop(SimplePDF pdf) {
    pdf.restoreState();
  }
  
  
}
