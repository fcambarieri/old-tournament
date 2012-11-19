package com.thorplatform.swing;

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Fernando
 */
public class DelegatingComboBoxModel<T> extends AbstractListModel implements ComboBoxModel {
    
    private List<T> list;
    private T selectedObject;

    public interface CellValueProvider {
        Object getCellValue(int rowIndex);
    }
    
    private CellValueProvider cellValueProvider;
    
    public DelegatingComboBoxModel(List<T> list) {
        this.list = list;
        installDefaultCellValueProvider();
    }
    
    private void installDefaultCellValueProvider() {
        setCellValueProvider(new CellValueProvider() {
            public Object getCellValue(int rowIndex) {
                return list.get(rowIndex);
            }
        });
    }


    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int index) {
        return cellValueProvider.getCellValue(index);
    }

    @SuppressWarnings("unchecked")
    public void setSelectedItem(Object object) {
        selectedObject = (T) object;
    }

    public Object getSelectedItem() {
        return selectedObject;
    }
    
    public void fireContentsChanged(Object source, int index0, int index1) {
        super.fireContentsChanged(source, index0, index1);
    }

    public void fireIntervalAdded(Object source, int index0, int index1) {
        super.fireIntervalAdded(source, index0, index1);
    }

    public void fireIntervalRemoved(Object source, int index0, int index1) {
        super.fireIntervalRemoved(source, index0, index1);
    }
    
    public CellValueProvider getCellValueProvider() {
        return cellValueProvider;
    }

    public void setCellValueProvider(CellValueProvider cellValueProvider) {
        this.cellValueProvider = cellValueProvider;
    } 
    
}
