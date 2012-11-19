package com.thorplatform.swing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ListTableModel<T> extends AbstractTableModel {
    
    private List<T> list;
    private String[] columnTitles;
    private Class[] columnClasses;
    private String[] columnMethodNames;
    
    private CellValueProvider cellValueProvider;
    
    public interface CellValueProvider {
        Object getCellValue(int rowIndex, int columnIndex);
    }
    
    public ListTableModel() {
        list = new ArrayList<T>();
        installDefaultDependencies();
    }
    
    private void installDefaultDependencies() {
        cellValueProvider = new CellValueProvider() {
            public Object getCellValue(int rowIndex, int columnIndex) {
                Object result = null;
                
                T item = list.get(rowIndex);
                
                String[] methods = columnMethodNames[columnIndex].split("\\.");
                try {
                    Method method = null; Object obj = item;
                    for (int i = 0; i < methods.length - 1; i++) {
                        method = obj.getClass().getMethod(methods[i], (Class[]) null);
                        obj = method.invoke(obj, (Object[]) null);
                    }
                    method = obj.getClass().getMethod(methods[methods.length - 1], (Class[]) null);
                    result = formatValue(columnIndex, method.invoke(obj, (Object[]) null));
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
                return result;
            }
        };
    }
    
    public int getRowCount() {
        return list.size();
    }
    
    public int getColumnCount() {
        return columnTitles.length;
    }
    
    public String getColumnName(int columnIndex) {
        return columnTitles[columnIndex];
    }
    
    public Class getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return cellValueProvider.getCellValue(rowIndex, columnIndex);
    }
    
    protected Object formatValue(int columnIndex, Object value) {
        return value;
    }
    
    public void setColumnTitles(String[] titles) {
        columnTitles = titles;
    }
    
    public void setColumnMethodNames(String[] methodNames) {
        columnMethodNames = methodNames;
    }
    
    public void setColumnClasses(Class[] classes) {
        columnClasses = classes;
    }
    
    public void assignData(List<T> data) {
        if (data.size() > 0)
            data.clear();
        
        data.addAll(data);
        
        fireTableDataChanged();
    }
    
    public void clear() {
        list.clear();
        
        fireTableDataChanged();
    }
    
    public void add(T object) {
        list.add(object);
        int modelIndex = list.size() - 1;
        
        // fireTableRowsInserted(modelIndex, modelIndex);
        fireTableDataChanged();
    }
    
    public T get(int index) {
        return list.get(index);
    }
    
    public void remove(int index) {
        list.remove(index);
        
        fireTableRowsDeleted(index, index);
    }
    
    public void remove(T object) {
        int index = list.indexOf(object);
        list.remove(index);
        
        fireTableRowsDeleted(index, index);
    }
    
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
        
        fireTableDataChanged();
    }
    
    public void setCellValueProvider(CellValueProvider cellValueProvider) {
        this.cellValueProvider = cellValueProvider;
    }
    
    public CellValueProvider getCellValueProvider() {
        return cellValueProvider;
    }
    
}
