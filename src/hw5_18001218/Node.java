package hw5_18001218;

import java.awt.*;

/**
 * @author ServantOfEvil
 */

public abstract class Node {

    private double minW;
    private double minH;

    private Node left;
    private Node right;
    private Node parent;

    private int heightPos = 1;

    public static final Color DEFAULT_COLOR;
    public static final Color MARK_COLOR;

    protected Color color;

    /**
     * The diameter for each Node to draw as a tree.
     */
    static int d = 30;

    abstract boolean isACut();

    public abstract String delegate();

    static {
        DEFAULT_COLOR = new Color(235, 220, 158);
        MARK_COLOR = new Color(85, 240, 120);
    }

    Node(Node parent, Node left, Node right) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        if (parent != null) heightPos = parent.getHeightPos() + 1;
        color = DEFAULT_COLOR;
    }


    public abstract void paintAsFloorPlan(int x, int y, Graphics g);

    public void paintAsTree(int x, int y, int anchor, Graphics g) {
        g.translate(x, y);
        g.setColor(Color.YELLOW);
        g.fillArc(0, 0, d, d, 0, 360);
        g.setColor(Color.BLACK);
        g.drawArc(0, 0, d, d, 0, 360);
        FontMetrics fontMetrics = g.getFontMetrics();
        g.drawString(delegate(), -fontMetrics.stringWidth(delegate()) / 2 + d / 2, fontMetrics.getHeight() / 2 + d / 2);

        if (hasChildren()) {
            g.drawLine(d / 2, d, -anchor / 2 + d / 2, 2 * d);
            g.drawLine(d / 2, d, anchor / 2 + d / 2, 2 * d);
        }
        g.translate(-x, -y);

        if (hasChildren()) {
            getLeft().paintAsTree(x - anchor / 2, y + 2 * d, anchor / 2, g);
            getRight().paintAsTree(x + anchor / 2, y + 2 * d, anchor / 2, g);
        }
    }

    public abstract int getMaxHeight();

    public Node getParent() {
        return parent;
    }

    public void setParent(Node p) {
        parent = p;
        if (parent != null) heightPos = parent.getHeightPos() + 1;
    }

    public abstract boolean hasChildren();

    public boolean hasParent() {
        return parent != null;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public double getMinW() {
        return minW;
    }

    public void setMinW(double minW) {
        this.minW = minW;
    }

    public double getMinH() {
        return minH;
    }

    public void setMinH(double minH) {
        this.minH = minH;
    }

    public int getHeightPos() {
        return heightPos;
    }

    public void setHeightPos(int heightPos) {
        this.heightPos = heightPos;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}