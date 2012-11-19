/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.competencias;

import com.tdk.domain.Alumno;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.competencia.Competencia;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.NodeBuilderFactory;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.validator.RequiredPropertyValidator;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class FinalizarCompetenciaController extends SwingModalController {

    private FinalizarCompetenciaForm form = new FinalizarCompetenciaForm();

    //Model
    public final Property<SwingNode<Alumno>> competidorAzulNode = new Property<SwingNode<Alumno>>("competidorAzul");
    public final Property<SwingNode<Alumno>> competidorRojoNode = new Property<SwingNode<Alumno>>("competidorAzul");
    public final Property<Long> numero = new Property<Long>("numero");
    public final Property<Competidor> ganador = new Property<Competidor>("ganador");
    public final ListProperty<Competidor> competidores = new ListProperty<Competidor>("ganador");
    private NodeFactory<Alumno> nodeFactory;
    
    @Override
    protected JButton getAcceptButton() {
        return form.btnAceptar;
    }

    @Override
    protected JButton getCancelButton() {
        return form.btnCancelar;
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        configureValidators();
        configureView();
        configureBindings();
        initPresentationModel();
    }

    @SuppressWarnings("unchecked")
    private void configureBindings() {
        NodeBuilderFactory builderFactory = Lookup.getDefault().lookup(NodeBuilderFactory.class);
        nodeFactory = builderFactory.createNodeFactory(Alumno.class);
        
        getSwingBinder().bindTextFieldToLong(form.txtNroCompetencia, numero);
        getSwingBinder().bindComboBoxToObject(form.cboGanador, ganador, competidores);
        
        ChoiceField<SwingNode<Alumno>> compA = new ChoiceField<SwingNode<Alumno>>(form.txtCompetidorAzul, "Seleccione el competidor azul", nodeFactory.createRootNode(), nodeFactory.getNodeSelectedClass());
        compA.addAnnounceSupportTag(Alumno.class);
        getSwingBinder().bindChoiceToObject(compA, competidorAzulNode);
        
        ChoiceField<SwingNode<Alumno>> compR = new ChoiceField<SwingNode<Alumno>>(form.txtCompetidorRojo, "Seleccione el competidor azul", nodeFactory.createRootNode(), nodeFactory.getNodeSelectedClass());
        compR.addAnnounceSupportTag(Alumno.class);
        getSwingBinder().bindChoiceToObject(compR, competidorRojoNode);
    }

    private void configureValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(competidorAzulNode, "Selecione un competidor"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(competidorRojoNode, "Selecione un contrincante"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(ganador, "Selecione un ganador"));
    }

    private void configureView() {
        form.cboGanador.setModel(new DelegatingComboBoxModel<Competidor>(competidores.getList()));
        
        form.txtCompetidorAzul.setEnabled(false);
        form.txtCompetidorRojo.setEnabled(false);
        form.txtNroCompetencia.setEnabled(false);
        
        setTitle("Finalizar competencia...");
    }

    private void initPresentationModel() {
        competidorAzulNode.set(null);
        competidorRojoNode.set(null);
        ganador.set(null);
        numero.set(null);
    }
    
    public void initFinalizacionCompetenciaController(Competencia competencia) {
        numero.set(competencia.getNumero());
        competidorAzulNode.set(nodeFactory.createNode(competencia.getCompetidorAzul().getAlumno()));
        competidorRojoNode.set(nodeFactory.createNode(competencia.getCompetidorRojo().getAlumno()));
        
        List<Competidor> comps = new ArrayList<Competidor>();
        comps.add(competencia.getCompetidorAzul());
        comps.add(competencia.getCompetidorRojo());
        
        competidores.assignData(comps);
    }
    
    
}
