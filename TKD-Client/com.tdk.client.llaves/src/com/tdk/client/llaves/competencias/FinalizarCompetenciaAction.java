/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves.competencias;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.llaves.graph.AbstractNodeAction;
import com.tdk.domain.torneo.competencia.Competencia;
import com.tdk.domain.torneo.competencia.TipoEstadoCompetencia;
import com.tdk.services.CompetenciaServiceRemote;
import com.thorplatform.swing.SwingControllerFactory;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class FinalizarCompetenciaAction extends AbstractNodeAction {


    public FinalizarCompetenciaAction() {
        this(null);
    }
    public FinalizarCompetenciaAction(Competencia c) {
        setCompetencia(c);
    }

    public void actionPerformed(ActionEvent arg0) {
        
        if (competencia != null) {
            
            if (competencia.getEstadoActual().getTipoEstado().equals(TipoEstadoCompetencia.INICIADA) ||
                    competencia.getEstadoActual().getTipoEstado().equals(TipoEstadoCompetencia.EN_CONDICIONES)) {

                SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
                FinalizarCompetenciaController controller = scf.createController(FinalizarCompetenciaController.class);
                controller.initFinalizacionCompetenciaController(competencia);

                if (controller.showModal()) {

                    String msg = "¿Desea finalizar la competencia número: " + competencia.getNumero() +
                            " con el ganador "+ controller.ganador.get().getDisplayCompetidor() + "?";
                    String title = "Finalizar competencia";
                    NotifyDescriptor.Confirmation question = new NotifyDescriptor.Confirmation(msg, title,
                            NotifyDescriptor.OK_CANCEL_OPTION,
                            NotifyDescriptor.WARNING_MESSAGE);

                    if (DialogDisplayer.getDefault().notify(question) == NotifyDescriptor.OK_OPTION) {
                        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
                        CompetenciaServiceRemote service = sf.getService(CompetenciaServiceRemote.class);
                        
                        
                        competencia.setCompetidorGanador(controller.ganador.get());

                        try {
                            service.finalizarCompentecia(competencia);
                            notifyListeners(competencia);
                            notifyParent(competencia);
                        } catch (Throwable ex) {
                            NotifyDescriptor.Exception msgEx = new NotifyDescriptor.Exception(ex);
                            DialogDisplayer.getDefault().notify(msgEx);
                        }
                    }
                }

            } else {
                NotifyDescriptor.Message message = new NotifyDescriptor.Message("No puede finalizar esta competencia " +
                        "ya que encuentra Finalizada, Pendiente o Cancelada");
                DialogDisplayer.getDefault().notify(message);
            }
        }
    }

    @Override
    public String getTitle() {
        return "Finalizar competencia...";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    private void notifyParent(Competencia competencia) {
        if (competencia.getCompetenciaPadre() != null) {
            ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
            CompetenciaServiceRemote service = sf.getService(CompetenciaServiceRemote.class);
            Competencia padre = service.recuperarCompetencia(competencia.getCompetenciaPadre().getId(), true);
            notifyListeners(padre);
        }
    }
}
