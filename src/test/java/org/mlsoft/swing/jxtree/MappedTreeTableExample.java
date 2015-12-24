package org.mlsoft.swing.jxtree;

import org.jdesktop.swingx.JXTreeTable;
import org.mlsoft.swing.jtable.ColDescr;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: mla
 * Date: 09.10.12
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class MappedTreeTableExample {


    public static class Node {
        public Node parent;

        public ArrayList<Node> children = new ArrayList<Node>();

        public String name;

        public boolean flag;

        public int val;

        public Node(String name, int val, boolean flag, Node parent) {
            this.name = name;
            this.val = val;
            this.flag = flag;
            this.parent = parent;
            if (parent != null) {
                parent.children.add(this);
            }
        }
    }


    public static void main(String[] args) {
        new MappedTreeTableExample().start();
    }

    private void start() {
        JDialog d = new JDialog();
        JXTreeTable tt = new JXTreeTable();
        JScrollPane p = new JScrollPane(tt);
        d.add(p);

        Node root = buildTree();

        MappedTreeTableModel<Node> model = new MappedTreeTableModel<Node>(root, Node.class, "parent", "children", ColDescr.col("name", "Name"), ColDescr.col("val"), ColDescr.col("flag", "flag", true));
        tt.setTreeTableModel(model);

        d.pack();;
        d.setVisible(true);
    }

    private Node buildTree() {
        Node root = new Node("MyRoot", 10, false, null);
        Node a1 = new Node("A1", 12, true, root);

        Node a2 = new Node("A2", 13, false, root);

        Node b1 = new Node("B1", 14, true, a1);

        Node b2 = new Node("B2", 15, true, a2);
        return root;
    }

}
