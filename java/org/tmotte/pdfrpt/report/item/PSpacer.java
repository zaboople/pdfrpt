package org.tmotte.pdfrpt.report.item;
import org.tmotte.pdfrpt.report.ReportItem;
import org.tmotte.pdfrpt.SimplePDF;

/** 
 * PSpacer is essentially an empty ReportItem that can be placed between other items and used to offset them
 * via ReportItem's top/left/width/height properties.
 */
public class PSpacer extends ReportItem {
  /** Does absolutely nothing. */
  public void print(SimplePDF pdf) throws Exception{}
}