package org.tmotte.pdfrpt;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.BaseColor;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import org.tmotte.common.text.StringChunker;


/**
 * Contains custom font, font-size, color, and line-spacing information for printing PDF's. 
 * <p>Note that FontInfo does not contain settings for bold and italics; a separate FontInfo instance
 * must be created for normal, bold and italic versions of a font, which would typically be loaded
 * each from a separate file and managed by the programmer.</p>
 * <p>A typical usage scenario would look like this:</p><pre>
   FontInfo fMain=new FontInfo("MyFont.ttf", new FileInputStream("C:\foo.ttf"), 512000)
     .setFontSize(12)
     .adjustLineSpacing(1.2f, 0f);
   FontInfo smaller=new FontInfo(fMain, 8);
   </pre>
 */
public class FontInfo {

  ////////////////////
  // INSTANCE DATA: //
  ////////////////////

  protected BaseFont font;
  protected float fontSize=12;
  protected BaseColor color;
  protected float maxAscent=-1, maxDescent=-1, minLineSpacing=-1, 
                  adjustLineSpacingB=0.5f,adjustLineSpacingT=0.5f;

  ///////////////////
  // CONSTRUCTORS: //
  ///////////////////

  /** Constructs a FontInfo object with the specified BaseFont &amp; and font size. */
  public FontInfo(BaseFont bf, float fontSize) {
    this.font=bf;
    this.fontSize=fontSize;
    recalc();
  }
  /** 
   * Constructs a FontInfo object with the default font and size, i&#46;e&#46; <code>BaseFont.createFont()</code> and <code>12</code>.  
   * @see BaseFont#createFont()
   */
  public FontInfo() throws Exception {
    this(BaseFont.createFont(), 12);
  }
  /** A shortcut to <code>new FontInfo(toCopy).setFontSize(fontSize)</code>. */
  public FontInfo(FontInfo toCopy, float fontSize) {
    this(toCopy);
    setFontSize(fontSize);
  }
  /** 
   * A shortcut to <code>new FontInfo(FontInfo.loadFont(name, str, contentSize), 12)</code>.
   * @see #loadFont(String, InputStream, int)
   */   
  public FontInfo(String name, InputStream str, int contentSize) throws Exception {
    this(loadFont(name, str, contentSize), 12);
  }
  /** 
   * A shortcut to <code>new FontInfo(FontInfo.loadFont(name, url), 12)</code>.
   * @see #loadFont(String, URL)
   */   
  public FontInfo(String name, URL url) throws Exception {
    this(loadFont(name, url), 12);
  }
  /** Constructs a FontInfo object with properties copied from <code>toCopy</code>. */
  public FontInfo(FontInfo toCopy) {
    this.font    =toCopy.font;
    this.fontSize=toCopy.fontSize;
    this.color   =toCopy.color;
    this.adjustLineSpacingT=toCopy.adjustLineSpacingT;
    this.adjustLineSpacingB=toCopy.adjustLineSpacingB;
    this.maxAscent =toCopy.maxAscent;
    this.maxDescent=toCopy.maxDescent;
    this.minLineSpacing=toCopy.minLineSpacing;
  }

  //////////////
  // GET/SET: //
  //////////////

  /**
   * Sets the font size.
   */
  public FontInfo setFontSize(float size) {
    this.fontSize=size;
    recalc();
    return this;
  }

  /** 
   * Controls the amount of blank space above and below rendered text. When <code>lineSpacingTop</code> and
   * <code>lineSpacingBottom</code> are both 0, text lines will be spaced vertically with
   * the absolute minimum necessary to show the largest glyphs for the specified font size and avoid overlapping
   * with other lines.
   * @see #getTextLineHeight()
   */
  public FontInfo adjustLineSpacing(float top, float bottom){
    this.adjustLineSpacingT=top;
    this.adjustLineSpacingB=bottom;
    return this;
  }
  /** 
   * Same as adjustLineSpacing(top, bottom) but only does the top.
   */
  public FontInfo adjustLineSpacingTop(float top){
    this.adjustLineSpacingT=top;
    return this;
  }
  /** 
   * Same as adjustLineSpacing(top, bottom) but only does the bottom.
   */  
  public FontInfo adjustLineSpacingBottom(float bottom){
    this.adjustLineSpacingB=bottom;
    return this;
  }
  
  /**
   * Sets the font color using RGB values. Valid range of values for each parameter is 0-255.
   */
  public FontInfo setColor(int red, int green, int blue) {
    setColor(new BaseColor(red, green, blue));
    return this;
  }
  /**
   * Sets the font color using Java's standard Color object. This is passed directly to iText's BaseColor object.
   */
  public FontInfo setColor(java.awt.Color color) {
    setColor(new BaseColor(color));
    return this;
  }
  /**
   * Sets the font color using an iText BaseColor instance.
   */
  public FontInfo setColor(BaseColor bc) {
    this.color=bc;
    return this;
  }
  /**
   * Sets the font.
   */
  public FontInfo setFont(BaseFont font) {
    this.font=font;
    recalc();
    return this;
  }

  /**
   * Gets the maximum ascent (the height above the baseline) for the current font/size. This is recomputed when the
   * BaseFont or font size values are changed.
   */
  public float getMaxAscent() {
    return maxAscent;
  }
  /**
   * Gets the maximum descent (the height below the baseline) for the current font/size; note that this will be <= 0. 
   * This is recomputed when the BaseFont or font size values are changed.
   */
  public float getMaxDescent() {
    return maxDescent;
  }


  /** 
   * Obtains the font color, if there is one.
   */
  public BaseColor getColor() {
    return color;
  }

  /**
   * Gets the font.
   */
  public BaseFont getFont() {
    return font;
  }
  /**
   * Gets the font size.
   */
  public float getFontSize() {
    return fontSize;
  }

  /**
   * Gets the upper (above the characters) line spacing adjustment.
   * @return The upper line spacing for this object. Default is 0.5.
   * @see FontInfo#adjustLineSpacing(float, float)
   */
  public float getAdjustLineSpacingTop() {
    return adjustLineSpacingT;
  }
  /**
   * Gets the lower (below the characters) line spacing adjustment.
   * @return The lower line spacing for this object. Default is 0.5.
   * @see FontInfo#adjustLineSpacing(float, float)
   */
  public float getAdjustLineSpacingBottom() {
    return adjustLineSpacingB;
  }

  /**
   * Obtains the total height that a line of text should use given the current font settings, including the amount
   * added by <code>adjustLineSpacing()</code>.
   * @see #adjustLineSpacing(float, float)
   */
  public float getTextLineHeight() {
    return minLineSpacing+adjustLineSpacingT+adjustLineSpacingB;
  }

  ////////////////////////////
  // TEXT-SIZING UTILITIES: //
  ////////////////////////////

  /** 
   * Aligns fonts to a common baseline. 
   * <p>PText and PTextLines normally position text from the top-left corner instead of the baseline. This makes vertical 
   * positioning &amp; alignment easier, but it also
   * means that horizontally aligned instances of different font sizes appear to be "hanging from the ceiling" instead of 
   * "planted in the ground". </p>
   * <p>This method fixes the problem by increasing FontInfo's adjustLineSpacingTop property to 
   * add extra space to smaller FontInfo instances so that they match the baseline position of larger FontInfos. Example:</p><pre>
      FontInfo large=new FontInfo(12), small=new FontInfo(8);
      //This is where we fix the alignment:
      small.alignBaselineTo(large);
      //Aligned horizontally, items will print "correctly" even with different font sizes:
      PGroup group=new PGroup();
      group.addHorizontal(
        new PText(large, "Hello"),
        new PText(small, "World")
      ); </pre>   
   * @param largerFont The font to align baselines with. If unsure as to which is "larger" between, say, 
   *   <code>f1</code> and <code>f2</code>, do both, i.e. invoke <code>f1.alignBaselineTo(f2)</code>
   *   and <code>f2.alignBaselineTo(f1)</code>. Extra space will only be added to the smaller font.
   * @return This instance for convenience (not immutable).
   * @see org.tmotte.pdfrpt.report.PGroup#addHorizontal(ReportItem)
   * @see FontInfo#getAdjustLineSpacingTop()
   */
  public FontInfo alignBaselineTo(FontInfo largerFont) {
    float lAbove=largerFont.getMaxAscent()+largerFont.getAdjustLineSpacingTop();
    float def=lAbove-maxAscent;
    if (def>0)
      this.adjustLineSpacingT=def;
    return this;
  }

  /** 
   *  Obtains the point width of <code>text</code>. This just calls iText's <code>BaseFont.getWidthPoint(text)</code>.
   */
  public float getWidth(String text) {
    return font.getWidthPoint(text, fontSize);
  }
  /**
   * Same as getWidth(String) but gets the width of a single char.
   */
  public float getWidth(char s){
    return font.getWidthPoint(s, fontSize);
  }
 
  /** 
   * Divides <code>str</code> into two Strings, one that fits inside of width, and the leftover text. 
   * @return An array of size 2. The first member will contain the beginning of <code>str</code> up
   *   to as much text as will fit inside <code>width</code>; the second member will be null
   *   if the entire String fits inside <code>width</code>. The line-splitting algorithm is otherwise basically
   *   the same as <code>getFit()</code>.
   * @see #getFit(String, float)
   */
  public String[] split(String str, float width) {
    String[] result={null, null};
    float allWidth=getWidth(str);
    if (allWidth<width) {
      result[0]=str;
      return result;
    }
    StringChunker sc=new StringChunker(str);
    StringBuilder sb=new StringBuilder();
    float lineWidth=0;
    float spaceWidth=getWidth(' ');
    while (sc.findOrFinish(" ")) {
      String lastPart=sc.getUpTo();
      float lastPartWidth=getWidth(lastPart);
      if (lineWidth+lastPartWidth>width){
        if (sb.length()!=0) {
          result[0]=sb.toString();
          result[1]=lastPart+" "+sc.getRest();
        }
        else{
          int brake=getBreak(str, width);
          result[0]=str.substring(0, brake)+"-";
          result[1]=str.substring(brake);
        }
        return result;
      }
      lineWidth+=lastPartWidth+spaceWidth;
      sb.append(lastPart);
      sb.append(" ");
    }
    //Shouldn't happen:
    result[0]=str;
    return result;
  }
  /** An alternate to <code>split(str, width)</code>.*/
  public String[] split(float width, String str) {
    return split(str, width);
  }


  /**
   * Divides <code>str</code> into a series of Strings, such that each will fit inside <code>width</code>
   * at the current font and size. Also calculates the total height of those "lines" of text. The line-splitting
   * algorithm looks for space characters in <code>str</code> and attempts to split it at those points. If 
   * a word is too large to fit on one line, <code>getFit()</code> will 
   * break it in the middle and put a hyphen in between; this may not result in strictly "correct"
   * hyphenation, however.
   */
  public List<String> getFit(String str, float width) {
    float allWidth=getWidth(str);
    if (allWidth<width){
      ArrayList<String> list=new ArrayList<String>(1);
      list.add(str);
      return list;
    }
    float totalHeight=0, lineHeight=getTextLineHeight();
    List<String> lines=new ArrayList<String>(2+(int)(allWidth/width));//Best guess, I guess
    StringChunker sc=new StringChunker(str);
    StringBuilder sb=new StringBuilder();
    float lineWidth=0, spaceWidth=getWidth(' ');
    while (sc.findOrFinish(" ")) {
      String lastPart=sc.getUpTo();
      float lastPartWidth=getWidth(lastPart);
      
      if (lineWidth+lastPartWidth>width && lineWidth>0){
        //Next section won't fit on this line:
        if (lastPartWidth > width && lineWidth+(width/4) < width) {
          //If line is very short, we'll not break it here, we'll combine it
          //with the big chunk and split in next section:
          lastPart=sb.toString()+lastPart;
          lastPartWidth=getWidth(lastPart);
        }
        else {
          //Normal operation: Add the line and start a new line:
          lines.add(sb.toString());
          totalHeight+=lineHeight;
        }
        lineWidth=0;
        sb.setLength(0);
      }
      if (lastPartWidth>width && lineWidth==0)
        //Next section of text is too long for a line, so we'll force a break:
        while (lastPartWidth>width) {
          int brake=getBreak(lastPart, width);
          lines.add(lastPart.substring(0,brake)+"-");
          totalHeight+=lineHeight;
          lastPart=lastPart.substring(brake);
          lastPartWidth=getWidth(lastPart);
        }
      if (!lastPart.equals("")){
        lineWidth+=lastPartWidth+spaceWidth;
        sb.append(lastPart);
        sb.append(" ");
      }
    }
    if (sb.length()>0)
      lines.add(sb.toString());
    return lines;
  }
  /** An alternate way to <code>getFit(s, width)</code>.*/
  public List<String> getFit(float width, String s) {
    return getFit(s, width);
  }

  ////////////////////////
  // PRIVATE UTILITIES: //
  ////////////////////////
  
  private void recalc() {
    this.maxAscent=font.getAscentPoint(allChars, fontSize);
    this.maxDescent=font.getDescentPoint(allChars, fontSize);
    minLineSpacing=maxAscent+(-1*maxDescent);
  }
  private int getBreak(String s, float width) {
    float lineWidth=getWidth('-');
    int len=s.length();
    for (int i=0; i<len; i++) {
      char c=s.charAt(i);
      lineWidth+=font.getWidthPoint(c, fontSize);
      if (lineWidth>width)
        return i;
    }
    return len;
  }

  ///////////////////////
  // STATIC UTILITIES: //
  ///////////////////////

  /** 
   * Used for estimating line height. Contains all the characters on 
   * a typical English keyboard. The value of allChars can be modified, but it is probably better
   * to make adjustments using <code>adjustLineSpacing()</code>. 
   * @see #getTextLineHeight()
   * @see #adjustLineSpacing(float, float)
   */
  public static String allChars="`1234567890=-qwertyuiop][\\asdfghjkl;'zxcvbnm,./~!@#$%&*()+_+QWERTYUIOP}|ASDFGHJKL:\"ZXCVBNM<>?";

  /** 
   * Loads a font from an InputStream.
   * @param name This <em>MUST</em> have an extension to identify font type; for example, a truetype font could use
   *             name "foo.ttf". The rest of the name isn't important, but without the extension, iText cannot
   *             identify the font type and will throw mysterious exceptions. This method has only been tested
   *             using TrueType fonts.
   * @param contentSize This must be the size (in bytes) of the data in the InputStream, or a larger value. It may seem silly,
   *             but iText demands it. 
   */
  public static BaseFont loadFont(String name, InputStream str, int contentSize) throws Exception {
    byte[] bytes=org.tmotte.common.io.Loader.loadBytes(str, contentSize);
    return BaseFont.createFont(name, "", true, false, bytes, null);
  }
  /**
   * Similar to <code>loadFont(String, InputStream, int)</code>, but obtains the InputStream and content size from
   * <code>url</code>. Specifically, <code>url.OpenConnection().getContentLength()</code> should provide 
   * the size of the content. 
   * <p>Warning: on Weblogic, resource URL's obtained from the ClassLoader, ServletContext etc.
   * will not provide the content length because everything is being read from a Zip file; this method
   * will throw a RuntimeException in such cases. </p>
   * @throws RuntimeException If url.getConnection().getContentLength() provides a zero size.
   */
  public static BaseFont loadFont(String name, URL url) throws Exception {
    URLConnection conn=url.openConnection();
    int size=conn.getContentLength();
    if (size<=0)
      throw new RuntimeException("Couldn't get content length from URL "+url);
    return loadFont(name, conn.getInputStream(), size);
  }
  
}