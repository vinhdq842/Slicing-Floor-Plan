package hw5_18001218;

import java.awt.*;

/**
 * @author ServantOfEvil
 */

public class BasicRectangle extends Node {

    /**
     * String to display inside the Node.
     */
    private final String delegate;

    BasicRectangle(double minW, double minH, Node parent, int number) {
        super(parent, null, null);
        setMinW(minW);
        setMinH(minH);
        delegate = String.valueOf(number);
    }


    BasicRectangle(double minW, double minH, int number) {
        this(minW, minH, null, number);
    }

    @Override
    public boolean isACut() {
        return false;
    }

    @Override
    public String delegate() {
        return delegate;
    }

    @Override
    public void paintAsFloorPlan(int x, int y, Graphics g) {
        g.translate(x, y);
        g.setColor(color);
        g.fillRect(0, 0, (int) getMinW(), (int) getMinH());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, (int) getMinW(), (int) getMinH());
        FontMetrics fontMetrics = g.getFontMetrics();
        g.drawString(delegate(), (int) (getMinW() / 2 - fontMetrics.stringWidth(delegate) / 2d), (int) (getMinH() / 2 + fontMetrics.getHeight() / 2d));
        g.translate(-x, -y);
    }

    @Override
    public int getMaxHeight() {
        return getHeightPos();
    }

    @Override
    public boolean hasChildren() {
        return false;
    }
}