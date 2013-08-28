package org.tmotte.pdfrpt.report.item;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.tmotte.common.text.StringChunker;
import org.tmotte.pdfrpt.FontInfo;
import org.tmotte.pdfrpt.report.PageCount;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

/**
 * Similar to PText, but allows for multi-line text. PTextLines computes individual line heights 
 * and its total height (and width) using FontInfo, so that lines are spaced vertically as intended. 
 * Thus <code>PTextLines.getHeight()</code>
 * should reflect the total height of all the lines of text contained; <code>PTextLines.addHeight()/setHeight()</code>
 * can be used to add extra space underneath the rendered text.
 * <p>Like PText, PTextLines defaults to left-aligned text.</p>
 */
public class PTextLines extends AbstractText {

  static Pattern breakPatt=Pattern.compile("(\n|\r)");
  private String[] lines;
  
  /**
   * Constructs a PTextLines using the specified font, width and text, adding line breaks
   * as necessary to make <code>text</code> fit within <code>width</code>. The height will be automatically
   * set to the total height of the resulting lines.
   * <p>Note that carriage return/line feed characters (\r\n) in <code>text</code> will be
   * printed as line breaks in the pdf. </p>
   * @param font  Contains font information.
   * @param width The desired width.
   * @param text  The text to display.
   * @see FontInfo#getFit(String, float)
   */
  public PTextLines(FontInfo font, float width, String text) {
    super(font);
    setWidth(width);
    float lineHeight=font.getTextLineHeight();
    if (text.indexOf((char)13)>-1 || text.indexOf((char)10)>-1){
      StringChunker sc=new StringChunker(text);
      List<String> newLines=new LinkedList<String>();
      while (sc.findOrFinish(breakPatt)){
        String s=sc.getUpTo();
        if (!"".equals(s)){
          List<String> fit=font.getFit(s, width);
          addHeight(fit.size()*lineHeight);
          for (String ttt: fit)
            newLines.add(ttt);
        }
      }
      lines=getLines(newLines);
    }
    else {
      List<String> fit=font.getFit(text, width);
      setHeight(lineHeight*fit.size());
      lines=getLines(fit);
    }
  }
  /**
   * Creates a new PTextLines with line breaks between each member
   * of the <code>text</code> array (for convenience, this is a varargs paramter).
   * Line feed and carriage return (\n, \r) characters in <code>text</code> will be ignored.
   */
  public PTextLines(FontInfo font, String... text) {
    super(font);
    lines=text;
    float w=0;
    setHeight(font.getTextLineHeight()*lines.length);
    for (String s: lines) {
      float x=font.getWidth(s);
      if (x>w)
        w=x;
    }
    setWidth(w);
  }



  /**
   * A shortcut to <code>PTextLines(pdf.getFontInfo(), width, text)</code>.
   */
  public PTextLines(SimplePDF pdf, float width, String text) {
    this(pdf.getFontInfo(), width, text);
  }
  /** 
   * A shortcut to <code>PTextLines(pdf.getFontInfo(), text)</code>.
   */
  public PTextLines(SimplePDF pdf, String... text) {
    this(pdf.getFontInfo(), text);
  }
  /** 
   * Converts <code>lines</code> to a String[] array and invokes <code>PTextLines(FontInfo, String...)</code>.
   */
  public PTextLines(FontInfo font, List<String> lines) {
    this(font, getLines(lines));
  }
  /** A shortcut to <code>PTextLines(pdf.getFontInfo(), lines)</code> */
  public PTextLines(SimplePDF pdf, List<String> lines) {
    this(pdf.getFontInfo(), getLines(lines));
  }


  private static String[] getLines(List<String> x) {
    String[] lines=new String[x.size()];
    for (int i=0; i<lines.length; i++)
      lines[i]=x.get(i);
    return lines;
  }

  ////////////////
  // ALIGNMENT: //
  ////////////////

  /**
   * Causes each line of text to be right-aligned when printed. 
   */
  public PTextLines rightAlign() {
    rightAlign=true;
    center=false;
    return this;
  }
  /**
   * Causes each line of text to be horizontally centered when printed. 
   */
  public PTextLines center() {
    rightAlign=false;
    center=true;
    return this;
  }

  /////////////////////
  // SIMPLE GET/SET: //
  /////////////////////
  
  /** Obtains the internal representation of the lines of text to be printed. */
  public String[] getLines() {
    return lines;
  }
  /** Obtains the FontInfo instance passed to the constructor. */
  public FontInfo getFontInfo() {
    return fontInfo;
  }

  /**
   * Overrides <code>AbstractText.setPageCount()</code> for method-chaining convenience.
   * @see AbstractText#setPageCount(PageCount)
   */
  public PTextLines setPageCount(PageCount pc) {
    super.setPageCount(pc);
    return this;
  }
  
  ////////////
  // PRINT: //
  ////////////

  /**
   * Similar to <code>PText.print(pdf)</code>, but prints multiple lines.
   * @see PText#print(SimplePDF)
   */
  public void print(SimplePDF pdf) {
    pushPrint(pdf);
    final boolean r=rightAlign, c=center;
    if (r)
      pdf.moveX(getRight()-getLeft());
    float wide=getWidth();
    for (String s: lines){
      if (pageCounter!=null)
        s=replacePageVars(s);
      if (r)
        pdf.drawToLeft(s).lineFeed();
      else
      if (c)
        pdf.drawCentered(s, wide).lineFeed();
      else
        pdf.draw(s).lineFeed();
    }
    pop(pdf);
  }
  
  
  ////////////////
  // DEBUGGING: //
  ////////////////
  
  /** Prints the text contents of this instance, for debugging purposes. */
  public String toString() {
    StringBuilder sb=new StringBuilder();
    for (String s: lines)
      sb.append(s+"\n");
    return sb.toString();
  }
}
