package org.tmotte.pdfrpt.report.item;
import com.itextpdf.text.Image;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

/**
 * Implements support for images in Reports.
 * <p>Note that <code>resize()</code>, <code>setHeight()</code>, <code>setWidth()</code>, <code>addHeight()</code> and <code>addWidth()</code>
 * resize the image contained in this object using <code>Image.scaleToFit(float, float)</code>, which does not
 * "stretch" the image, but makes it fit within the specified height/width. Also, only <code>resize()</code> can make
 * the image <i>larger</i>. In all of these cases, PImage will resize itself to match the size of the image.
 * </p>
 */
public class PImage extends ReportItem {
  Image image;


  /**
   * Sets the iText Image object to be printed, and sets the height/width of this PImage to
   * <code>Image.getScaledWidth()</code> &amp; <code>Image.getScaledHeight()</code>
   */
  public PImage(Image i) {
    this.image=i;
    fitToImage();
  }
  /**
   * A shortcut to <code>new PImage(SimplePDF.loadImage(inStream))</code>.
   * @see SimplePDF#loadImage(InputStream)
   */
  public PImage(InputStream inStream) throws Exception {
    this(SimplePDF.loadImage(inStream));
  }
  /**
   * A shortcut to <code>new PImage(SimplePDF.loadImage(url))</code>.
   * @see SimplePDF#loadImage(URL)
   */
  public PImage(URL url) throws Exception {
    this(SimplePDF.loadImage(url));
  }

  /**
   * Creates a copy of <code>other</code>, including a copy of the image it contains.
   */
  public PImage(PImage other) {
    this(Image.getInstance(other.image));
  }

  /**
   * Obtains the internal iText image object.
   */
  public Image getImage(){
    return image;
  }

  /**
   * Resizes the image to fit within the desired width/height. This PImage will be resized to the same width/height as the image.
   * Resizing does not seem to lose image information.
   */
  public PImage resize(float width, float height) {
    super.setHeight(height).setWidth(width);
    resize();
    return this;
  }
  /**
   * Resizes the image to fit within the desired height; only has an effect if <code>h</code> is less than the current height.
   * This PImage will be resized to the same width/height as the image.
   */
  public PImage setHeight(float h) {
    super.setHeight(h);
    resize();
    return this;
  }
  /**
   * Resizes the image to fit within the desired width; only has an effect if <code>w</code> is less than the current width.
   * This PImage will be resized to the same width/height as the image.
   */
   public PImage setWidth(float w) {
    super.setWidth(w);
    resize();
    return this;
  }
  /** A shortcut to <code>setHeight(getHeight()+h)</code> */
  public PImage addHeight(float h) {
    super.addHeight(h);
    resize();
    return this;
  }
  /** A shortcut to <code>setWidth(getWidth()+h)</code> */
  public PImage addWidth(float w) {
    super.addWidth(w);
    resize();
    return this;
  }

  private void resize(){
    image.scaleToFit(getWidth(), getHeight());
    fitToImage();
  }
  private void fitToImage(){
    super.setWidth(image.getScaledWidth());
    super.setHeight(image.getScaledHeight());
  }


  /**
   * Prints the image using <code>pdf.draw(Image)</code>.
   * @see SimplePDF#draw(Image)
   */
  public void print(SimplePDF pdf) throws Exception {
    pdf.draw(image);
  }
}