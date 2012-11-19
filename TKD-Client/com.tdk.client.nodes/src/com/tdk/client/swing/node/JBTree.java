/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import com.tdk.client.nodes.BinaryTreeModel;
import com.tdk.client.nodes.BinaryTreeModelListener;
import com.tdk.client.swing.node.actions.PositionAction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.jdesktop.swingx.JXStatusBar;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.print.ScenePrinter;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class JBTree extends JPanel {

    //BinaryTreeModel
    private BinaryTreeModel model;
    private BinaryTreeModelListener modelListener;
    private Map<BNode, Widget> nodeMapping = new HashMap<BNode, Widget>();    // the node location of the tree
    private Map<BNode, Widget> connectionMapping = new HashMap<BNode, Widget>();    // the node location of the tree
    //Layers
    private LayerWidget connectionLayer;
    private LayerWidget nodesLayer;
    private LayerWidget backgroundLayer;
    //Factories
    private WidgetFactory jbNodeFactory;
    private JBNodeConnectionFactory jBNodeConnectionFactory;
    private JBNodeLocationFactory jBNodeLocationFactory;
    private int parent2child = 200;
    private int child2child = 30;
    private int initialX = 300;
    private int initialY = 300;
    //Scene
    private Scene scene;

    //Swing
    private JScrollPane scrollPanel;
    private org.jdesktop.swingx.JXStatusBar jxStatusBar;
    private JLabel position;
    private JLabel nodePosition;

    public JBTree() {
        this(new Scene());
    }

    public JBTree(Scene scene) {
        this(scene, null);
    }

    public JBTree(Scene scene, BinaryTreeModel model) {
        if (scene == null) {
            throw new NullPointerException("The scene can not be null.");
        }

        this.scene = scene;

        initComponents();

        setModel(model);
    }

    public BinaryTreeModel getModel() {
        return model;
    }

    public void setModel(BinaryTreeModel model) {

        if (this.model != null) {
            nodeMapping.clear();
            nodesLayer.removeChildren();
            this.model.removeBinaryTreeModelListener(modelListener);
        }

        if (model != null) {
            model.addBinaryTreeModelListener(modelListener);
            rebuildTree(model.getRoot());
        } else {
            clearAll();
        }

        this.model = model;
    }

    private void installModelListener() {
        modelListener = new BinaryTreeModelListener() {

            public void fireNodeChange(BNode node) {
                refreshNode(node);
            }

            public void fireNodeInsert(BNode node) {
                add(node);
            }

            public void fireNodeDelete(BNode node) {
                removeNode(node);
            }

            public void fireStructChange(BNode node) {
                rebuildTree(node);
            }
        };
    }

    private void rebuildTree(BNode node) {
        clearAll();
        reloadMapping(node);
        System.out.println("Paso realoadMapping");
        rebuildTree();
        getScene().validate();
    }

    private void rebuildTree() {
        if (model != null && model.getRoot() != null) {
            reCalcLocation();
            System.out.println("Paso reCalcLocation");
            Iterator<BNode> it = nodeMapping.keySet().iterator();
            while (it.hasNext()) {
                BNode node = it.next();
                Widget jn = nodeMapping.get(node);
                nodesLayer.addChild(jn);

                System.out.println("Paso BNode " + node.getDisplay());
                createConnections(node);
                System.out.println("Paso create connection " + node.getDisplay());
            }

        }
    }

    private void add(BNode bnode) {
        reloadMapping(bnode);
        reCalcLocation();
        nodesLayer.addChild(nodeMapping.get(bnode));
        createConnections(bnode);
    }

    private void refreshNode(BNode node) {
        Widget jn = nodeMapping.get(node);
        if (jn != null) {
            jn.repaint();
        }

        Widget jc = connectionMapping.get(node);
        if (jc != null) {
            jc.repaint();
        }
    }

    private void removeNode(BNode bnode) {
        Widget jnode = nodeMapping.remove(bnode);
        nodesLayer.removeChild(jnode);

        Widget jconn = connectionMapping.remove(bnode);
        connectionMapping.remove(jconn);
    }

    private void reloadMapping(BNode bnode) {
        if (bnode != null) {

            Widget jn = getJbNodeFactory().createJBNode(getScene(), bnode);
            nodeMapping.put(bnode, jn);

            reloadMapping(bnode.getLeftNode());
            reloadMapping(bnode.getRightNode());
        }
    }

    private void reCalcLocation() {
        BNode root = model.getRoot();
        int top = model.size();
        //top = ((top + 1) / 2) + 1;
        long size = Math.round(Math.sqrt(top)) + 1;

        top = top * getChild2child();

        calNewDimension();

        calcLocation(root, getInitialX(), getInitialY(), top,(int) size);
    }

    private void calcLocation(BNode node, int xp, int yp, int top, int level) {
        /*if (node != null) {
            /*Widget jn = nodeMapping.get(node);
            jn.setPreferredLocation(new Point(xp, yp));
            Rectangle bounds = jn.getBounds();
            xp = xp - getParent2child() - (bounds != null ? bounds.width : 0);
            top = top / 2;

            calcLocation(node.getLeftNode(), xp, yp + top, top);
            calcLocation(node.getRightNode(), xp, yp - top, top);
        }*/
        getJBNodeLocationFactory().createJBNodeLocation(node, nodeMapping, xp, yp, top, level);
    }

    private void createConnections(BNode bnode) {
        if (bnode != null) {
            if (bnode.getParent() != null) {
                Widget nodeSource = nodeMapping.get(bnode);
                Widget nodeTarget = nodeMapping.get(bnode.getParent());

                createLines(nodeSource, nodeTarget, bnode);
            }
        }
    }

    private void createLines(Widget sourceNode, Widget targetNode, BNode node) {
        if (getJBNodeConnectionFactory() != null) {
            ConnectionWidget edge = getJBNodeConnectionFactory().createJBNodeConnection(sourceNode, targetNode, node, nodesLayer);
            connectionMapping.put(node, edge);
            connectionLayer.addChild(edge);
        }
    }

    private void clearAll() {
        nodeMapping.clear();
        connectionMapping.clear();
        connectionLayer.removeChildren();
        nodesLayer.removeChildren();
    }

    public int getParent2child() {
        return parent2child;
    }

    public void setParent2child(int parent2child) {
        this.parent2child = parent2child;
    }

    public int getChild2child() {
        return child2child;
    }

    public void setChild2child(int child2child) {
        this.child2child = child2child;
    }

    public WidgetFactory getJbNodeFactory() {
        return jbNodeFactory;
    }

    public void setJbNodeFactory(WidgetFactory jbNodeFactory) {
        this.jbNodeFactory = jbNodeFactory;
    }

    public JBNodeConnectionFactory getJBNodeConnectionFactory() {
        return jBNodeConnectionFactory;
    }

    public void setJBNodeConnectionFactory(JBNodeConnectionFactory jBNodeConnectionFactory) {
        this.jBNodeConnectionFactory = jBNodeConnectionFactory;
    }

    public int getInitialX() {
        return initialX;
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private void initComponents() {

        nodesLayer = new LayerWidget(scene);
        scene.addChild(nodesLayer);

        connectionLayer = new LayerWidget(scene);
        scene.addChild(connectionLayer);

        backgroundLayer = new LayerWidget(scene);
        scene.addChild(backgroundLayer);


        //actions
        scene.getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.5));
        scene.getActions().addAction(ActionFactory.createPanAction());

        ActionFactory.createDefaultRectangularSelectDecorator(scene);

        installModelListener();

        setLayout(new BorderLayout());

        //Creo el panel
        scrollPanel = new JScrollPane(getScene().createView());
        scrollPanel.getHorizontalScrollBar().setUnitIncrement(32);
        scrollPanel.getHorizontalScrollBar().setBlockIncrement(256);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(32);
        scrollPanel.getVerticalScrollBar().setBlockIncrement(256);

        add(scrollPanel, BorderLayout.CENTER);

        setJbNodeFactory(new JBNodeFactory());
        setJBNodeConnectionFactory(new JBNodeDirectionalConnection());
        setJBNodeLocationFactory(new JDefaultBNodeLocationFactory());

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setInitialX(screenSize.width / 4);
        setInitialY(screenSize.height / 4);

        jxStatusBar = new JXStatusBar();
        position = new JLabel("Position: x = 0, y = 0");
        jxStatusBar.add(position);
        scene.getActions().addAction(new PositionAction(position));

        //action = ActionFactory.createHoverAction(new MyHoverProvider(scene));
        //scene.getActions().addAction(action);

        jxStatusBar.addSeparator();

        nodePosition = new JLabel("Node selected: none");
        jxStatusBar.add(nodePosition);
        add(jxStatusBar, BorderLayout.SOUTH);
        
        jxStatusBar.addSeparator();
        JButton button = new JButton("Print");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ScenePrinter.print(scene, 0.5D, 0.5D);
            }
        });
        jxStatusBar.add(button);
    }

    /**
     * @return the jBNodeLocationFactory
     */
    public JBNodeLocationFactory getJBNodeLocationFactory() {
        return jBNodeLocationFactory;
    }

    /**
     * @param jBNodeLocationFactory the jBNodeLocationFactory to set
     */
    public void setJBNodeLocationFactory(JBNodeLocationFactory jBNodeLocationFactory) {
        this.jBNodeLocationFactory = jBNodeLocationFactory;
    }

    private void calNewDimension() {
        if (scene.getClientArea() != null) {
            setInitialX((int) (scene.getClientArea().getWidth() - getParent2child()));
            setInitialY((int) (scene.getClientArea().getHeight() / 2));
        } else {
            setInitialX((int) (getWidth() - getParent2child()));
            setInitialY((int) (getHeight() / 2));
        }
    }
}


