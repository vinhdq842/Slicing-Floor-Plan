package hw5_18001218;

import java.awt.*;

/**
 * @author ServantOfEvil
 */

public abstract class Cut extends Node {

    Cut(Node parent, Node left, Node right) {
        super(parent, left, right);
    }

    @Override
    public boolean isACut() {
        return true;
    }

    public abstract boolean isHorizontalCut();

    @Override
    public int getMaxHeight() {
        return Math.max(getLeft().getHeightPos(), getRight().getHeightPos());
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
}

class HorizontalCut extends Cut {

    HorizontalCut(Node parent, Node left, Node right) {
        super(parent, left, right);
    }

    @Override
    public boolean isHorizontalCut() {
        return true;
    }

    @Override
    public String delegate() {
        return "H";
    }

    @Override
    public void paintAsFloorPlan(int x, int y, Graphics g) {
        // g.translate(x, y);
        // g.drawLine(0, (int) getRight().getMinH(), (int) getMinW(), (int) getRight().getMinH());
        // g.translate(-x, -y);
        getLeft().paintAsFloorPlan(x, y + (int) getRight().getMinH(), g);
        getRight().paintAsFloorPlan(x, y, g);
    }

}

class VerticalCut extends Cut {

    VerticalCut(Node parent, Node left, Node right) {
        super(parent, left, right);
    }

    @Override
    public void paintAsFloorPlan(int x, int y, Graphics g) {
        //  g.translate(x, y);
        //  g.drawLine((int) getLeft().getMinW(), 0, (int) getLeft().getMinW(), (int) getMinH());
        //  g.translate(-x, -y);
        getLeft().paintAsFloorPlan(x, y, g);
        getRight().paintAsFloorPlan(x + (int) getLeft().getMinW(), y, g);
    }

    @Override
    public boolean isHorizontalCut() {
        return false;
    }

    @Override
    public String delegate() {
        return "V";
    }

}