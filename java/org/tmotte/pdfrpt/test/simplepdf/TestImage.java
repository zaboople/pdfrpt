package org.tmotte.pdfrpt.test.simplepdf;
import com.itextpdf.text.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import org.tmotte.pdfrpt.SimplePDF;
import org.tmotte.pdfrpt.PageInfo;


/** Tests image rendering. */
public class TestImage {
  public static void main(String[] args) throws Exception {
    SimplePDF pdf=
      new SimplePDF(new FileOutputStream(args[0]), PageInfo.LETTER_PORTRAIT)
        .setMargins(25,25,25,25);      

    pdf.setLineWidth(4);
    URL fileURL=new java.io.File("./lib/images/test2.jpg").toURL();
    Image image=SimplePDF.loadImage(
      new FileInputStream(new java.io.File("./lib/images/test2.jpg")) 
    );
    {
      pdf.draw("Test 1: We will now use the same image object twice:").lineFeed();
      image.scaleToFit(10000, 50);
      pdf.moveX(10).draw("Test 1.1: Image.scaleToFit(10000, "+image.getScaledHeight()+"):").lineFeed()
       .moveX(10).draw("Image below here").lineFeed()
       .move(10, 10).drawLine(0, 50).draw(image)
       .move(-10, image.getScaledHeight()+10).draw("Image above here").lineFeed()
       .move(-20, 10);
      image.scaleToFit(10000, 100);
      pdf.moveX(10).draw("Test 1.1: Image.scaleToFit(10000, "+image.getScaledHeight()+"):").lineFeed()
       .moveX(10).draw("Image below here").lineFeed()
       .move(10, 10).drawLine(0, 100).draw(image)
       .move(-10, image.getScaledHeight()+10).draw("Image above here").lineFeed()
       .move(-20, 10);
    }
    
    pdf.setX(0);
    {
      image.scaleToFit(25, 25);
      pdf.draw("Test 2: Image.scaleToFit(25, 25)").lineFeed()
       .moveX(10).draw("Image below here").lineFeed()
       .move(10, 10).drawLine(0, 25).draw(image)
       .moveX(-10).moveY(image.getScaledHeight()+10).draw("Image above here").lineFeed().setX(0).moveY(10);
    }
    pdf.setX(0);
    {
      image.scaleToFit(75, 210000);
      pdf.draw("Test 3.1: Image.scaleToFit(75, 210000):").lineFeed()
       .moveX(10).draw("Image below here").lineFeed()
       .move(10, 10).drawLine(75,0)
       .moveY(3).draw(image)
       .moveX(-10).moveY(image.getScaledHeight()+10).draw("Image above here").lineFeed().setX(0).moveY(10);
    }
    pdf.setX(0);
    {
      image.scaleToFit(210000, 75);
      pdf.draw("Test 3.2: Image.scaleToFit(210000, 75):").lineFeed()
       .moveX(10).draw("Image below here").lineFeed()
       .moveX(10).moveY(10).draw(image)
       .moveX(-10).moveY(image.getScaledHeight()+10).draw("Image above here").lineFeed().setX(0).moveY(10);
    }

    pdf.close();

  }
}