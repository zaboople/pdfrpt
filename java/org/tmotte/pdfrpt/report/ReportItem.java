package org.tmotte.pdfrpt.report;
import org.tmotte.pdfrpt.SimplePDF;
import com.itextpdf.text.pdf.BaseFont;

/** 
 * <p>
 * The generic base class for report components. A ReportItem always has a top and left position, which 
 * allow it to be positioned relative to the top/left of its container (PGroup); a top/left of {0,0} 
 * means that the ReportItem's top/left will align against the top &amp; and left corner of its container. Additionally,
 * a ReportItem can have a specific height and width, although technically this is only necessary when using PGroup's "add" methods
 * to position multiple items next to one another in a PGroup.</p>
 * <p>ReportItem is designed to be easily overridden to implement custom report components; one need only implement
 * <code>ReportItem.print()</code> to do so, although this requires some understanding of the SimplePDF class, which
 * represents the PDF document being printed.
 * </p>
 */
public abstract class ReportItem {
  private float x=0, y=0, width=0, height=0;

  /** 
   * This method must be implemented by subclasses so that they can print themselves. Note that PGroup
   * will pre-position <code>pdf</code>'s x/y coordinates appropriately before printing the ReportItem.
   * @see PGroup#print(SimplePDF)
   */
  public abstract void print(SimplePDF pdf) throws Exception;

  /** 
   * Sets the top of this ReportItem to <code>x</code>. A value of zero indicates that the top
   * of this item is directly aligned with the top of its container; positive values indicate
   * a vertical offset downwards; and negative values position the top above the 
   * top of the container. Typically the top will default to zero.
   */
  public ReportItem setTop(float x) {
    this.y=x;
    return this;
  }
  /** 
   * Sets the left of this ReportItem to <code>x</code>. A value of zero indicates that the left side
   * of this item is directly aligned with the left side of its container; positive values indicate
   * a horizontal offset rightwards from the container's left edge; and negative values indicate an offset
   * leftwards from the container's left edge. Typically the left will default to zero.
   */
  public ReportItem setLeft(float x) {
    this.x=x;
    return this;
  }
  /** Sets the height of the this ReportItem. */
  public ReportItem setHeight(float x){
    this.height=x;
    return this;
  }
  /** Sets the width of the this ReportItem. */
  public ReportItem setWidth(float x){
    this.width=x;
    return this;
  }  
  /** A shortcut to <code>setLeft(x).setTop(y)</code>. */
  public ReportItem setPosition(float x, float y){
    this.x=x;
    this.y=y;
    return this;
  }
  /** A shortcut to <code>setHeight(y+getTop())</code>. */
  public ReportItem addHeight(float y) {
    this.height+=y;
    return this;
  }
  /** A shortcut to <code>setWidth(x+getTop())</code>. */
  public ReportItem addWidth(float x) {
    this.width+=x;
    return this;
  }
  /** A shortcut to <code>setTop(top+getTop())</code>. */
  public ReportItem addTop(float top) {
    this.y+=top;
    return this;
  }
  /** A shortcut to <code>setLeft(left+getLeft())</code>. */
  public ReportItem addLeft(float left) {
    this.x+=left;
    return this;
  }

  /** Obtains the width. */
  public float getWidth() {
    return width;
  }
  /** Obtains the height. */
  public float getHeight() {
    return height;
  }
  /** 
   * Obtains this ReportItem's offset from its container's top. 
   * @see #setTop(float)
   */
  public float getTop() {
    return y;
  }
  /** 
   * Obtains this ReportItem's offset from its container's left. 
   * @see #setLeft(float)
   */
  public float getLeft() {
    return x;
  }
  /** A shortcut to getTop()+getHeight() */
  public float getBottom() {
    return getTop()+getHeight();
  }
  /** A shortcut to getLeft()+getWidth() */
  public float getRight() {
    return getLeft()+getWidth();
  }
 

}
