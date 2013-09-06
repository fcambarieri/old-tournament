/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import com.tdk.domain.torneo.Competidor;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

/**
 *
 * @author sabon
 */
public class LonelyCompetidorsController extends SwingModalController
{
    private final LonelyCompetidorsForm form = new LonelyCompetidorsForm();
    private final ListProperty<Competidor> tableList = new ListProperty<Competidor>("tableList");
    private final Property<Integer> index = new Property<Integer>("tableIndex");
    private ListTableModel<Competidor> listTableModel;
    
    
    
    @Override
    protected JButton getAcceptButton() {
        return form.btnCerrar;
    }

    @Override
    protected JButton getCancelButton() {
         return new JButton();
    }

    @Override
    protected JPanel getForm() {
       return form;
    }

    
    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
        
    }

    private void configureView() {
        this.listTableModel = configureTable();
        
//        HighlighterPipeline highlighter = new HighlighterPipeline(new Highlighter[]{AlternateRowHighlighter.quickSilver});
//        form.xtbCompetidores.setHighlighters(highlighter);
        form.xtbCompetidores.setModel(listTableModel);

    }

    private void configureBindings() {
        getSwingBinder().bindSingleSelectionTable(form.xtbCompetidores, tableList, index);
    }
    
    
    protected ListTableModel<Competidor> configureTable() {
        final ListTableModel<Competidor> model = new ListTableModel<Competidor>();
        model.setColumnTitles(new String[]{"Insitución", "Competidor", "Sexo", "Cinturon", "Categoria Forma", "Categoria Lucha"});
        model.setColumnClasses(new Class[]{String.class,String.class,String.class,String.class,String.class, });
        model.setCellValueProvider(new ListTableModel.CellValueProvider() {

            public Object getCellValue(int i, int i1) {
                Object result = null;
                Competidor competidor = tableList.get(i);
                switch(i1) {
                    case 0 :
                        result = competidor.getAlumno().getInstitucion().getDisplayName();
                        break;
                    case 1:
                        result = competidor.getDisplayCompetidor();
                        break;
                    case 2:
                        result = competidor.getAlumno().getPersonaFisica().getSexo().name();
                        break;
                    case 3:
                        result = competidor.getCinturon().getDescripcion();
                    case 4:
                        if (competidor.getCompetidorCategoriaForma() != null) {
                             result = competidor.getCompetidorCategoriaForma().getCategoriaForma().getDisplay();
                        }
                        break;
                    case 5:
                        if (competidor.getCompetidorCategoriaLucha()!= null) {
                             result = competidor.getCompetidorCategoriaLucha().getDisplay();
                        }
                        break;
                       
                }
                
                return result;
            }
        });
     
        return model;
    }
    
    public void setCompetidores(List<Competidor> competidores) {
        tableList.assignData(competidores);
    }
}
