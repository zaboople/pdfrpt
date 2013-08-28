package org.tmotte.pdfrpt.report.item;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.SimplePDF;

/** 
 * Allows for rendering of simple text data. For multi-line text, use PTextLines. <p>Notes:
 * <ul>
 *  <li>By default, text is left-aligned (left-justified).
 *  <li>Increasing the height of PText (via <code>setHeight()</code> or <code>addHeight()</code>)
 *     only affects the amount of space below the text rendered. The text is always rendered from the
 *     top/left corner. Use FontInfo to obtain more specific spacing and font sizing.
 *  <li>Setting the width of a PText has no effect on character spacing; extra space appears to the the left and/or right
 *      of the text depending on alignment.
 * </ul>
 * </p>
 */
public class PText extends AbstractText {
  private String text;

  /**
   * Constructs a PText using the specified font, width and text; also sets the height as determined
   * by <code>fontInfo.getTextLineHeight()</code>.
   * @param fontInfo Font information for rendering the text.
   * @param width The desired width of the PText element. Extra space will appear to the
   * left and/or right of this PText depending on left/right/center alignment settings.
   * @param text The text to display.
   */
  public PText(FontInfo fontInfo, float width, String text) {
    super(fontInfo);
    this.text=text;
    setWidth(width);
    setHeight(fontInfo.getTextLineHeight());
  }
  /**
   * Constructs a PText with width &amp; height equal to the width and height of <code>text</code> as determined
   * by <code>fontInfo.getWidth(text)</code> and <code>fontInfo.getTextLineHeight()</code>.
   * @param fontInfo Contains font information for rendering the text.
   * @param text The text to display.
   */
  public PText(FontInfo fontInfo, String text) {
    super(fontInfo);
    this.text=text;
    setWidth(fontInfo.getWidth(text));
    setHeight(fontInfo.getTextLineHeight());
  }


  /** Works the same as <code>PText(fontInfo, width, text)</code>. */
  public PText(float width, FontInfo fontInfo, String text) {
    this(fontInfo, width, text);
  }
  /** A shortcut to <code>PText(pdf.getFontInfo(), width, text)</code>. */
  public PText(SimplePDF pdf, float width, String text) {
    this(pdf.getFontInfo(), width, text);
  }

  /** A shortcut to <code>PText(pdf.getFontInfo(), text)</code> */
  public PText(SimplePDF pdf, String text) {
    this(pdf.getFontInfo(), text);;
  }


  /////////////////////
  // SIMPLE GET/SET: //
  /////////////////////
  
  protected String getText() {
    return text;
  }

  /**
   * Causes text to be right-aligned when printed. This only has an effect when the width of this PText
   * is set to a value greater than the width of its rendered text.
   * @see #PText(FontInfo, float, String)
   * @see #setWidth(float)
   */
  public PText rightAlign() {
    rightAlign=true;
    center=false;
    return this;
  }
  /**
   * Causes text to be horizontally centered when printed. This only has an effect when the width of this PText
   * is set to a value greater than the width of its rendered text.
   * @see #PText(FontInfo, float, String)
   * @see #setWidth(float)
   */  
   public PText center() {
    rightAlign=false;
    center=true;
    return this;
  }
  /**
   * Overrides <code>AbstractText.setPageCount()</code> for method-chaining convenience.
   * @see AbstractText#setPageCount(PageCount)
   */
  public PText setPageCount(PageCount pc) {
    super.setPageCount(pc);
    return this;
  }
  
  
  
  ////////////
  // PRINT: //
  ////////////
  
  /** 
   * Prints this PText instance. Center/right-alignment is handled here. 
   * Also, if a PageCount instance has been assigned, values for 
   * <code>PText.REPLACE_CURR_PAGE</code> and <code>PText.REPLACE_PAGE_COUNT</code> will be obtained and inserted into the text drawn.
   * @see #setPageCount(PageCount)
   */
  public void print(SimplePDF pdf) {
    pushPrint(pdf);
    
    //Build text:
    String t=text;
    if (pageCounter!=null) 
      t=replacePageVars(t);
    
    //Now print:
    if (rightAlign) 
      pdf.moveX(getRight()-getLeft())
         .drawToLeft(t);
    else
    if (center) 
      pdf.drawCentered(t, getWidth());
    else
      pdf.draw(t);

    pop(pdf);
  }

  

  
  ////////////////
  // DEBUGGING: //
  ////////////////

  /** Prints the text contents of this instance, for debugging purposes. */
  public String toString() {
    if (text!=null)
      return text;
    else 
      return "";
  }
}
