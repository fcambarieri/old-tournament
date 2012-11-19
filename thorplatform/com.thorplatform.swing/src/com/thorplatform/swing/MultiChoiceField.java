/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class MultiChoiceField<T> {
    
    private static final String EMPTY_STRING = "";
    
    private JTextField textField;
    private String title;
    private ChoiceRootNode choiceRootNode;
    private Class selectableClass;
    private Class tagAnnounceSupport;
    
    private List<T> choice;
    
    private ArrayList<ChoiceChangeListener> choiceChangeListeners;
    
    private boolean writeLock = false;
    
    private Border validChoiceBorder;
    private Border invalidChoiceBorder;
    
    public interface ChoiceChangeListener {
        
        void onChoiceChanged();
        
    }
    
    public MultiChoiceField(JTextField textField, String title, ChoiceRootNode choiceRootNode, Class selectableClass) {
        this.textField = textField;
        this.title = title;
        this.choiceRootNode = choiceRootNode;
        this.selectableClass = selectableClass;
        choiceChangeListeners = new ArrayList<ChoiceChangeListener>();
        createBorders();
        installActionListener();
        installDocumentListener();
        initChoice();
    }
    
    private void createBorders() {
        validChoiceBorder = textField.getBorder();
        invalidChoiceBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }
    
    private void installDocumentListener() {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (!writeLock && getChoice() != null)
                    setChoice(null);
            }
            
            public void removeUpdate(DocumentEvent e) {
                if (!writeLock && getChoice() != null)
                    setChoice(null);
            }
            
            public void changedUpdate(DocumentEvent e) {
                if (!writeLock && getChoice() != null)
                    setChoice(null);
            }
        });
    }
    
    private void installActionListener() {
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAction();
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    private void textAction() {
        SwingControllerFactory swingControllerFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);
        ChoiceTreeViewController controller = swingControllerFactory.createController(ChoiceTreeViewController.class);
        controller.setTitle(this.title);
        controller.setChoiceRootNode(this.choiceRootNode);
        controller.setSelectableClass(this.selectableClass);
        controller.installAnnounceSupport(tagAnnounceSupport);
        
        choiceRootNode.replaceKeys(textField.getText());
        
        // ac� tengo oportunidad de no abrir el dialog si
        // los resultados son unitarios.
        
        if (controller.showModal()) 
            setChoice((List<T>)controller.selectedNodes.getList());
        else
            setChoice(null);
    }
    
    public void addAnnounceSupportTag(Class tag) {
        this.tagAnnounceSupport = tag;
    }
    
    public List<T> getChoice() {
        return choice;
    }
    
    public synchronized void addChoiceChangeListener(ChoiceChangeListener listener) {
        choiceChangeListeners.add(listener);
    }
    
    public synchronized void removeChoiceChangeListener(ChoiceChangeListener listener) {
        choiceChangeListeners.remove(listener);
    }
    
    public void setChoice(List<T> newValue) {
        if (writeLock)
            return;
        
        writeLock = true;
        try {
            List<T> oldValue = choice;
            if (newValue != null) {
                if (!newValue.equals(oldValue)) {
                    choice = newValue;
                    textField.setText(newValue.toString());
                    textField.setBorder(validChoiceBorder);
                    notifyChange();
                }
            } else {
                if (oldValue != null) {
                    choice = newValue;
                    textField.setBorder(invalidChoiceBorder);
                    notifyChange();
                }
            }
        } finally {
            writeLock = false;
        }
    }
    
    private void notifyChange() {
        for (ChoiceChangeListener listener : choiceChangeListeners)
            listener.onChoiceChanged();
    }
    
    private void initChoice() {
        choice = null;
        textField.setBorder(invalidChoiceBorder);
        notifyChange();
    }
    
    public void setBackgroundColor(Color color) {
        textField.setBackground(color);
    }

}
