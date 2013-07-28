/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.llaves.competencias.CompetenciaTopComponent;
import com.tdk.domain.torneo.competencia.Competencia;
import com.tdk.domain.torneo.competencia.Llave;
import com.tdk.domain.torneo.competencia.LlaveLucha;
import com.tdk.services.CompetenciaServiceRemote;
import com.thorplatform.swing.SwingNode;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import com.tdk.client.llaves.competencias.FinalizarCompetenciaAction;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.competencia.EstadoCompetencia;
import com.tdk.domain.torneo.competencia.TipoEstadoCompetencia;


/**
 *
 * @author fernando
 */
public class LlaveNode extends SwingNode<Llave> {

    public LlaveNode(Llave llave) {
        super(new LlaveNodeChildren(llave), llave);
        setIconBaseWithExtension("com/tdk/client/llaves/llaves-16x16.png");
    }

    public String getDisplay() {
//        //getValue().
//        //return getValue().getDisplay();
//        StringBuilder sb = new StringBuilder();
//        Llave llave = getValue();
//        sb.append(getValue().getTorneo().getNombre());
//        sb.append(getValue().getCategoria().getDescripcion());
//        sb.append(getValue().getCinturon());
//        
        //return String.format("%s %s %s", llave.getTorneo().getNombre(), llave.getCinturon().getDescripcion());
         Llave llave = getValue();
          return String.format("<strong>%s</strong> %s %s ", 
                  llave.getTorneo().getNombre(),
                  llave.getCategoria().getDescripcion(),
                  llave.getCinturon().getDescripcion());
    }

    @Override
    public String getHtmlDisplayName() {
        //return super.getHtmlDisplayName(); //To change body of generated methods, choose Tools | Templates.
          Llave llave = getValue();
          return String.format("<strong>%s</strong> %s %s ", 
                  llave.getTorneo().getNombre(),
                  llave.getCategoria().getDescripcion(),
                  llave.getCinturon().getDescripcion());
    }
    
    

    public Action[] getActions(boolean arg0) {
        return new Action[]{ new EliminarLlaveAction(getValue()) , new ShowLlaveAction(getValue())};
    }
}
class LlaveNodeChildren extends Children.Keys<Integer> {

    private static final Integer TORNEO = new Integer(0);
    private static final Integer CINTURON = new Integer(1);
    private static final Integer CATEGORIA = new Integer(2);
    private static final Integer COMPETENCIAS = new Integer(3);
    private List<Integer> categorias = null;
    private Llave llave;

    public LlaveNodeChildren(Llave llave) {
        this.llave = llave;
    }

    @Override
    protected void addNotify() {
        if (llave != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(TORNEO);
            categorias.add(CINTURON);
            categorias.add(CATEGORIA);
            categorias.add(COMPETENCIAS);

            setKeys(categorias);
        }
    }

    @Override
    protected Node[] createNodes(Integer key) {
        AbstractNode node = null;
        if (key.equals(TORNEO)) {
            node = new NodeInfo("Torneo: " + llave.getTorneo().getNombre(), "com/tdk/client/torneos/torneo-14x16.png");
        } else if (key.equals(CINTURON)) {
            node = new NodeInfo("Cinturon: " + llave.getCinturon().getDescripcion(), "com/tdk/client/llaves/cinturones-16x16.png");
        } else if (key.equals(CATEGORIA)) {
            node = new NodeInfo("Categoria: " + llave.getCategoria().getDisplay(), categoriaIcon());
        } else if (key.equals(COMPETENCIAS)) {
            node = new CompetenciaNode(llave.getCompetencia(), "com/tdk/client/llaves/vs-16x16.gif");
        }

        return new Node[]{node};
    }

    private String categoriaIcon() {
        if (llave instanceof LlaveLucha) {
            return "com/tdk/client/torneos/combate-16x16.png";
        } else {
            return "com/tdk/client/torneos/formas-16x16.png";
        }
    }
}

class NodeInfo extends AbstractNode {

    private String info;

    public NodeInfo(String info, String icon) {
        super(Children.LEAF);
        setIconBaseWithExtension(icon);
        this.info = info;
    }

    @Override
    public String getDisplayName() {
        return info;
    }
}


class CompetenciaNode extends AbstractNode {

    private Competencia competencia;

    public CompetenciaNode(Competencia c, String icon) {
        super(new CompetenciaNodeChildren(c, icon));
        this.competencia = c;
        setIconBaseWithExtension(icon);
    }

    @Override
    public String getDisplayName() {
        String str = "Competencia Nro(" + competencia.getNumero() + ")";

        return str;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new FinalizarCompetenciaAction(competencia)};
    }
    
    
}
class CompetenciaNodeChildren extends Children.Keys<Integer> {

    private static final Integer COMPETIDOR_AZUL = new Integer(0);
    private static final Integer COMPETIDOR_ROJO = new Integer(1);
    private static final Integer COMPETENCIA_IZQ = new Integer(2);
    private static final Integer COMPETENCIA_DER = new Integer(3);
    private static final Integer ESTADO = new Integer(4);
    private static final Integer HISTORIAL_ESTADO = new Integer(5);
    private Competencia competencia;
    private String icon;
    private List<Integer> list;

    public CompetenciaNodeChildren(Competencia c, String icon) {
        this.competencia = c;
        this.icon = icon;
    }

    @Override
    protected void addNotify() {
        if (competencia != null) {
            list = new ArrayList<Integer>();
            list.add(ESTADO);
            list.add(COMPETIDOR_AZUL);
            list.add(COMPETIDOR_ROJO);

            if (competencia.getCompentenciaLeft() != null) {
                list.add(COMPETENCIA_IZQ);
            }
            if (competencia.getCompentenciaRight() != null) {
                list.add(COMPETENCIA_DER);
            }
            setKeys(list);
        }
    }

    @Override
    protected Node[] createNodes(Integer key) {

        AbstractNode node = null;
        String str = "Sin info";
        if (key.equals(COMPETIDOR_AZUL)) {
            node = new CompetidorNode(competencia, competencia.getCompetidorAzul(),
                    "com/tdk/client/llaves/com_azul-16x16.png");
        } else if (key.equals(COMPETIDOR_ROJO)) {
            node = new CompetidorNode(competencia, competencia.getCompetidorRojo(),
                    "com/tdk/client/llaves/com_rojo-16x16.png");
        } else if (key.equals(COMPETENCIA_IZQ)) {
            node = new CompetenciaNode(competencia.getCompentenciaLeft(), icon);
        } else if (key.equals(COMPETENCIA_DER)) {
            node = new CompetenciaNode(competencia.getCompentenciaRight(), icon);
        } else if (key.equals(ESTADO)) {
            node = new EstadoCompetenciaNode(competencia.getEstadoActual());
        }

        return new Node[]{node};
    }
    
}


class EstadoCompetenciaNode extends AbstractNode {
    
    private EstadoCompetencia estado;
    
    public EstadoCompetenciaNode(EstadoCompetencia estado) {
        super(Children.LEAF);
        this.estado = estado;
        setIconBaseWithExtension("com/tdk/client/llaves/estado.png");
    }

    @Override
    public String getHtmlDisplayName() {
        boolean state = estado.getTipoEstado().equals(TipoEstadoCompetencia.INICIADA)
                || estado.getTipoEstado().equals(TipoEstadoCompetencia.EN_CONDICIONES);
        
        String str = "Estado: " +color(state, estado.getDisplayWithOutDate());
        str += " " + estado.getDisplayDate();        
        return str;
    }
    
    private String color(boolean value, String str) {
        String newStr = "<font color=" + (value ? "'00FF00'" : "'FF0000'") + ">" + str + "</font> ";
        return newStr;
    }
}

/******************************************************************************/
//                                  Actions
class CompetidorNode extends AbstractNode {
    
    private Competencia competencia;
    private Competidor competidor;
    
    public CompetidorNode(Competencia competencia, Competidor competidor, String icon) {
        super(Children.LEAF);
        this.competencia = competencia;
        this.competidor = competidor;
        setIconBaseWithExtension(competidorIcon(icon));
    }
    
    private String competidorIcon(String strOriginal) {
        String str = strOriginal;
        if (!isGanador() && competencia.getEstadoActual().getTipoEstado().equals(TipoEstadoCompetencia.FINALIZADA))
            str = "com/tdk/client/llaves/com_gris-16x16.png";
        
        return str;
    }
    
    private boolean isGanador() {
        return competencia.getCompetidorGanador() != null && 
                competencia.getCompetidorGanador().equals(competidor);
    }

    @Override
    public String getHtmlDisplayName() {
        if (competidor != null) {
            return isGanador() ? ("<font> <b>" + competidor.getDisplayCompetidor() + "<b> </font>") 
                    : competidor.getDisplayCompetidor();
        } else {
            return "Sin información";
        }
        
    }
    
    
}
/******************************************************************************/
class EliminarLlaveAction extends AbstractAction {

    private Llave llave;

    public EliminarLlaveAction(Llave llave) {
        super("Eliminar llave...");
        this.llave = llave;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (llave != null) {
            String msg = "¿Desea eliminar la llave " + llave.getDisplay() + "?";
            String title = "Eliminar llave";
            NotifyDescriptor.Confirmation question = new NotifyDescriptor.Confirmation(msg, title,
                    NotifyDescriptor.OK_CANCEL_OPTION,
                    NotifyDescriptor.WARNING_MESSAGE);

            if (DialogDisplayer.getDefault().notify(question) == NotifyDescriptor.OK_OPTION) {
                ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
                CompetenciaServiceRemote service = sf.getService(CompetenciaServiceRemote.class);
                service.eliminarLlave(llave.getId());
            }
        }
    }
}

class ShowLlaveAction extends AbstractAction {

    private Llave llave;

    public ShowLlaveAction(Llave llave) {
        super("Show llave...");
        this.llave = llave;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (llave != null) {
            TopComponent win = new CompetenciaTopComponent(llave);
            win.open();
            win.requestActive();
        }
    }
}


