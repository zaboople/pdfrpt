package org.tmotte.pdfrpt.report.item;
import java.awt.Color;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.report.PGroup;
import org.tmotte.pdfrpt.SimplePDF;
import com.itextpdf.text.BaseColor;

/**
 * Represents a graphical rectangle to be drawn in a Report. The width and height
 * of the rectangle are determined by <code>PRect.getHeight()</code> and <code>PRect.getWidth()</code>
 * Draws the rectangle using SimplePDF's rectangle methods. 
 */
public class PRect extends ReportItem {

  float lineWidth=1;
  float offsetTL=0, offsetTR=0, offsetBR=0, offsetBL=0;
  private BaseColor color;
  private boolean outline=true;
  
  /** Creates a PRect with height and width equal to 0. */
  public PRect() {}
  
  /** Creates a PRect with dimensions of <code>width</code> and <code>height</code>. */
  public PRect(float width, float height) {
    setWidth(width);
    setHeight(height);
  }

  /**
   * Allows for rounded corners on the rectangle. The offsets should be the 
   * vertical &amp; horizontal distance of each curve's endpoints from the corner of the rectangle.
   * @see #print(SimplePDF)
   */
  public PRect setRounded(float offsetTL, float offsetTR, float offsetBR, float offsetBL) {
    this.offsetTL=offsetTL;
    this.offsetTR=offsetTR;
    this.offsetBR=offsetBR;
    this.offsetBL=offsetBL;
    return this;
  }
  /** A shortcut to <code>setRounded(offset, offset, offset, offset)</code>.*/
  public PRect setRounded(float offset) {
    return setRounded(offset, offset, offset, offset);
  }
  /**
   * Sets the color using Java's standard Color object. This is passed directly to iText's BaseColor object. The color applies to both outlined and filled rectangles.
   */
  public PRect setColor(Color color) {
    return setColor(new BaseColor(color));
  }  
  /** Sets the color of the rectangle, both for fill and outline rectangles. The color applies to both outlined and filled rectangles.*/
  public PRect setColor(BaseColor bc) {
    this.color=bc;
    return this;
  }
  /** 
   * Sets the color of the rectangle, using red/green/blue values. Allowable values for each parameter
   * are in the range of 0-255. The color applies to both outlined and filled rectangles.
   */
  public PRect setColor(int red, int green, int blue) {
    return setColor(new BaseColor(red, green, blue));
  }
  /**
   * Obtains the color used to draw the rectangle.
   */
  public BaseColor getColor() {
    return color;
  }
  /**
   * Sets the line width, for use when drawing in outline mode.
   * @see #setOutline()
   */
  public PRect setLineWidth(float lineWidth) {
    this.lineWidth=lineWidth;
    return this;
  }
  /** Gets the line width of the rectangle. */
  public float getLineWidth() {
    return lineWidth;
  }
  /**
   * Causes the rectangle to be drawn as an outline, as opposed to a fill. PRect draws an outline by default.
   * @see #setFill()
   */
  public PRect setOutline() {
    outline=true;
    return this;
  }
  /**
   * <p>Causes the rectangle to be drawn filled instead of as an outline. A PRect's Fill and Outline properties are always
   * opposites, i.e. one is false if the other is true. The default is Outline.</p>
   * <p> To draw another ReportItem on top of the rectangle (such as
   * text that should appear within it), always add PRect to a PGroup <i>before</i> the ReportItem that will 
   * be drawn on top of it, since PGroup draws ReportItems in the order they are added.
   * </p>
   * @see #setOutline
   */
  public PRect setFill() {
    outline=false;
    return this;
  }
  /**
   * A shortcut to <code>PRect.setFill().setColor(color)</code>, for convenience.
   */
  public PRect setFill(Color color) {
    setColor(color);
    setFill();
    return this;
  }
  /**
   * A shortcut to <code>PRect.setFill().setColor(color)</code>, for convenience.
   */
  public PRect setFill(BaseColor color) {
    setColor(color);
    setFill();
    return this;
  }
  /**
   * A shortcut to <code>PRect.setFill().setColor(r, g, b)</code>, for convenience.
   * @see #setColor(int, int, int)
   */
  public PRect setFill(int r, int g, int b) {
    setColor(r, g, b);
    setFill();
    return this;
  }
  /**
   * Returns true if the rectangle is to be drawn in Fill mode.
   */
  public boolean getFill() {
    return !outline;
  }
  /**
   * Returns true if the rectangle is to be drawn in Outline mode.
   */
  public boolean getOutline() {
    return outline;
  }
  
  
  /**
   * Encloses <code>item</code> within this PRect by creating a PGroup that contains both, and setting
   * the width and height of this PRect to be just
   * large enough to contain <code>item</code>'s width/height plus the specified top/right/bottom/left padding values. 
   * Works with both fill & outline PRects.
   * @param top Amount of space to put between <code>r</code> and top of rectangle.
   * @param right Amount of space to put between <code>r</code> and right side of rectangle.
   * @param bottom Amount of space to put between <code>r</code> and bottom of rectangle.
   * @param left Amount of space to put between <code>r</code> and left side of rectangle.
   * @return A ReportItem containing both the PRect and the item to be enclosed by it. 
   */
  public ReportItem enclose(float top, float right, float bottom, float left, ReportItem item) {
    float lineFactor=outline ?lineWidth :0;
    top+=lineFactor;
    left+=lineFactor;
    if (top>0)
      item.addTop(top);
    if (left>0)
      item.addLeft(left);
    setWidth( item.getWidth() +right +(lineFactor)+item.getLeft());
    setHeight(item.getHeight()+bottom+(lineFactor)+item.getTop());
    return new PGroup(this, item);
  }
  /** A shortcut to <code>enclose(padding, padding, padding, padding, item)</code>. */
  public ReportItem enclose(float padding, ReportItem item) {
    return enclose(padding, padding, padding, padding, item);
  }
  /** A shortcut to <code>enclose(0, 0, 0, 0, r)</code>. */
  public ReportItem enclose(ReportItem r) {
    return enclose(0, 0, 0, 0, r);
  }


  /**
   * Prints this PRect using SimplePDF's rectangle drawing methods as appropriate, depending on fill vs. outline and
   * rounded vs. square corners.
   * @see SimplePDF#drawRect(float, float)
   * @see SimplePDF#fillRect(float, float)
   * @see SimplePDF#drawRectRounded(float, float, float, float, float, float)
   * @see SimplePDF#fillRectRounded(float, float, float, float, float, float)
   */
  public void print(SimplePDF pdf) throws Exception {
    push(pdf);
    boolean rounded=offsetBL+offsetTL+offsetBR+offsetTR>0;
    if (outline && rounded) 
      pdf.drawRectRounded(getWidth(), getHeight(), offsetTL, offsetTR, offsetBR, offsetBL);
    else
    if (rounded)
      pdf.fillRectRounded(getWidth(), getHeight(), offsetTL, offsetTR, offsetBR, offsetBL);
    else
    if (outline)
      pdf.drawRect(getWidth(), getHeight());
    else
      pdf.fillRect(getWidth(), getHeight());
    pop(pdf);
  }
  
  ////////////////
  // INTERNALS: //
  ////////////////
  

  private void push(SimplePDF pdf) {    
    if (lineWidth!=pdf.getLineWidth()||color!=null){
      pdf.saveState();
      if (lineWidth!=0)
        pdf.setLineWidth(lineWidth);
      if (color!=null)
        pdf.setColor(color);
    }
  } 
  private void pop(SimplePDF pdf) {
    if (pdf.isStateSaved())
      pdf.restoreState();
  }
  
}