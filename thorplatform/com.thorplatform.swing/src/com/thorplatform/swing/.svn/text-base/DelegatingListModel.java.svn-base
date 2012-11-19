/*
 * DelegatingListModel.java
 *
 * Created on 7 de febrero de 2008, 7:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.thorplatform.swing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/**
 *
 * @author fcambarieri
 */
public class DelegatingListModel<T> extends AbstractListModel implements ListModel {
   
    private List<T> list;
    private T selectedObject ;

    public interface CellValueProvider {
        Object getCellValue(int rowIndex);
    }
    
    private CellValueProvider cellValueProvider;
   
    public DelegatingListModel(List<T> list) {
        this.list = list;
        installDefaultCellValueProvider();
    }
    
    private void installDefaultCellValueProvider() {
        setCellValueProvider(new CellValueProvider() {
            public Object getCellValue(int rowIndex) {
                return list.toArray()[rowIndex];
            }
        });
    }
   
    public void assignList(List<T> list) {
        this.list = list;
    }
   
    public List<T> getList() {
        return list;
    }
   
    public int getSize() {
        return list.size();
    }
   
    @SuppressWarnings("unchecked")
    public Object getElementAt(int index) {
        return cellValueProvider.getCellValue(index);
    }
   
    @SuppressWarnings("unchecked")
    public void setSelectedItem(Object object) {
        selectedObject = (T) object;
    }
   
    public T getSelectedItem() {
        return selectedObject;
    }
   
    @Override
    public void fireContentsChanged(Object source, int index0, int index1) {
        super.fireContentsChanged(source, index0, index1);
    }
   
    @Override
    public void fireIntervalAdded(Object source, int index0, int index1) {
        super.fireIntervalAdded(source, index0, index1);
    }
   
    @Override
    public void fireIntervalRemoved(Object source, int index0, int index1) {
        super.fireIntervalRemoved(source, index0, index1);
    }
   
    public void add(T element) {
        if (list.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }
   
    public void addAll(T elements[]) {
        Collection<T> c = Arrays.asList(elements);
        list.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }
   
    public void clear() {
        list.clear();
        fireContentsChanged(this, 0, getSize());
    }
   
    public boolean contains(Object element) {
        return list.contains(element);
    }
   
    public T firstElement() {
        return list.size() > 0 ? list.get(0) : null ;
    }
   
    public Iterator iterator() {
        return list.iterator();
    }
   
    public T lastElement() {
        return list.size() > 0 ? list.get(list.size() - 1) : null;
    }
   
    public boolean removeElement(T element) {
        boolean removed = list.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }
    
    public CellValueProvider getCellValueProvider() {
        return cellValueProvider;
    }

    public void setCellValueProvider(CellValueProvider cellValueProvider) {
        this.cellValueProvider = cellValueProvider;
    }   
    
    
}