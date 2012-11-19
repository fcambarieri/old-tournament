/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos;

import com.tdk.client.utils.EstadoController;
import com.tdk.client.utils.VigenciaControllerFactory;
import com.tdk.client.utils.VigenciaInterface;
import com.tdk.domain.torneo.EstadoTorneo;
import com.tdk.domain.torneo.TipoEstadoTorneo;
import com.tdk.domain.torneo.Torneo;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.StringPropertyValidator;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class TorneoController extends SwingModalController implements SwingControllerChangeEvent {

    private TorneoForm form = new TorneoForm();
    private EstadoController<EstadoTorneo, TipoEstadoTorneo> estadoController;
    private VigenciaInterface vigenciaController;
    
    //Model
    private final Property<String> nombre = new Property<String>("nombre");
    
    
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

    public void notifyEvent(PropertyChangeEvent evt) {
        this.propertyChange(evt);
    }

    @Override
    public void initController() {
        super.initController();
        installValidators();
        configureView();
        configureBindings();
        initPresentationModel();
    }

    private void configureBindings() {
        getSwingBinder().bindTextComponentToString(form.txtNombre, nombre);
    }

    @SuppressWarnings("unchecked")
    private void configureView() {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        estadoController = scf.createController(EstadoController.class);
        estadoController.setControllerChangeEvent(this);
        estadoController.initEstadoController(Arrays.asList(TipoEstadoTorneo.values()));
        
        form.pnlEstado.setLayout(new BorderLayout());
        form.pnlEstado.add(estadoController.getPanel(), BorderLayout.CENTER);
        
        configureVigencia();
        
    }
    
    private void configureVigencia() {
        VigenciaControllerFactory vigenciaFactory = Lookup.getDefault().lookup(VigenciaControllerFactory.class);
        vigenciaController = vigenciaFactory.createVigenciaInterface();
        vigenciaController.setControllerChangeEvent(this);
        for (AbstractSwingValidator asv : vigenciaController.getValidators()) {
            getSwingValidator().addSwingValidator(asv);
        }
        
        
        form.pnlVigencia.setLayout(new BorderLayout());
        form.pnlVigencia.add(vigenciaController.getPanel(), BorderLayout.CENTER);
    }

    private void initPresentationModel() {
        nombre.set(null);
    }

    private void installValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new StringPropertyValidator(nombre, "Ingrese un nombre", new Integer(3), new Integer(64)));
    }
    
    public void initTorneoController(Torneo torneo) {
        nombre.set(torneo.getNombre());
        vigenciaController.initForUpdate(torneo.getFechaDesde(), torneo.getFechaHasta());
        estadoController.initEstadoController(torneo.getEstadoTorneo().getTipoEstadoTorneo(), torneo.getHistorialEstados(), torneo.getEstadoTorneo().getDisplayDate());
    }
    
    public Torneo crearTorneo() {
        Torneo torneo = new Torneo();
        EstadoTorneo estado = new EstadoTorneo();
        estado.setFechaDesde(estadoController.getFechaDesde());
        torneo.setEstadoTorneo(estado);
        return modificarTorneo(torneo);
    }

    public Torneo modificarTorneo(Torneo torneo) {
        torneo.setFechaDesde(vigenciaController.getFechaDesde());
        torneo.setFechaHasta(vigenciaController.getFechaHasta());
        torneo.setNombre(nombre.get());
        torneo.getEstadoTorneo().setTipoEstadoTorneo(estadoController.getTipoEstado());
        return torneo;
    }
    
}
