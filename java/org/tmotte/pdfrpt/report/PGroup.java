package org.tmotte.pdfrpt.report;
import org.tmotte.pdfrpt.SimplePDF;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * <p>
 * Groups ReportItems together into a single ReportItem, and acts as a central component of reporting, especially
 * as concerns relative positioning. The top/left position of a ReportItem is combined with that of its containing PGroup
 * to obtain the absolute position of the item on the page. As ReportItems are added, PGroup recalculates
 * its own width and height to accommodate the position &amp; width/height of all the items it contains. Since PGroup
 * extends ReportItem, PGroup instances can be added to other PGroups.
 * </p>
 * <p>Note that PGroup only adjusts its height/width and/or the position of a ReportItem when the ReportItem is added; if
 *    a ReportItem's left/top/height/width properties change after being added, the PGroup will be "unaware"
 *    of those changes, and the results may be undesirable.</p>
 * <b>Adding the same ReportItem to multiple PGroups:</b>
 * <p>
 * When ReportItems are added to a PGroup, the top/left values of those ReportItems are altered as documented for methods
 * like <code>PGroup.addVertical()</code> and <code>PGroup.addHorizontal()</code>.
 * One can add the same ReportItem to different PGroups, but this will usually cause undesirable results unless the item is always
 * the first item to be added to each of those PGroups. There is a simple workaround, however,
 * which is to wrap the item in a "dummy" PGroup first:
 * </p><pre>
    function addAllOver(ReportItem usedALot, PGroup here, PGroup there, PGroup elsewhere) {
      here.addVertical(new PGroup(usedALot));
      there.addHorizontal(new PGroup(usedALot));
      elsewhere.add(PGroup.HRight, PGroup.VTop, new PGroup(usedALot));
    }</pre>
 * <p> In the example above, the top/left position of the wrappers are altered by each of the <code>addXXX()</code> variations,
 * but the position of <code>usedALot</code>
 * is left alone. It will still appear at different positions, but its position relative to the enclosing dummy groups is
 * always the same.</p>
 */
public class PGroup extends ReportItem {
  /**
   * Use with <code>new PGroup(int, ReportItem...)</code>.
   * @see #PGroup(int, ReportItem...)
   */
  public final static int Horizontal=1;
  /**
   * Use with <code>new PGroup(int, ReportItem...)</code>.
   * @see #PGroup(int, ReportItem...)
   */
  public final static int Vertical=2;

  /**
   * Use with <code>add(horizontalPos, verticalPos, ReportItem)</code>.
   * @see #add(int, int, ReportItem)
   */
  public final static int HRight=3;
  /**
   * Use with <code>add(horizontalPos, verticalPos, ReportItem)</code>.
   * @see #add(int, int, ReportItem)
   */
  public final static int HLeft=4;
  /**
   * Use with <code>add(horizontalPos, verticalPos, ReportItem)</code>.
   * @see #add(int, int, ReportItem)
   */
  public final static int HCenter=5;

  /**
   * Use with <code>add(horizontalPos, verticalPos, ReportItem)</code>.
   * @see #add(int, int, ReportItem)
   */
  public final static int VTop=6;
  /**
   * Use with <code>add(horizontalPos, verticalPos, ReportItem)</code>.
   * @see #add(int, int, ReportItem)
   */
  public final static int VBottom=7;
  /**
   * Use with <code>add(horizontalPos, verticalPos, ReportItem)</code>.
   * @see #add(int, int, ReportItem)
   */
  public final static int VCenter=8;

  protected List<ReportItem> items;
  protected ReportItem lastAdded;

  ///////////////////
  // CONSTRUCTORS: //
  ///////////////////

  public PGroup() {
    items=new LinkedList<ReportItem>();
  }
  /**
   * Creates a new PGroup and adds <code>reportItems</code> to it. Assumes that
   * <code>reportItems.length</code> can be used to calculate the size of PGroup's internal List (in this case,
   * an ArrayList), so this constructor
   * is best suited to situations where the PGroup will have a fixed size; more items can
   * be added, but that will force resizing of the internal List and thus a slight performance hit.
   */
  public PGroup(ReportItem... reportItems) {
    items=new ArrayList<ReportItem>(reportItems.length);
    add(reportItems);
  }
  /**
   * <p>
   * Works the same as <code>new(ReportItem...)</code>, but
   * adds the ReportItems vertically or horizontally according to the
   * value of <code>hv</code>. </p>
   * @param hv Indicates whether <code>reportItems</code> should be added
   *  using <code>addVertical()</code> or <code>addHorizontal()</code>; use <code>PGroup.Vertical</code>
   *  to indicate the former, <code>PGroup.Horizontal</code> to indicate the latter.
   * @see #PGroup(ReportItem...)
   */
  public PGroup(int hv, ReportItem... reportItems) {
    items=new ArrayList<ReportItem>(reportItems.length);
    if (hv==Vertical)
      addVertical(reportItems);
    else
    if (hv==Horizontal)
      addHorizontal(reportItems);
    else
      throw new UnsupportedOperationException("Illegal value for parameter hv: "+hv);
  }

  protected PGroup(List<ReportItem> items) {
    this.items=items;
  }

  //////////
  // ADD: //
  //////////

  /**
   * Adds <code>item</code> to this PGroup. Does not change the top/left position setting of <code>item</code>, so
   * its actual position will be offset from the top left of this PGroup as per <code>item.getLeft()</code> and
   * <code>item.getTop()</code>
   */
  public PGroup add(ReportItem item){
    items.add(item);
    lastAdded=item;
    expandToFit(item);
    return this;
  }
  /**
   * A shortcut for adding multiple ReportItems; invokes <code>add(ReportItem)</code> for each member of <code>items</code>.
   */
  public PGroup add(ReportItem... items){
    for (ReportItem p: items)
      add(p);
    return this;
  }
  /**
   * Works the same as <code>add(ReportItem)</code>, but positions <code>item</code> vertically relative to the most recently
   * added ReportItem. If this PGroup is
   * empty, <code>item</code>'s position setting will be left alone; otherwise, it will be positioned beneath the
   * last added item, i.e. <code>item.addTop(lastAdded.getBottom())</code>. Note that this means <code>item</code> will
   * be additionally offset from the last-added ReportItem by the original value of <code>item.getTop()</code>.
   */
  public PGroup addVertical(ReportItem item) {
    if (lastAdded!=null)
      item.addTop(lastAdded.getBottom());
    add(item);
    return this;
  }
  /**
   * A shortcut to add multiple ReportItems using <code>addVertical(ReportItem)</code> .
   */
  public PGroup addVertical(ReportItem... items) {
    for (ReportItem p: items)
      addVertical(p);
    return this;
  }

  /**
   * Works the same as <code>add(ReportItem)</code>, but positions <code>item</code> horizontally relative to the most recently
   * added ReportItem. If this PGroup is
   * empty, <code>item</code>'s position setting will be left alone; otherwise, it will be placed to the right of the
   * last added item, i.e. <code>item.addLeft(lastAdded.getRight());</code>. Note that this means <code>item</code> will
   * be additionally offset from the last-added ReportItem by <code>item.getLeft()</code>.
   */
   public PGroup addHorizontal(ReportItem item) {
    if (lastAdded!=null)
      item.addLeft(lastAdded.getRight());
    add(item);
    return this;
  }
  /**
   * A shortcut to add multiple ReportItems using <code>addHorizontal(ReportItem)</code>.
   */
  public PGroup addHorizontal(ReportItem... items) {
    for (ReportItem p: items)
      addHorizontal(p);
    return this;
  }

  /**
   * Works the same as <code>addVertical(items)</code>, but additionally right-justifies each item
   * to the right side of this PGroup using <code>item.setLeft()</code>.
   */
  public PGroup addVerticalRight(ReportItem... items) {
    for (ReportItem item: items) {
      position(HRight, -1, item);
      addVertical(item);
    }
    return this;
  }
  /**
   * Works the same as <code>addVertical(items)</code>, but also horizontally centers each item
   * within this PGroup using <code>item.setLeft()</code>.
   */
  public PGroup addVerticalCenter(ReportItem... items) {
    for (ReportItem item: items) {
      position(HCenter, -1, item);
      addVertical(item);
    }
    return this;
  }

  /**
   * Adds <code>item</code> to this PGroup and invokes <code>item.addLeft()</code> and <code>item.addTop()</code>
   * to change its relative position according to rules selected by <code>horizontalPos</code> and <code>verticalPos</code>.
   * @param horizontalPos Legal values are <code>PGroup.HLeft</code>, <code>PGroup.HRight</code>, and <code>PGroup.HCenter</code>:
   <ul>
    <li><code>HLeft</code> does nothing;
    </li>
    <li><code>HCenter</code> pushes <code>item</code> to the right far enough to center it within
      this PGroup, plus the current value of <code>item.getLeft()</code>;
    </li>
    <li><code>HRight</code> pushes <code>item</code> to the
    right far enough to right-justify it, plus <code>item.getLeft()</code>.
    </li>
   </ul>
   *
   * @param verticalPos Legal values are <code>PGroup.VTop</code>, <code>PGroup.VCenter</code>, and
   *   <code>PGroup.VBottom</code>:
    <ul>
      <li><code>VTop</code> does nothing;
      </li>
      <li>
        <code>VCenter</code> moves <code>item</code> down far enough to vertically center it, plus <code>item.getTop()</code>;
      </li>
      <li>
        <code>VCenter</code> moves <code>item</code> down far enough to bottom-justify it, plus <code>item.getTop()</code>;
      </li>
    </ul>
   */
  public PGroup add(int horizontalPos, int verticalPos, ReportItem item) {
    position(horizontalPos, verticalPos, item);
    add(item);
    return this;
  }

  private void position(int h, int v, ReportItem item) {
    if (h==-1 || h==HLeft) {
    }
    else
    if (h==HRight)
      item.addLeft(getWidth()-item.getWidth());
    else
    if (h==HCenter)
      item.addLeft((getWidth()-item.getWidth())/2f);
    else
      throw new RuntimeException("Illegal value specified for horizontal position: "+h);

    if (v==-1 || v==VTop) {
    }
    else
    if (v==VBottom)
      item.addTop(getHeight()-item.getHeight());
    else
    if (v==VCenter)
      item.addTop((getHeight()-item.getHeight())/2f);
    else
      throw new RuntimeException("Illegal value specified for vertical position: "+v);
  }

  ////////////
  // PRINT: //
  ////////////

  /**
   * <p>Prints all of the items in this PGroup using <code>ReportItem.print()</code>, in the order they were added to this PGroup. </p>
   * <b>Item positioning:</b>
   * <p>
   * Before a ReportItem is printed, <code>pdf</code>'s position
   * is moved to the relative offset determined by <code>PGroup.getTop()</code> and <code>PGroup.getLeft()</code>, plus the offset
   * of the ReportItem as per <code>ReportItem.getLeft()</code> and <code>ReportItem.getTop()</code>.
   * <p>
   * When <code>print()</code> completes, <code>pdf</code>'s position is reset to the same coordinates it had
   * when <code>print()</code> was invoked.
   * </p>
   */
  public void print(SimplePDF pdf) throws Exception {
    float absLeft=pdf.getX(),
          absTop=pdf.getY();
    for (ReportItem p: items) {
      pdf.setXY(absLeft+p.getLeft(), absTop+p.getTop());
      p.print(pdf);
    }
    pdf.setXY(absLeft, absTop);
  }


  /////////////////////
  // MORE INTERNALS: //
  /////////////////////

  private void expandToFit(ReportItem p) {
    float r=p.getRight(),
          b=p.getBottom();
    if (r>getWidth())
      setWidth(r);
    if (b>getHeight())
      setHeight(b);
  }



}