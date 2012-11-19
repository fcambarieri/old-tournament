/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas;

import com.tdk.domain.ContactoPersona;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.table.SwingTableController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class ContactosPesonalesController extends SwingTableController<ContactoPersona>{

    private SwingControllerChangeEvent controllerChangeEvent = null;
    
    @Override
    protected ListTableModel<ContactoPersona> configureTable() {
        ListTableModel<ContactoPersona> model = new ListTableModel<ContactoPersona>();
        model.setColumnTitles(new String[]{"Tipo de contacto", "Valor", "Descripcion"});
        model.setColumnClasses(new Class[]{String.class, String.class, String.class});
        model.setCellValueProvider(new ListTableModel.CellValueProvider() {
            public Object getCellValue(int arg0, int arg1) {
                Object result = null;
                ContactoPersona cp = getTableList().get(arg0);
                switch(arg1) {
                    case 0:
                        result = cp.getTipoContacto();
                        break;
                    case 1: 
                        result = cp.getValor();
                        break;
                    case 2:
                        result = cp.getDescripcion();
                        break;
                }
                
                return result;
            }
        });
        return model;
    }

    @Override
    protected ContactoPersona agregarAction() {
        ContactoPersonaController controller = controllerFactory().createController(ContactoPersonaController.class);
        if (controller.showModal()) {
            return controller.crearContactoPersona();
        }
        return null;
    }

    @Override
    protected ContactoPersona editarAction(int index) {
        ContactoPersonaController controller = controllerFactory().createController(ContactoPersonaController.class);
        ContactoPersona cp = getTableList().get(index);
        controller.initControllerForUpdate(cp);
        if (controller.showModal()) {
            controller.modificarContactoPersona(getTableList().get(index));
            return getTableList().get(index);
        }
        return null;
    }

    @Override
    protected boolean canAcceptDialog() {
        return getTableList().getList().size() > 0;
    }
    
    public void setControllerNotifier(SwingControllerChangeEvent controllerNotifier) {
        this.controllerChangeEvent = controllerNotifier;
    }

    public SwingControllerChangeEvent getControllerNotifier() {
        return controllerChangeEvent;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        
        if(controllerChangeEvent != null)
            controllerChangeEvent.notifyEvent(evt);
    }

    @Override
    protected void quitarAction(ContactoPersona arg0) {
    }
}