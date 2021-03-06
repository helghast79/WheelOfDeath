package custom;


import java.awt.*;

/**
 * Created by macha on 02/04/2016.
 */
public class PieLayout implements LayoutManager{

    private boolean isCircle;

    /**
     * Creates a new CircleLayout that lays out components in a perfect circle
     */

    public PieLayout() {
        this(true);
    }

    /**
     * Creates a new CircleLayout that lays out components in either an Ellipse or
     * a Circle. Ellipse Layout is not yet implemented.
     *
     * @param circle
     *          Indicated the shape to use. It's true for circle or false for
     *          ellipse.
     */

    public PieLayout(boolean circle) {
        isCircle = circle;
    }

    /**
     * For compatibility with LayoutManager interface
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Arranges the parent's Component objects in either an Ellipse or a Circle.
     * Ellipse is not yet implemented.
     */
    @Override
    public void layoutContainer(Container parent) {
        int x, y, w, h, s, c;
        int n = parent.getComponentCount();
        double parentWidth = parent.getSize().width;
        double parentHeight = parent.getSize().height;
        Insets insets = parent.getInsets();
        int centerX = (int) (parentWidth - (insets.left + insets.right)) / 2;
        int centerY = (int) (parentHeight - (insets.top + insets.bottom)) / 2;

        Component comp = null;
        Dimension compPS = null;
        if (n == 1) {
            comp = parent.getComponent(0);
            x = centerX;
            y = centerY;
            compPS = comp.getPreferredSize();
            w = compPS.width;
            h = compPS.height;
            comp.setBounds(x, y, w, h);
        } else {
            double r = (Math.min(parentWidth - (insets.left + insets.right), parentHeight
                    - (insets.top + insets.bottom))) / 2;
            r *= 0.75; // Multiply by .75 to account for extreme right and bottom


            // Components
            for (int i = 0; i < n; i++) {
                comp = parent.getComponent(i);
                compPS = comp.getPreferredSize();
                if (isCircle) {
                    c = (int) (r * Math.cos(2 * i * Math.PI / n));
                    s = (int) (r * Math.sin(2 * i * Math.PI / n));
                } else {
                    c = (int) ((centerX * 0.75) * Math.cos(2 * i * Math.PI / n));
                    s = (int) ((centerY * 0.75) * Math.sin(2 * i * Math.PI / n));
                }
                x = c + centerX;
                y = s + centerY;

                w = compPS.width;
                h = compPS.height;

                int size = (int)Math.min(parentWidth,parentHeight);
                int x_offset = parentWidth>size ? (int)(parentWidth-size)/2 : 0;
                int y_offset = parentHeight>size ? (int)(parentHeight-size)/2 : 0;


               comp.setBounds(x_offset, y_offset, size,size);

            }
        }

    }

    /**
     * Returns this CircleLayout's preferred size based on its Container
     *
     * @param target
     *          This CircleLayout's target container
     * @return The preferred size
     */
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return target.getSize();
    }

    /**
     * Returns this CircleLayout's minimum size based on its Container
     *
     * @param target
     *          This CircleLayout's target container
     * @return The minimum size
     */
    @Override
    public Dimension minimumLayoutSize(Container target) {
        return target.getSize();
    }

    /**
     * For compatibility with LayoutManager interface
     */
    @Override
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Returns a String representation of this CircleLayout.
     *
     * @return A String that represents this CircleLayout
     */
    @Override
    public String toString() {
        return this.getClass().getName();
    }

}
