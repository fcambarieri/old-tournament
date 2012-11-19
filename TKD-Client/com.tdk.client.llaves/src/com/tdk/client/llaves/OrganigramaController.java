/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves;

import com.tdk.client.nodes.BinaryTree;
import com.tdk.client.swing.node.JBTree;
import com.tdk.utils.CalcBitInverso;
import com.thorplatform.swing.SwingController;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class OrganigramaController extends SwingController {

    private JBTree form = new JBTree();
    private BinaryTree<Alumno> model = new BinaryTree<Alumno>();
    
    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
    }

    private void configureView() {
        model.convertListToBinaryTree(crearAlumnos());
        
        form.setModel(model);
    }

    
    private List<Alumno> crearAlumnos() {
        List<Alumno> alumnos = new ArrayList<Alumno>();
        List<Alumno> orderAlumnos = new ArrayList<Alumno>();
        
       
        Escuela esc1 = new Escuela("Andina");
        Escuela esc2 = new Escuela("Kukiwon");
        Escuela esc3 = new Escuela("Tae");
        
        alumnos.add(new Alumno("Fer", esc1));
        alumnos.add(new Alumno("Andrea", esc1));
        alumnos.add(new Alumno("Yoli", esc2));
        alumnos.add(new Alumno("Beatriz", esc2));
        
        OrderByInverseBit<Alumno> order = new OrderByInverseBit<Alumno>();
        
        order.order(alumnos);
        
        return alumnos;
    }
}

class OrderByInverseBit <T> {

    @SuppressWarnings("unchecked")
    public List<T> order(List<T> list) {
        int[] index = CalcBitInverso.bitInverse(list.size());
        List<InverseBitItem<T>> items = new ArrayList<InverseBitItem<T>>();
        for(int i = 0; i < index.length ; i++) {
            items.add(new InverseBitItem<T>(list.get(i), index[i]));
        }
        
        Collections.sort(items, new Comparator<InverseBitItem>() {

            public int compare(InverseBitItem arg0, InverseBitItem arg1) {
                return arg0.getIndexValue().compareTo(arg1.getIndexValue());
            }

        });
        
        list.clear();
        
        for(InverseBitItem i : items) {
            list.add((T) i.getValue());
        }
        
        return list;
    }
}

class InverseBitItem <T> {

    private Integer indexValue;
    private T value;
    
    public InverseBitItem(T value, int index) {
        this.value = value;
        this.indexValue = new Integer(index);
    }

    public Integer getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    
}
class Escuela {
    
    private String nombre;
    
    public Escuela(String n){
        setNombre(n);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}

class Alumno {
    
    private String nombre;
    private Escuela escuela;

    public Alumno(String n, Escuela esc) {
        setNombre(n);
        setEscuela(esc);
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Escuela getEscuela() {
        return escuela;
    }

    public void setEscuela(Escuela escuela) {
        this.escuela = escuela;
    }

    @Override
    public String toString() {
        return escuela.getNombre() + " - " + nombre;
    }
    
    
}
