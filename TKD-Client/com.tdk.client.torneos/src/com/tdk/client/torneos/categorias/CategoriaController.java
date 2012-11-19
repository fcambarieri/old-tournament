/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias;

import com.tdk.domain.Sexo;
import com.tdk.domain.torneo.Categoria;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.StringPropertyValidator;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author fernando
 */
public class CategoriaController extends SwingController {

    private CategoriaForm form = new CategoriaForm();
    private SwingControllerChangeEvent controllerNotifier;
    private final Property<Integer> edadInferior = new Property<Integer>("edadInferior");
    private final Property<Integer> edadSuperior = new Property<Integer>("edadSuperior");
    private final Property<String> descripcion = new Property<String>("descripcion");
    private final Property<Sexo> sexoSelected = new Property<Sexo>("sexo");
    private final ListProperty<Sexo> sexoList = new ListProperty<Sexo>("sexoList");

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {

        if (controllerNotifier != null) {
            controllerNotifier.notifyEvent(evt);
        }
    }

    public SwingControllerChangeEvent getControllerNotifier() {
        return controllerNotifier;
    }

    public void setControllerNotifier(SwingControllerChangeEvent controllerNotifier) {
        this.controllerNotifier = controllerNotifier;
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
        initPresentationModel();
    }

    private void configureBindings() {
        getSwingBinder().bindSpinnerToInteger(form.spnEdadInferior, edadInferior);
        getSwingBinder().bindSpinnerToInteger(form.spnEdadSuperior, edadSuperior);
        getSwingBinder().bindTextComponentToString(form.txtDescripcion, descripcion);
        getSwingBinder().bindComboBoxToObject(form.cboSexo,sexoSelected, sexoList);
    }

    private void configureView() {
        form.spnEdadInferior.setModel(new SpinnerNumberModel(1, 0, null, 1));
        form.spnEdadSuperior.setModel(new SpinnerNumberModel(1, 0, null, 1));
        sexoList.assignData(Arrays.asList(Sexo.values()));
        form.cboSexo.setModel(new DelegatingComboBoxModel<Sexo>(sexoList.getList()));
        
        setSexoVisible(false);
    }

    private void initPresentationModel() {
        clearFields();
    }

    public Integer getEdadInferior() {
        return edadInferior.get();
    }

    public Integer getEdadSuperior() {
        return edadSuperior.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }
    
    public Sexo getSexo() {
        return sexoSelected.get();
    }
    
    public void setSexo(Sexo sexo) {
        sexoSelected.set(sexo);
    }

    public AbstractSwingValidator getDescripcionValidator() {
        return new StringPropertyValidator(descripcion, "Ingrese una descripción", new Integer(3), new Integer(64));
    }

    public AbstractSwingValidator getEdadesValidator() {
        return new AbstractSwingValidator("La edad inferior debe ser menor a la superior") {

            @Override
            public boolean validate() {
                return edadInferior.get() < edadSuperior.get();
            }
        };
    }
    
    public void clearFields() {
        descripcion.set(null);
        edadInferior.set(new Integer(0));
        edadSuperior.set(new Integer(0));
        sexoSelected.set(null);
    }
    
    public void initControllerForUpdate(Categoria categoria) {
        descripcion.set(categoria.getDescripcion());
        edadInferior.set(categoria.getEdadInferior());
        edadSuperior.set(categoria.getEdadSuperior());
    }
    
    public Categoria modificarCategoria(Categoria categoria) {
        categoria.setDescripcion(getDescripcion());
        categoria.setEdadInferior(getEdadInferior());
        categoria.setEdadSuperior(getEdadSuperior());
        return categoria;
        
    }
    
    public void setSexoVisible(boolean visible) {
        form.cboSexo.setVisible(visible);
        form.lblSexo.setVisible(visible);
    }
}
