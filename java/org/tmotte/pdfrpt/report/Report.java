package org.tmotte.pdfrpt.report;
import java.util.List;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.PageInfo;

/**
 * <b>The centerpiece of this package.</b> ReportItems are added to a Report, and then the Report is printed. 
 * A Report instance generally corresponds to a single PDF file, although
 * multiple Reports can print to a single PDF.
 * <p>The following example illustrates a straightforward use of Report:
 * </p><pre>
   SimplePDF pdf=new SimplePDF(myOutputStream);
   Report report=new Report(pdf);
   //... Add items to the report
   report.printAndClose(pdf);</pre>
 */
public class Report extends PGroup {

  ///////////////////
  // CONSTRUCTORS: //
  ///////////////////

  /**
   * Creates a new Report that uses the specified width and height for its pages. 
   */
  public Report(float pageWidth, float pageHeight) {
    super();
    setWidth(pageWidth);
    setHeight(pageHeight);
  }
  /**
   * Shortcut to <code>Report(pageInfo.getWidth(), pageInfo.getHeight())</code>; here, Report will attempt
   * to respect the margins set on <code>pageInfo</code> and provide positional information based on the space bounded
   * by those margins, not the boundaries of the page itself. 
   * @see PageInfo#setMargins(float, float, float, float)
   */
  public Report(PageInfo pageInfo) {
    this(pageInfo.getWidth(), pageInfo.getHeight());
  }
  /**
   * Shortcut to <code>Report(pdf.getPageInfo())</code>. 
   */
  public Report(SimplePDF pdf) {
    this(pdf.getWidth(), pdf.getHeight());
  }


  //////////////
  // METHODS: //
  //////////////


  /**
   * Starts a new page in the PDF file, which will be created using SimplePDF.newPage()
   * @see SimplePDF#newPage()
   */
  public Report newPage() throws Exception {
    add(newPage);
    return this;
  }
  private ReportItem newPage=new ReportItem(){
    public void print(SimplePDF pdf) throws Exception {
      pdf.newPage();
    }
  };

  /** 
   * Enhances <code>PGroup.addVertical(ReportItem...)</code> such that item is only added if it fits on the page vertically
   * below the last ReportItem added, and with enough space left over for <code>footerHeight</code>.
   * @return true if item fits, false if not.
   */
  public boolean addVerticalIfPageFits(ReportItem item, float footerHeight) {
    if (lastAdded!=null && lastAdded.getBottom()+item.getBottom()+item.getTop()+footerHeight>getHeight())
      return false;
    addVertical(item);
    return true;
  }
  /** 
   * A shortcut to <code>addVerticalIfPageFits(item, 0)</code>
   */
  public boolean addVerticalIfPageFits(ReportItem item) {
    return addVerticalIfPageFits(item, 0);
  }
  /**
   * An enhancement over <code>addVerticalIfPageFits()</code> that lumps a variety of page-management features into 
   * a single method: If <code>item</code> fits within the 
   * remaining space on the page (including the space needed for <code>footer</code>) it is added
   * via <code>addVertical(ReportItem)</code>; if not, the following takes place:<pre>
     addFooter(footer);   //Add the footer to the current page:
     newPage();           //Start a new page
     pageCount.inc();     //Increment the page counter
     addVertical(header); //Add the header to the top of the new page.</pre>
     addVertical(item);   //Now add the item as originally intended
   * <p>When using this method, the programmer is only responsible for adding a footer to the 
   * to the very <i>last</i> page of the Report. </p>
   * @param item The ReportItem to add.
   * @param pageCount (optional) A PageCount instance that should be incremented if we start a new page. 
   * @param footer (optional) A ReportItem to be added as a footer to the current page if we must start a new page.
   * @param header (optional) A ReportItem to be added as a header to the next page if we must start a new page.
   * @see #footerPrintAndClose(SimplePDF, ReportItem)
   */
  public boolean addVertical(
      ReportItem item, PageCount pageCount, ReportItem footer, ReportItem header
    ) throws Exception {
    if (!addVerticalIfPageFits(item, footer==null ?0 :footer.getHeight())){
      if (footer!=null)
        addFooter(footer);
      newPage();
      if (pageCount!=null)
        pageCount.inc();
      if (header!=null)
        addVertical(header);
      addVertical(item);
      return false;
    }
    return true;
  }

  /**
   * Adds <code>footer</code> to the bottom of the current page, setting <code>footer</code>'s top offset so it fits
   * flush with the bottom page margin, i.e. <code>footer.setTop(Report.getHeight()-footer.getHeight())</code>. Note that
   * the same <code>footer</code> instance can be added to every page, since it always receive the same height setting.
   */
  public Report addFooter(ReportItem footer) {
    footer.setTop(getHeight()-footer.getHeight());
    add(footer);
    return this;
  }

  /**
   * A shortcut to <code>Report.print()</code> and <code>pdf.close()</code>. The PDF will be complete
   * and no further action need be taken (closing OutputStreams, etc.)
   */
  public void printAndClose(SimplePDF pdf) throws Exception {
    print(pdf);
    pdf.close();
  }
  /**
   * A shortcut to <code>addFooter(footer).printAndClose(pdf)</code>.
   */
  public void footerPrintAndClose(SimplePDF pdf, ReportItem footer) throws Exception {
    addFooter(footer);
    print(pdf);
    pdf.close();
  }

}