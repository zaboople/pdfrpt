package org.tmotte.pdfrpt;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

/** 
 * Contains information about page size and margins, and is used by SimplePDF's constructor.  
 * @see SimplePDF#SimplePDF(java.io.OutputStream, PageInfo)
 */
public class PageInfo {

  /** 
   * A shortcut to <code>com.itextpdf.text.PageSize.LETTER</code>. 
   * @see PageSize#LETTER
   */
  public static Rectangle LETTER_PORTRAIT=PageSize.LETTER;
  /** 
   * A shortcut to <code>com.itextpdf.text.PageSize.LETTER.rotate()</code>. 
   * @see PageSize#LETTER
   */
  public static Rectangle LETTER_LANDSCAPE=PageSize.LETTER.rotate();
  /** 
   * A shortcut to <code>com.itextpdf.text.PageSize.LEGAL</code>. 
   * @see PageSize#LEGAL
   */
  public static Rectangle LEGAL_PORTRAIT=PageSize.LEGAL;
  /** 
   * A shortcut to <code>com.itextpdf.text.PageSize.LEGAL.rotate()</code>. 
   * @see PageSize#LEGAL
   */
  public static Rectangle LEGAL_LANDSCAPE=PageSize.LEGAL.rotate();

  Rectangle pageSize;
  float marginTop, marginRight, marginBottom, marginLeft, useableWidth, useableHeight;


  /**
   * Creates a new PageInfo with page sized according to <code>pageSize</code> and with the specified top/right/bottom/left
   * margin values.
   * @param pageSize Specifies the page height/width. 
   *    PageInfo's LETTER_PORTRAIT, LETTER_LANDSCAPE, LEGAL_PORTRAIT and LEGAL_LANDSCAPE variables are convenient 
   *    shortcuts to common settings; see iText's PageSize class itself for more.
   */
  public PageInfo(Rectangle pageSize, float topMargin, float rightMargin, float bottomMargin, float leftMargin) {
    setPageSize(pageSize);
    setMargins(topMargin, rightMargin, bottomMargin, leftMargin);
  }
  /** A shortcut to <code>PageInfo(pageSize, margin, margin, margin, margin)</code> */
  public PageInfo(Rectangle pageSize, float margin) {
    this(pageSize, margin, margin, margin, margin);
  }
  /** A shortcut to <code>PageInfo(pageSize, 0, 0, 0, 0)</code> */
  public PageInfo(Rectangle pageSize) {
    this(pageSize, 0, 0, 0, 0);
  }
  /** Creates a new instance of PageInfo that has the same properties as <code>other</code>. */
  public PageInfo(PageInfo other) {
    this(other.getPageSize(), other.marginTop, other.marginRight, other.marginBottom, other.marginLeft);
  }

  /** A shortcut to <code>PageInfo(LETTER_PORTRAIT, 0, 0, 0, 0)</code> */
  public PageInfo() {
    this(LETTER_PORTRAIT);
  }

  /** Changes the size of the page. */
  public PageInfo setPageSize(Rectangle pageSize) {
    this.pageSize=pageSize;
    recalc();
    return this;
  }
  /** Changes the page margins. */
  public PageInfo setMargins(float top, float right, float bottom, float left) {
    this.marginTop=top;
    this.marginRight=right;
    this.marginBottom=bottom;
    this.marginLeft=left;    
    recalc();
    return this;
  }
  /** Obtains height of bottom margin.*/
  public float getMarginBottom(){
    return marginBottom;
  }
  /** Obtains height of top margin.*/
  public float getMarginTop(){
    return marginTop;
  }
  /** Obtains width of left margin.*/
  public float getMarginLeft(){
    return marginLeft;
  }
  /** Obtains width of right margin.*/
  public float getMarginRight(){
    return marginRight;
  }
  /** Gets the useable width, i.e. the distance between left and right margins */
  public float getWidth() {
    return useableWidth;
  }
  /** Gets the useable height, i.e. the distance between top and bottom margins */
  public float getHeight() {
    return useableHeight;
  }
  /** Gets the total width of the page, including margin space.*/
  public float getTotalWidth() {
    return pageSize.getWidth();
  }
  /** Gets the total height of the page, including margin space.*/
  public float getTotalHeight() {
    return pageSize.getHeight();
  }
  /** Gets the internal iText Rectangle that represents the actual page dimensions; alternatively, 
      <code>getTotalWidth()</code> and <code>getTotalHeight()</code> can be used. */
  public Rectangle getPageSize() {
    return pageSize;
  }
  private void recalc() {
    useableWidth=pageSize.getWidth()-(marginLeft+marginRight);
    useableHeight=pageSize.getHeight()-(marginTop+marginBottom);
  }
}