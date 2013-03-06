/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos;

import com.tdk.domain.Institucion;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.DelegatingListModel;
import com.thorplatform.swing.NodeBuilderFactory;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingListController;
import com.thorplatform.swing.SwingNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class InstitucionTorneoListController extends SwingListController<TorneoInstitucion> {

    private JTextField txtInstitucion = new JTextField();
    private final Property<SwingNode<Institucion>> institucionNode = new Property<SwingNode<Institucion>>("institucionNode");
    private NodeFactory<Institucion> nodeFactory = null;
    private Torneo torneo;

    @Override
    protected DelegatingListModel<TorneoInstitucion> configureListModel() {
        return new DelegatingListModel<TorneoInstitucion>(getListProperty().getList());
    }

    @Override
    protected TorneoInstitucion agregarAction() {
        for (ActionListener al : txtInstitucion.getActionListeners()) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "agregarAction"));
        }

        return crearTorneoInstitucion();
    }

    @Override
    protected TorneoInstitucion editarAction(TorneoInstitucion arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected boolean quitarAction(TorneoInstitucion arg0) {
        return true;
    }

    @Override
    public void initController() {
        super.initController();

        configureView();
    }

    @SuppressWarnings("unchecked")
    private void configureView() {
        NodeBuilderFactory builderFactory = Lookup.getDefault().lookup(NodeBuilderFactory.class);
        nodeFactory = builderFactory.createNodeFactory(Institucion.class);

        ChoiceField<SwingNode<Institucion>> cfInstitucion = new ChoiceField<SwingNode<Institucion>>(txtInstitucion, "Seleccione una institucion", nodeFactory.createRootNode(), nodeFactory.getNodeSelectedClass());
        cfInstitucion.addAnnounceSupportTag(Institucion.class);
        getSwingBinder().bindChoiceToObject(cfInstitucion, institucionNode);
    }

    private TorneoInstitucion crearTorneoInstitucion() {
        if (institucionNode.get() != null) {
            if (existeInstitucionTorneo(institucionNode.get().getValue())) {
                getGuiUtils().warnnig("Ya exista la institución en el torneo");
            } else {
                TorneoInstitucion ti = new TorneoInstitucion();
                ti.setInstitucion(institucionNode.get().getValue());
                ti.setTorneo(torneo);
                return ti;
            }
        }

        return null;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    private boolean existeInstitucionTorneo(Institucion institucion) {
        boolean existe = false;
        int index = 0;
        while (!existe && index < getListProperty().getList().size()) {
            existe = getListProperty().get(index).equals(institucion);
            index++;
        }

        return false;
    }
}
