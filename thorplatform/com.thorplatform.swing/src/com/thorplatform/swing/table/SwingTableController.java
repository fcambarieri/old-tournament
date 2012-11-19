/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing.table;

import com.thorplatform.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public abstract class SwingTableController<T> extends SwingController {

    private SwingTableForm form = new SwingTableForm();
    private final ListProperty<T> tableList = new ListProperty<T>("tableList");
    private final Property<Integer> index = new Property<Integer>("tableIndex");
    private SwingControllerChangeEvent swingControllerChangeEvent;
    private ListTableModel<T> listTableModel;
    private PatternFilter filter;
    private SwingTableFilter swingTableFilter;
    
    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        if (evt.getSource() == tableList) {
            boolean enabled = tableList.getList().size() > 0;
            setEnabledEditar(enabled);
            setEnabledQuitar(enabled);
        }
        
        if (getSwingControllerChangeEvent() != null)
            getSwingControllerChangeEvent().notifyEvent(evt);

    }

    public ListProperty<T> getTableList() {
        return tableList;
    }

    public Property<Integer> getIndex() {
        return index;
    }

    public void setEnabledQuitar(boolean enabled) {
        form.btnQuitar.setEnabled(enabled);
    }

    public void setEnabledEditar(boolean enabled) {
        form.btnEditar.setEnabled(enabled);
    }

    public void setEnabledAgregar(boolean enabled) {
        form.btnAgregar.setEnabled(enabled);
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
        installActions();
    }

    private void configureView() {
        this.listTableModel = configureTable();
        form.xTable.setModel(listTableModel);
        HighlighterPipeline highlighter = new HighlighterPipeline(new Highlighter[]{AlternateRowHighlighter.quickSilver});
        form.xTable.setHighlighters(highlighter);

        configureTaskPane();
    }

    private void configureBindings() {
        getSwingBinder().bindSingleSelectionTable(form.xTable, tableList, index);
    }
    
    private void installActions() {
        form.btnAgregar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                T newValue = agregarAction();
                if (newValue != null) {
                    tableList.add(newValue);
                }
            }
        });

        form.btnEditar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (index.get() != null && index.get().intValue() >= 0) {
                    T newValue = editarAction(index.get().intValue());
                    if (newValue != null) {
                        form.xTable.repaint();
                    }
                }
            }
        });

        form.btnQuitar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                quitarAction();
            }
        });

        
        
    }
    
    protected void quitarAction() {
        if (index.get() != null && index.get().intValue() >= 0) {
            quitarAction(tableList.get(index.get().intValue()));
            tableList.remove(index.get().intValue());
        }
    }

    protected JButton getAgregarButton() {
        return form.btnAgregar;
    }

    protected JButton getEditarButton() {
        return form.btnEditar;
    }

    protected JButton getQuitarButton() {
        return form.btnQuitar;
    }

    protected JXTable getJXTable() {
        return form.xTable;
    }

    private void configureTaskPane() {
        swingTableFilter = new SwingTableFilter();
        setFilter(new PatternFilter(null, 0, 0));

        Filter[] filterArray = {getFilter()};
        FilterPipeline filters = new FilterPipeline(filterArray);
        getJXTable().setFilters(filters);

        swingTableFilter.getJxSearchPanel().setPatternFilter(filter);
        form.xpnlCentral.add(swingTableFilter, BorderLayout.SOUTH);

    }

    public void setSearchPanelVisible(boolean visible) {
        if (swingTableFilter != null)
            swingTableFilter.setVisible(visible);
    }

    public JPanel getPanel() {
        return getForm();
    }

    protected SwingControllerFactory controllerFactory() {
        return Lookup.getDefault().lookup(SwingControllerFactory.class);
    }

    protected abstract ListTableModel<T> configureTable();

    protected abstract T agregarAction();

    protected abstract T editarAction(int index);
    
    protected abstract void quitarAction(T value);

    public PatternFilter getFilter() {
        return filter;
    }

    public void setFilter(PatternFilter filter) {
        this.filter = filter;
    }

    public SwingControllerChangeEvent getSwingControllerChangeEvent() {
        return swingControllerChangeEvent;
    }

    public void setSwingControllerChangeEvent(SwingControllerChangeEvent swingControllerChangeEvent) {
        this.swingControllerChangeEvent = swingControllerChangeEvent;
    }
    
}