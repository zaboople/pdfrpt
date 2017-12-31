package org.tmotte.pdfrpt.report.item;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;
import com.itextpdf.text.BaseColor;

/**
 * <p>Represents a simple graphical line to be drawn in a Report.</p>
 * <p>
 * Note that when iText draws a horizontal line from position {x,y,} half the line will appear above the y coordinate, and half
 * below. The same holds true for the x coordinate in vertical lines; in general, the line width is spread perpendicular to the
 * line itself, equally in opposite directions. This is entirely reasonable, but PLine nonetheless "corrects" this
 * behavior for perfectly vertical
 * and horizontal lines only:
 * <ul>
 *   <li>When horizontal, the line is pushed down using <code>setTop()</code>so that
 * the line renders below the y-coordinate;
 *   <li>Vertical lines are pushed over using <code>setLeft()</code>, to the right of the x-coordinate.
 * </ul>
 * <p>
 * This is done to simplify the process of adding
 * PLine instances to a PGroup, horizontally or vertically, so that they fit flush with other elements. This behavior is implemented
 * by both the PLine constructor and <code>setLineWidth()</code>.
 */
public class PLine extends ReportItem {
  float xDist, yDist, lineWidth=1;
  private BaseColor color;

  /**
   * Creates a line to be starting from the x,y coordinate specified by the Top and Left properties and drawn to
   * the x,y coordinate determined by xDist and yDist.
   * <p>Note that as a side effect, xDist and yDist will determine the Width and Height properties. Changing the Width or Height
   *  properties afterwards will not affect the line, however.  </p>
   */
  public PLine(float xDist, float yDist) {
    this.xDist=xDist;
    this.yDist=yDist;
    setWidth(xDist);
    setHeight(yDist);
    setLineWidth(1);
  }
  /**
   * Sets the color using Java's standard Color object. This is passed directly to iText's BaseColor object.
   */
  public PLine setColor(java.awt.Color color) {
    return setColor(new BaseColor(color.getRGB()));
  }
  /** Sets the color of the line using RGB values; each parameter should be in the range of 0-255. */
  public PLine setColor(int red, int green, int blue) {
    return setColor(new BaseColor(red, green, blue));
  }
  /** Sets the color of the line. */
  public PLine setColor(BaseColor bc) {
    this.color=bc;
    return this;
  }
  /** Obtains the color setting. */
  public BaseColor getColor() {
    return color;
  }
  /**
   * Sets the width of the line. As noted elsewhere, this also adjusts the top/height or left/width for perfectly horizontal or vertical
   * lines, respectively.
   */
  public PLine setLineWidth(float lineWidth) {
    this.lineWidth=lineWidth;
    float half=lineWidth/2;
    if (yDist==0 && half>getHeight())
      setHeight(half);
    else
    if (xDist==0 && half>getWidth())
      setWidth(half);
    if (yDist==0 && half>getTop())
      setTop(half);
    else
    if (xDist==0 && half>getLeft())
      setLeft(half);
    return this;
  }
  /** Obtains the line width setting. */
  public float getLineWidth() {
    return lineWidth;
  }

  /**
   * Draws the line using <code>SimplePDF.drawLine()</code>.
   * @see SimplePDF#drawLine(float, float)
   */
  public void print(SimplePDF pdf) throws Exception {
    pushPrint(pdf);
    pdf.drawLine(xDist, yDist);
    pop(pdf);
  }

  ////////////////
  // INTERNALS: //
  ////////////////


  private void pushPrint(SimplePDF pdf) {
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