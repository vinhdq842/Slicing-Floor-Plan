package hw5_18001218;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author ServantOfEvil
 * The source code for exercise 4 - hw5
 */

public class SlicingFloorPlan extends JPanel implements ActionListener {

    /**
     * Implement tree with linked structure.
     */
    private Node slicingTree;

    /**
     * The height of the tree. Default is 1, the tree initially has one node.
     */
    private int height = 1;

    /**
     * Contains all leaves of the slicing tree.
     */
    private final ArrayList<Node> leaves;

    /**
     * Illustrates things.
     */
    private final JFrame frame;

    /**
     * Functions.
     */
    private final JButton assign;
    private final JButton verCut;
    private final JButton horCut;
    private final JButton reset;
    private final JButton refineTree;

    /**
     * Contains leaf indices to perform operation on.
     */
    private final JComboBox<String> leafIndices;

    /**
     * For input.
     */
    private final JTextField minWidth;
    private final JTextField minHeight;

    /**
     * Default dimension for the Canvas.
     */
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 650;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Slicing Floor Plan");

        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        } catch (Exception e) {
            System.out.println("NimbusLookAndFeel not installed...");
        }

        frame.setBounds(100, 50, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.getContentPane().add(new JScrollPane(new SlicingFloorPlan(frame)));

        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setIconImage(new ImageIcon("".getClass().getResource("/icon.png")).getImage());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    SlicingFloorPlan(JFrame frame) {
        this(150, 150, frame);
    }

    SlicingFloorPlan(double width, double height, JFrame frame) {
        slicingTree = new BasicRectangle(width, height, null, 1);
        leaves = new ArrayList<>();
        leaves.add(slicingTree);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(reset = new JButton("Reset"));
        menuBar.add(verCut = new JButton("Vertical Cut"));
        menuBar.add(horCut = new JButton("Horizontal Cut"));
        menuBar.add(assign = new JButton("Assign"));
        menuBar.add(refineTree = new JButton("Refine tree(experimental)"));
        menuBar.add(new JLabel("on leaf"));
        menuBar.add(leafIndices = new JComboBox<>(new String[]{"1"}));
        menuBar.add(new JLabel("with min width ="));
        menuBar.add(minWidth = new JTextField(3));
        menuBar.add(new JLabel("and min height ="));
        menuBar.add(minHeight = new JTextField(3));

        reset.addActionListener(this);
        verCut.addActionListener(this);
        horCut.addActionListener(this);
        assign.addActionListener(this);
        refineTree.addActionListener(this);
        leafIndices.addActionListener(this);
        actionPerformed(null);

        (this.frame = frame).setJMenuBar(menuBar);
    }

    /**
     * Renders things on screen.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawFloorPlan(getWidth() / 2 - (int) slicingTree.getMinW() - 100, 200, g);
        drawSlicingTree(getWidth() / 2 + (int) Math.pow(2, height - 1) * Node.d, 200, (int) Math.pow(2, height - 1) * Node.d, g);
        g.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        String author = "*=. Written By SOE .=*";
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString(author, getWidth() / 2 - fontMetrics.stringWidth(author) / 2, getHeight() - fontMetrics.getHeight());
    }

    private void drawFloorPlan(int x, int y, Graphics g) {
        g.translate(x, y);
        slicingTree.paintAsFloorPlan(0, 0, g);
        g.setColor(Color.RED);
        g.drawRect(0, 0, (int) slicingTree.getMinW(), (int) slicingTree.getMinH());
        g.setColor(Color.BLACK);
        g.translate(-x, -y);
    }

    private void drawSlicingTree(int x, int y, int anchor, Graphics g) {
        g.translate(x, y);
        slicingTree.paintAsTree(0, 0, anchor, g);
        g.translate(-x, -y);
    }

    /**
     * Re-computes the width of the Node p.
     *
     * @return new computed width.
     */
    public double w(Node p) {
        if (p.isACut()) {

            Cut cut = (Cut) p;
            double tmp;

            if (cut.isHorizontalCut()) {
                p.setMinW(tmp = Math.max(w(cut.getLeft()), w(cut.getRight())));
            } else {
                p.setMinW(tmp = w(cut.getLeft()) + w(cut.getRight()));
            }

            return tmp;
        } else return p.getMinW();
    }

    /**
     * Re-computes the height of the Node p.
     *
     * @return new computed height.
     */
    public double h(Node p) {
        if (p.isACut()) {

            Cut cut = (Cut) p;
            double tmp;

            if (cut.isHorizontalCut()) {
                p.setMinH(tmp = h(cut.getLeft()) + h(cut.getRight()));
            } else {
                p.setMinH(tmp = Math.max(h(cut.getLeft()), h(cut.getRight())));
            }

            return tmp;
        } else return p.getMinH();
    }

    /**
     * Cuts the Node p with a horizontal cut.
     */
    public void horizontallyCut(Node p) {
        if (!(p instanceof BasicRectangle)) return;

        HorizontalCut cut = new HorizontalCut(p.getParent(), new BasicRectangle(p.getMinW(), p.getMinH() / 2, Integer.parseInt(p.delegate())), new BasicRectangle(p.getMinW(), p.getMinH() / 2, leaves.size() + 1));
        updateAfterCut(cut, p);
    }

    /**
     * Cuts the Node p with a vertical cut.
     */
    public void verticallyCut(Node p) {
        if (!(p instanceof BasicRectangle)) return;

        VerticalCut cut = new VerticalCut(p.getParent(), new BasicRectangle(p.getMinW() / 2, p.getMinH(), Integer.parseInt(p.delegate())), new BasicRectangle(p.getMinW() / 2, p.getMinH(), leaves.size() + 1));
        updateAfterCut(cut, p);
    }

    /**
     * Updates the tree.
     */
    private void updateAfterCut(Cut cut, Node p) {
        cut.setMinH(p.getMinH());
        cut.setMinW(p.getMinW());
        cut.getLeft().setParent(cut);
        cut.getRight().setParent(cut);

        if (p.hasParent()) {
            Node c = p.getParent();
            if (c.getLeft().equals(p)) c.setLeft(cut);
            else c.setRight(cut);
        } else slicingTree = cut;

        leaves.remove(p);
        leaves.add(cut.getLeft());
        leaves.add(cut.getRight());
        height = Math.max(height, cut.getLeft().getHeightPos());
    }

    public void setMin(Node p, double minW, double minH) {
        if (p == null) return;
        p.setMinW(minW);
        p.setMinH(minH);
    }

    /**
     * Re-computes width and height for all Nodes of the tree.
     */
    public void compact() {
        w(slicingTree);
        h(slicingTree);
    }

    /**
     * Handles events.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object eSource = e == null ? null : e.getSource();
        int selectedIndex = leafIndices.getSelectedIndex();

        if (eSource == assign) {
            double newW = 0;
            double newH = 0;

            try {
                newW = Double.parseDouble(minWidth.getText());
                newH = Double.parseDouble(minHeight.getText());
            } catch (Exception exception) {
                System.out.println(exception.toString());
            }
            if (newW > 0 && newH > 0) setMin(getLeafAt(selectedIndex + 1), newW, newH);
            compact();
            notifySizeChanged();
        } else if (eSource == verCut) {
            verticallyCut(getLeafAt(selectedIndex + 1));
            notifySizeChanged();
        } else if (eSource == horCut) {
            horizontallyCut(getLeafAt(selectedIndex + 1));
            notifySizeChanged();
        } else if (eSource == reset) {
            ((JScrollPane) frame.getContentPane().getComponent(0)).setViewportView(new SlicingFloorPlan(frame));
        } else if (eSource == refineTree) {
            processTree(slicingTree);
            updateHeightPos(slicingTree);
            compact();
        }

        Node node = getLeafAt(selectedIndex + 1);
        if (node != null) {
            minHeight.setText(node.getMinH() + "");
            minWidth.setText(node.getMinW() + "");
            for (Node n : leaves) n.setColor(Node.DEFAULT_COLOR);
            node.setColor(Node.MARK_COLOR);
        }

        updateLeafIndices();
        if (selectedIndex < leafIndices.getItemCount()) leafIndices.setSelectedIndex(selectedIndex);
        repaint();
    }

    /**
     * Shrink or stretch the Canvas to fit its contents.
     * There's a little bug, could be of the language.
     */
    private void notifySizeChanged() {
        double tmp;

        if ((tmp = getWidth() / 2d + Node.d + Math.pow(2, height - 1) * Node.d * 2 - getWidth()) > 0) {
            setPreferredSize(new Dimension((int) (getWidth() + tmp * 2), getHeight()));
        }
        if ((tmp = getWidth() / 2d - (int) slicingTree.getMinW() - 200) < 0)
            setPreferredSize(new Dimension((int) (getWidth() - 2 * tmp), getHeight()));

        if ((tmp = 300 + slicingTree.getMinH() - getHeight()) > 0)
            setPreferredSize(new Dimension(getWidth(), getHeight() + (int) tmp));
        if ((tmp = 300 + (height - 1) * Node.d * 2 - getHeight()) > 0)
            setPreferredSize(new Dimension(getWidth(), getHeight() + (int) tmp));

        revalidate();
    }

    /**
     * Updates indices for the JCombobox.
     */
    private void updateLeafIndices() {
        leafIndices.removeAllItems();
        for (int i = 1; i <= leaves.size(); ++i) leafIndices.addItem(String.valueOf(i));
    }

    /**
     * Returns the leaf labeled with num.
     *
     * @return the found leaf.
     */
    private Node getLeafAt(int num) {
        for (Node node : leaves) if (node.delegate().equals(String.valueOf(num))) return node;
        return null;
    }

    /**
     * Makes the tree more balanced
     */
    private void processTree(Node root) {
        if (!root.isACut()) return;

        Node left1 = root.getLeft();
        Node right1 = root.getRight();
        Node left2;
        Node right2;

        if (left1.delegate().equals(root.delegate())) {
            left2 = left1.getLeft();
            right2 = left1.getRight();
            if (left2.delegate().equals(left1.delegate())) {
                root.setLeft(left2);
                left2.setParent(root);
                left1.setLeft(right2);
                left1.setRight(right1);
                right1.setParent(left1);
                root.setRight(left1);
            }
        } else if (right1.delegate().equals(root.delegate())) {
            left2 = right1.getLeft();
            right2 = right1.getRight();
            if (right2.delegate().equals(right1.delegate())) {
                root.setRight(right2);
                right2.setParent(root);
                right1.setRight(left2);
                right1.setLeft(left1);
                left1.setParent(right1);
                root.setLeft(right1);
            }
        }
        processTree(root.getLeft());
        processTree(root.getRight());
    }

    /**
     * Gets height of a tree.
     */
    private int getMaxHeightOf(Node node) {
        return node.getMaxHeight() - node.getHeightPos() + 1;
    }

    /**
     * Updates height pos.
     */
    private void updateHeightPos(Node root) {
        if (root.hasChildren()) {
            root.getLeft().setHeightPos(root.getHeightPos() + 1);
            root.getRight().setHeightPos(root.getHeightPos() + 1);
            updateHeightPos(root.getLeft());
            updateHeightPos(root.getRight());
        }
    }

}