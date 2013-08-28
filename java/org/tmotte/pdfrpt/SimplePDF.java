package org.tmotte.pdfrpt;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Rectangle;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.tmotte.common.text.StringChunker;

/**
 * <p>
 * A simple wrapper for the iText PDF library, for use by itself or with the org.tmotte.pdfrpt.report
 * package (in either case with iText, of course). It attempts to simplify the
 * most common tasks involved in rendering PDF output, including fonts, colors, text positioning, line art, 
 * and images.
 * </p>
 */
public class SimplePDF {


  /**
   * Creates an iText Image from the data in <code>inStream</code>. 
   * @see #draw(Image)
   */
  public static Image loadImage(InputStream inStream) throws Exception {
    java.awt.Image rawImage;
    try {
      rawImage=ImageIO.read(inStream);
    } finally {
      inStream.close();
    }
    Image x=Image.getInstance(rawImage, null);  
    return x;
  }
  /**
   * Shortcut to <code>loadImage(url.openStream())</code>;
   */
  public static Image loadImage(URL url) throws Exception {
    return loadImage(url.openStream());
  }
  
  ////////////////////
  // INSTANCE DATA: //
  ////////////////////

  private OutputStream outStream;
  private Document document;
  private PdfContentByte renderer;
  
  //Non-state-saved data:
  private float currX, currY;
  private PageInfo pageInfo;

  //Instance data that requires saveState:
  private float lineWidth=1, lineWidthPushed=-1;
  private BaseColor color, colorPushed;
  private FontInfo fontInfo, fontInfoPushed;
  protected boolean stateSaved=false;

  //Instance data that must be checked before drawing:
  private BaseFont lastFont;
  private float lastFontSize;
  private BaseColor lastColor;
  private float lastLineWidth;

  //////////////////////
  //  INITIALIZATION  //
  //////////////////////

  
  /**
   * Starts a new PDF, to be written to <code>outStream</code>, and using the specified
   * page size and margins. Invoke <code>SimplePDF.close()</code> when the PDF is finished.
   * @param pageInfo Margin and page/width height settings are copied from <code>pageInfo</code>. Changing
   *   <code>pageInfo</code>'s settings afterwards will have no effect on existing instances of SimplePDF
   *  the used it in their constructor.
   * @see com.itextpdf.text.PageSize
   */
  public SimplePDF(OutputStream outStream, PageInfo pageInfo) throws Exception{
    setFontInfo(new FontInfo().setColor(BaseColor.BLACK));
    createDocument(new PageInfo(pageInfo));
    setOutputStream(outStream);
  }
  /**
   * Shortcut to <code>SimplePDF(outStream, new PageInfo(pageSize))</code>.
   */
  public SimplePDF(OutputStream outStream, Rectangle pageSize) throws Exception{
    this(outStream, new PageInfo(pageSize));
  }
  /** 
   * Shortcut to <code>SimplePDF(outStream, new PageInfo())</code>. The PDF will use portrait/letter page size.
   */
  public SimplePDF(OutputStream outStream) throws Exception {
    this(outStream, new PageInfo());
  }


  
  private void setOutputStream(OutputStream outStream) throws Exception {
    this.outStream=outStream;
    PdfWriter writer=PdfWriter.getInstance(document, outStream);
    document.open();
    renderer=writer.getDirectContent();
    renderer.setFontAndSize(fontInfo.getFont(), fontInfo.getFontSize());
  }
  private void createDocument(PageInfo pageInfo) throws Exception{
    this.pageInfo=pageInfo;
    document=new Document();
    document.setPageSize(pageInfo.getPageSize());
    setXY(0,0);
    setColor(BaseColor.BLACK);
  }

  ///////////
  // STATE //
  ///////////

  /**
   * Saves all of the state concerning font, color, and line width of drawn lines; does not 
   * save margin settings or X-Y coordinate position. Use <code>restoreState()</code> to reset back
   * to the prior state.
   * @see #restoreState()
   * @see #isStateSaved()
   * @throws RuntimeException If saveState has already been called without a following call to restoreState.
   */
  public SimplePDF saveState() {
    if (stateSaved)
      throw new RuntimeException("State stack only goes one level deep; you must invoke restoreState() before invoking saveState() a second time.");
    stateSaved=true;
    fontInfoPushed=fontInfo;
    lineWidthPushed=lineWidth;
    colorPushed=color;
    return this;
  }
  /**
   * Companion method to <code>saveState()</code>; returns font, color and line width 
   * settings to the state previously saved by <code>saveState()</code>.
   * @see #saveState()
   * @throws RuntimeException If no call to saveState was made.
   */
  public SimplePDF restoreState() {
    stateSaved=false;
    setFontInfo(fontInfoPushed);
    lineWidth=lineWidthPushed;
    color=colorPushed;
    return this;
  }
  /**
   * Indicates whether the SimplePDF instance is currently in "saved state" mode, 
   * i.e. <code>saveState()</code> has been invoked without
   * a following call to <code>restoreState()</code>.
   * @see #saveState()
   */
  public boolean isStateSaved() {
    return stateSaved;
  }
  
  ////////////////
  // EXPOSED    //
  // INTERNALS: //
  ////////////////
  
  /** Obtains the internal iText Document object. */
  public Document getDocument() {
    return document;
  }
  /** 
   * Obtains the internal iText renderer object. This object contains advanced rendering methods for graphics that
   * are not directly supported by SimplePDF. 
   * @see #getITextX()
   * @see #getITextY()
   */
  public PdfContentByte getInternalRenderer() {
    return renderer;
  }

  ////////////////////////////
  // INSTANCE FONT METHODS: //
  ////////////////////////////

  /** 
   * Sets font information according to values set in <code>fontInfo</code>. 
   */
  public SimplePDF setFontInfo(FontInfo fontInfo) {
    this.fontInfo=fontInfo;
    return this;
  }
  /** Obtains the current font information. */
  public FontInfo getFontInfo() {
    return fontInfo;
  }
  
  //////////////////////////
  // MARGINS / PAGE SIZE: //
  //////////////////////////
  
  /** 
   * Gets the useable page width, i.e. between page margins; this is determined by the PageInfo instance
   * passed to SimplePDF's constructor, as well as by <code>SimplePDF.setMargins()</code>.
   */
  public float getWidth() {
    return pageInfo.getWidth();
  }
  /** 
   * Gets the useable page height, i.e. between page margins; this is determined by the PageInfo instance
   * passed to SimplePDF's constructor, as well as by <code>SimplePDF.setMargins()</code>.
   */
  public float getHeight(){
    return pageInfo.getHeight();
  }
  /** 
   * Sets the margins on the page.
   */
  public SimplePDF setMargins(float top, float right, float bottom, float left) {
    pageInfo.setMargins(top, right, bottom, left);
    setXY(0,0);
    return this;
  }


  //////////////////
  // POSITIONING: //
  //////////////////

  // POSITION SET: 

  /**
   * Sets the X coordinate relative to the left margin. <code>setX(0)</code>
   * will set the coordinate position directly over the left margin; <code>setX(mySimplePDF.getWidth())</code>
   * will set the coordinate position directly over the right margin.
   */
  public SimplePDF setX(float x){
    currX=x+pageInfo.marginLeft;
    return this;
  }
  /**
   * Sets the Y coordinate relative to the top margin. <code>setY(0)</code>
   * will set the coordinate position directly on the top margin; <code>setY(mySimplePDF.getHeight())</code>
   * will set the coordinate position directly on the bottom margin.
   */
  public SimplePDF setY(float y){
    currY=(pageInfo.getTotalHeight()-pageInfo.marginTop)-y;
    return this;
  }
  /**
   * A shortcut to <code>setX(x).setY(y)</code>.
   */
  public SimplePDF setXY(float x, float y){
    setX(x);
    setY(y);
    return this;
  }


  //POSITION MOVE:  

  /**
   * Moves the internal X coordinate; positive values of <code>distance</code> will
   * move to the right, negatives values to the left.
   */
  public SimplePDF moveX(float distance) {
    currX+=distance;
    return this;
  }
  /**
   * Moves the internal Y coordinate; positive values of <code>distance</code> will
   * move down, negatives values move up.
   */
  public SimplePDF moveY(float distance) {
    currY-=distance;
    return this;
  }
  /** A shortcut to <code>moveX(xDistance).moveY(yDistance)</code>. */
  public SimplePDF move(float xDistance, float yDistance){
    moveX(xDistance);
    moveY(yDistance);
    return this;
  }

  // POSITION GET: 

  /** 
   * Obtains the X coordinate position, relative to the left margin.
   * @see #setX(float)
   */
  public float getX(){
    return currX-pageInfo.marginLeft;
  }
  /** 
   * Obtains the Y coordinate position, relative to the top margin.
   * @see #setY(float)
   */
  public float getY(){
    return (pageInfo.getTotalHeight()-pageInfo.marginTop)-currY;
  }
  /**
   * Determines whether the intended vertical position change will
   * move past the bottom margin; useful when deciding whether to 
   * start a new page. Does not change the current position, however.
   * @return True if <code>moveY(distance)</code> will move the Y 
   *   coordinate below the bottom margin.
   */
  public boolean willMovePastBottom(float distance) {
    return currY-distance<pageInfo.marginBottom;
  }
  /**
   * This obtains the X coordinate as seen by iText. This is only
   * useful when making direct calls to the iText PDFContentByte 
   * instance.
   * @see #getInternalRenderer()
   */
  public float getITextX() {
    return currX;
  }
  /**
   * This obtains the Y coordinate as seen by iText. This is only
   * useful when making direct calls to the iText PDFContentByte 
   * instance.
   * @see #getInternalRenderer()
   */
  public float getITextY() {
    return currY;
  }
  
  ////////////
  // COLOR: //
  ////////////

  /**
   * Sets the "fill" and "stroke" colors, i.e. the colors for text
   * and line art. Does not affect the color of Images.
   */
  public SimplePDF setColor(BaseColor bc) {
    this.color=bc;
    return this;
  }
  /**
   * A shortcut to <code>setColor(new BaseColor(red, green, blue))</code>;
   */
  public SimplePDF setColor(int red, int green, int blue) {
    return setColor(new BaseColor(red, green, blue));
  }
  /**
   * Sets the color using Java's standard Color object. This is passed directly to iText's BaseColor object.
   */
  public SimplePDF setColor(java.awt.Color color) {
    return setColor(new BaseColor(color));
  }  

  ///////////////////////////////
  // GEOMETRY-DRAWING METHODS: //
  ///////////////////////////////
  /**
   * Sets the line width used for drawing lines and rectangles. Does not
   * affect lines already drawn.
   */
  public SimplePDF setLineWidth(float f) {
    lineWidth=f;
    return this;
  }
  public float getLineWidth() {
    return lineWidth;
  }

  /**
   * Draws a straight line from the current X,Y coordinates according to the
   * distance specified by xDistance and yDistance, using the current line width and color. 
   * Note that width-wise, half of the line will appear to either side of the actual
   * path drawn.
   * @param xDistance Positive values draw to the right, negative to the left.
   * @param yDistance Positive values draw downwards, negative upwards.
   * @see #setLineWidth(float)
   * @see #setColor(BaseColor)
   */
  public SimplePDF drawLine(float xDistance, float yDistance) {
    checkDraw();
    renderer.moveTo(currX, currY);
    renderer.lineTo(currX+xDistance, currY-yDistance);
    renderer.stroke();
    return this;
  }
  /**
   * <p>Draws a rectangle outline from the current X,Y coordinates according
   * to the distance specified by <code>width</code> and <code>height</code>, 
   * using the current line width and color. 
   * </p>
   * <p>Note that the line will drawn be <i>inside</i>
   * the specified coordinates, not half-in/half-out; although this is atypical,
   * it allows easier control of the rectangle's position.
   * </p> 
   * @param width Positive values draw to the right, negative to the left.
   * @param height Positive values draw downwards, negative upwards.
   * @see #setLineWidth(float)
   * @see #setColor(BaseColor)
   * @see #fillRect(float, float)
   */
  public SimplePDF drawRect(float width, float height){
    checkDraw();
    float half=lineWidth/2;
    renderer.rectangle(currX+half, currY-half, width-lineWidth, -(height-lineWidth));  
    renderer.stroke();
    return this;
  }

  /**
   * <p>Generally the same as <code>drawRect()</code>, but with rounded corners. The specified offsets should be the 
   * perpendicular distance from each corner (i.e. the distance along the x and y
   * axes from the corner) where that corner's curve begins. 
   * </p>
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   * @param offsetTL Offset from the top left.
   * @param offsetTR Offset from the top right.
   * @param offsetBR Offset from the bottom right.
   * @param offsetBL Offset from the bottom left.
   */
  public SimplePDF drawRectRounded(float width, float height, float offsetTL, float offsetTR, float offsetBR, float offsetBL){
    drawRectRounded(width, height, offsetTL, offsetTR, offsetBR, offsetBL, false);
    return this;
  }
  /**
   * The same as drawRectRounded(), but fills the rectangle with the current color.
   * @see #drawRectRounded(float, float, float, float, float, float)
   * @see #setColor(BaseColor)
   */
  public SimplePDF fillRectRounded(float width, float height, float offsetTL, float offsetTR, float offsetBR, float offsetBL){
    drawRectRounded(width, height, offsetTL, offsetTR, offsetBR, offsetBL, true);
    return this;
  }
  
  private final static float hypFactor=1f-((float)Math.cos(Math.PI/4));
  private static float getHype(float offset){
    return offset/hypFactor;
  }
  private SimplePDF drawRectRounded(
      float width, float height, 
      float offsetTL, float offsetTR, float offsetBR, float offsetBL,
      boolean fill
    ){
    checkDraw();
    float x=currX, y=currY;
    if (!fill){
      float half=lineWidth/2;
      x+=half;
      y-=half;
      width-=lineWidth;
      height-=lineWidth;
    }
    float bezTL=getHype(offsetTL)
         ,bezTR=getHype(offsetTR)
         ,bezBR=getHype(offsetBR)
         ,bezBL=getHype(offsetBL);
    //Top left:
    renderer.moveTo(x, y-bezTL);
    renderer.curveTo(x, y, x+bezTL, y);
    //Top right:
    renderer.lineTo(x+width-bezTR, y);
    renderer.curveTo(x+width, y, x+width, y-bezTR);
    //Bottom right:
    renderer.lineTo(x+width, y-(height-bezBR));
    renderer.curveTo(x+width, y-height, x+width-bezBR, y-height);
    //Bottom left:
    renderer.lineTo(x+bezBL, y-height);
    renderer.curveTo(x, y-height, x, y-(height-bezBL));
    //Back to top left:
    renderer.lineTo(x, y-bezTL);
    if (fill) renderer.fill();
    else      renderer.stroke();
    return this;
  }
  /**
   * Fills in a rectangle from the current X,Y coordinates according
   * to the distance specified by <code>width</code> and <code>height</code>, 
   * using the current line width.
   * @param width Positive values draw to the right, negative to the left.
   * @param height Positive values draw downwards, negative upwards.
   * @see #setLineWidth(float)
   * @see #setColor(BaseColor)
   * @see #drawRect(float, float)
   */
  public SimplePDF fillRect(float width, float height){
    checkDraw();
    renderer.rectangle(currX, currY, width, -height);  
    renderer.fill();
    return this;
  }

  ////////////////////////////
  // IMAGE DRAWING METHODS: //
  ////////////////////////////

  /**
   * Draws <code>image</code> at the current X,Y coordinate position, according
   * the size determined by <code>image.getScaledHeight()</code> and <code>image.getScaledWidth()</code>
   * @see #loadImage(InputStream)
   */
  public SimplePDF draw(Image image) throws Exception {
    float imgHeight=(image.getScaledHeight()), 
          imgWidth=(image.getScaledWidth());
    renderer.addImage(image, imgWidth, 0, 0, imgHeight, currX, currY-imgHeight);
    return this;
  }

  ///////////////////////////
  // TEXT DRAWING METHODS: //
  ///////////////////////////

  /**
   * Draws <code>text</code> to the right of the current X,Y coordinate position.
   */
  public SimplePDF draw(String text) {
    return draw(text, currX, currY);
  }
  /**
   * Draws <code>text</code> to the right of the current X,Y coordinate position and moves
   * the X coordinate to the end of the text; equivalent to <code>draw(text).moveX(getWidth(text))</code>.
   */
  public SimplePDF drawAndMoveX(String text) {
    draw(text);
    moveX(fontInfo.getWidth(text));
    return this;
  }
  /**
   * Draws <code>text</code> to the left of the current X,Y coordinate position, i.e. right-justifies
   * the text.
   */
  public SimplePDF drawToLeft(String text) {
    float width=fontInfo.getWidth(text);
    return draw(text, currX-width, currY);
  }
  /**
   * Draws <code>text</code> centered on the current X,Y coordinate position.
   */
  public SimplePDF drawCentered(String text, float width) {
    float pad=(width-fontInfo.getWidth(text))/2;
    return draw(text, currX+pad, currY);
  }
  private SimplePDF draw(String text, float x, float y) {
    checkFont();
    renderer.beginText();
    renderer.setTextMatrix(x, y-(fontInfo.getMaxAscent()+fontInfo.getAdjustLineSpacingTop()));
    renderer.showText(text);
    renderer.stroke();
    renderer.endText();   
    return this;
  }
  /**
   * Moves the Y coordinate downwards by the amount of <code>getFontInfo().getTextLineHeight()</code>; 
   * does not affect the X coordinate.
   * @see FontInfo#getTextLineHeight
   */
  public SimplePDF lineFeed(){
    currY-=fontInfo.getTextLineHeight();
    return this;
  }
  /**
   * Determines whether <code>lineFeed()</code> will push the Y coordinate down below the bottom
   * margin. 
   */
  public boolean lineFeedWillExceed() {
    return currY-(fontInfo.getTextLineHeight())<pageInfo.marginBottom;
  }


  /////////////
  // PAGING: //
  /////////////

  /** 
   * Starts a new page in the document. Does not change the current X,Y coordinates, so 
   * those may need to be set before drawing anything.
   * @see #setXY(float, float)
   */
  public SimplePDF newPage() {
    document.newPage();
    //This is necessary because of an iText (and/or PDF) bug/feature. The font, line & color information
    //is lost when we switch pages.
    lastFontSize=-1;
    lastFont=null;
    lastColor=BaseColor.BLACK;
    lastLineWidth=-1;
    return this;
  }

  ////////////
  // CLOSE: //
  ////////////

  /** 
   * Closes the internal iText Document object and the OutputStream.
   */
  public void close() throws java.io.IOException {
    document.close();
    outStream.flush();
    outStream.close();
  }


  private void checkFont() {
    if (renderer==null)
      return;
    BaseFont a=fontInfo.getFont();
    float b=fontInfo.getFontSize();
    if (lastFont!=a || lastFontSize!=b){
      renderer.setFontAndSize(a, b);
      lastFont=a;
      lastFontSize=b;
    }
    BaseColor c=fontInfo.getColor();
    if (c!=null && lastColor!=c){
      renderer.setColorFill(c);
      renderer.setColorStroke(c);
      lastColor=c;
    }      
    else
    if (c==null && color!=null && lastColor!=color){
      renderer.setColorFill(color);
      renderer.setColorStroke(color);
      lastColor=color;
    }          
  }
  private void checkDraw() {  
    if (renderer==null)
      return;
    if (color!=null && lastColor!=color){
      renderer.setColorFill(color);
      renderer.setColorStroke(color);
      lastColor=color;
    }          
    if (lastLineWidth!=lineWidth){
      renderer.setLineWidth(lineWidth);
      lastLineWidth=lineWidth;
    }
  }

}
