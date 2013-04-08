package com.thorplatform.swing.internal;

import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.ChoiceField.ChoiceChangeListener;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.DelegatingListModel;
import com.thorplatform.swing.DelegatingTreeTableModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.ListTreeTableModel;
import com.thorplatform.swing.MapProperty;
import com.thorplatform.swing.MultiChoiceField;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingBinder;
import com.thorplatform.utils.NumericUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.openide.util.Lookup;

/**
 *
 * @author Fernando
 */
public class DefaultSwingBinder implements SwingBinder {

    private static final int NO_SELECTION_INDEX = -1;
    private PropertyChangeSupport propertyChangeSupport;

    public DefaultSwingBinder() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    private void notifyBindingListener(PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange(evt);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void bindCheckBoxToBoolean(final JCheckBox checkBox, final Property<Boolean> booleanProperty) {
        checkBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (booleanProperty.get() == null || (booleanProperty.get().booleanValue() != checkBox.isSelected())) {
                    booleanProperty.set(Boolean.valueOf(checkBox.isSelected()));
                }
            }
        });

        booleanProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (booleanProperty.get() != null && (checkBox.isSelected() != booleanProperty.get().booleanValue())) {
                    checkBox.setSelected(booleanProperty.get().booleanValue());
                }
                notifyBindingListener(evt);
            }
        });
    }

    public void bindTextFieldToBigDecimal(final JTextField textField, final Property<BigDecimal> bigDecimalProperty) {
        final NumericUtils nu = Lookup.getDefault().lookup(NumericUtils.class);
        textField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }

            private void textChanged() {
                BigDecimal newValue;
                boolean mustSet;
                try {
                    newValue = nu.parseBigDecimal(textField.getText());
                    mustSet = !newValue.equals(bigDecimalProperty.get());
                } catch (Throwable t) {
                    newValue = null;
                    mustSet = bigDecimalProperty.get() != null;
                }
                if (mustSet) {
                    bigDecimalProperty.set(newValue);
                } else {
                    //validatorHandler.notifyValidation(textField,bigDecimalProperty);
                }

            }
        });

        bigDecimalProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (bigDecimalProperty.get() != null) {
                    BigDecimal textFieldValue;
                    BigDecimal propertyValue = bigDecimalProperty.get();
                    try {
                        textFieldValue = nu.parseBigDecimal(textField.getText());
                    } catch (Throwable t) {
                        textFieldValue = null;
                    }

                    if (!propertyValue.equals(textFieldValue)) {
                        textField.setText(nu.bigDecimalToString(propertyValue));
                    }
                }
                //validatorHandler.notifyValidation(textField,bigDecimalProperty);
                notifyBindingListener(evt);
            }
        });
    }

    public void bindTextFieldToInteger(final JTextField textField, final Property<Integer> integerProperty) {
        textField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }

            private void textChanged() {
                Integer newValue;
                boolean mustSet;
                try {
                    newValue = new Integer(textField.getText());
                    mustSet = !newValue.equals(integerProperty.get());
                } catch (Throwable t) {
                    newValue = null;
                    mustSet = integerProperty.get() != null;
                }
                if (mustSet) {
                    integerProperty.set(newValue);
                //else
                //    validatorHandler.notifyValidation(textField,integerProperty);
                }
            }
        });

        integerProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (integerProperty.get() != null) {
                    Integer textFieldValue;
                    Integer propertyValue = integerProperty.get();
                    try {
                        textFieldValue = new Integer(textField.getText());
                    } catch (Throwable t) {
                        textFieldValue = null;
                    }

                    if (!propertyValue.equals(textFieldValue)) {
                        textField.setText(propertyValue.toString());
                    }
                }

                //validatorHandler.notifyValidation(textField,integerProperty);
                notifyBindingListener(evt);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> void bindComboBoxToObject(final JComboBox comboBox, final Property<T> objectProperty, final ListProperty<T> listProperty) {
        listProperty.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                DelegatingComboBoxModel<T> model = (DelegatingComboBoxModel<T>) comboBox.getModel();
                model.fireContentsChanged(model, 0, model.getSize() - 1);
            }

            public void onAddItem(int index) {
                DelegatingComboBoxModel<T> model = (DelegatingComboBoxModel<T>) comboBox.getModel();
                model.fireIntervalAdded(model, index, index);
            }

            public void onRemoveItem(int index) {
                DelegatingComboBoxModel<T> model = (DelegatingComboBoxModel<T>) comboBox.getModel();
                model.fireIntervalRemoved(model, index, index);
            }
        });

        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                T viewValue = (T) comboBox.getSelectedItem();
                T modelValue = objectProperty.get();

                if ((viewValue != null && !viewValue.equals(modelValue)) || (viewValue == null && modelValue != null)) {
                    objectProperty.set(viewValue);
                }
            }
        });

        objectProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                T viewValue = (T) comboBox.getSelectedItem();
                T modelValue = objectProperty.get();

                if (modelValue == null || (modelValue != null && !modelValue.equals(viewValue))) {
                    comboBox.setSelectedItem(modelValue);                //validatorHandler.notifyValidation(comboBox,objectProperty);
                }
                notifyBindingListener(evt);
            }
        });

    }

    public void bindDatePickerToDate(final JXDatePicker datePicker, final Property<Date> dateProperty) {
        datePicker.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Date viewValue = datePicker.getDate();
                Date modelValue = dateProperty.get();

                if (viewValue != null && !viewValue.equals(modelValue) || (viewValue == null && modelValue != null)) {
                    dateProperty.set(viewValue);
                }
            }
        });

        dateProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Date viewValue = datePicker.getDate();
                Date modelValue = dateProperty.get();

                if (modelValue == null && viewValue != null || (modelValue != null && !modelValue.equals(viewValue))) {
                    datePicker.setDate(modelValue);                //validatorHandler.notifyValidation(datePicker,dateProperty);
                }
                notifyBindingListener(evt);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> void bindSingleSelectionTable(final JXTable table, final ListProperty<T> listProperty, final Property<Integer> indexProperty) {
        indexProperty.set(NO_SELECTION_INDEX);

        final ListTableModel<T> listTableModel = (ListTableModel<T>) table.getModel();
        listTableModel.setList(listProperty.getList());

        //validatorHandler.notifyValidation(table,listProperty);

        listProperty.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                listTableModel.fireTableDataChanged();
                changeColor();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onAddItem(int index) {
                listTableModel.fireTableRowsInserted(index, index);
                changeColor();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onRemoveItem(int index) {
                listTableModel.fireTableRowsDeleted(index, index);
                changeColor();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void changeColor() {
                //validatorHandler.notifyValidation(table,listProperty);
            }
        });


        final ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int tableIndex = table.getSelectedRow();
                    if (tableIndex >= 0) {
                        int tableModelIndex = table.convertRowIndexToModel(tableIndex);
                        if (tableModelIndex != indexProperty.get().intValue()) {
                            indexProperty.set(new Integer(tableModelIndex));
                        }
                    } else {
                        indexProperty.set(NO_SELECTION_INDEX);
                    }
                }
            }
        });



        indexProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                int modelValue = indexProperty.get().intValue();
                if (modelValue != NO_SELECTION_INDEX) {
                    int viewValue = table.convertRowIndexToView(modelValue);
                    int selectedViewIndex = table.getSelectedRow();
                    if (viewValue != selectedViewIndex) {
                        selectionModel.setSelectionInterval(viewValue, viewValue);
                    }
                } else {
                    if (selectionModel.getMinSelectionIndex() != -1) {
                        selectionModel.clearSelection();
                    }
                }

                notifyBindingListener(evt);
            }
        });

    }

    public <T> void bindChoiceToObject(final ChoiceField<T> choiceField, final Property<T> objectProperty) {
        choiceField.addChoiceChangeListener(new ChoiceChangeListener() {

            public void onChoiceChanged() {
                T viewValue = choiceField.getChoice();
                T modelValue = objectProperty.get();

                if ((viewValue != null && !viewValue.equals(modelValue)) || (viewValue == null && modelValue != null)) {
                    objectProperty.set(viewValue);
                }
            }
        });

        objectProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                T viewValue = choiceField.getChoice();
                T modelValue = objectProperty.get();

                if (modelValue == null && viewValue != null || (modelValue != null && !modelValue.equals(viewValue))) {
                    choiceField.setChoice(modelValue);                //validatorHandler.notifyValidation(choiceField,objectProperty);
                }
                notifyBindingListener(evt);
            }
        });
    }

    public void bindRadioButtonToBoolean(final JRadioButton radioButton, final Property<Boolean> booleanProperty) {
        radioButton.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (booleanProperty.get() == null || (booleanProperty.get().booleanValue() != radioButton.isSelected())) {
                    booleanProperty.set(Boolean.valueOf(radioButton.isSelected()));
                }
            }
        });

        booleanProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (booleanProperty.get() != null && (radioButton.isSelected() != booleanProperty.get().booleanValue())) {
                    radioButton.setSelected(booleanProperty.get().booleanValue());
                }
                notifyBindingListener(evt);
            }
        });
    }

    public <T> void bindTextComponentToString(final JTextComponent textComponent, final Property<String> stringProperty) {
        textComponent.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                onTextChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextChanged();
            }

            public void changedUpdate(DocumentEvent e) {
                onTextChanged();
            }

            private void onTextChanged() {
                if (stringProperty.get() == null || !stringProperty.get().equals(textComponent.getText())) {
                    stringProperty.set(textComponent.getText());
                }
            }
        });

        stringProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (stringProperty.get() != null && !stringProperty.get().equals(textComponent.getText())) {
                    textComponent.setText(stringProperty.get());
                }
                //validatorHandler.notifyValidation(textComponent,stringProperty);
                notifyBindingListener(evt);
            }
        });
    }

    public void bindSpinnerToInteger(final JSpinner spinner, final Property<Integer> integerProperty) {
        final JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (integerProperty.get() == null || (integerProperty.get().intValue() != (Integer) spinner.getValue())) {
                    integerProperty.set((Integer) spinner.getValue());
                }
            }
        });

        integerProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ((integerProperty.get() != null) && ((Integer) spinner.getValue() != integerProperty.get().intValue())) {
                    spinner.setValue(integerProperty.get().intValue());
                }
                //validatorHandler.notifyValidation(spinner,integerProperty);
                notifyBindingListener(evt);
            }
        });
    }

    public void bindToggleButtonToBoolean(final JToggleButton toggleButton, final Property<Boolean> booleanProperty) {
        toggleButton.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (booleanProperty.get() == null || (booleanProperty.get().booleanValue() != toggleButton.isSelected())) {
                    booleanProperty.set(Boolean.valueOf(toggleButton.isSelected()));
                }
            }
        });

        booleanProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (booleanProperty.get() != null && (toggleButton.isSelected() != booleanProperty.get().booleanValue())) {
                    toggleButton.setSelected(booleanProperty.get().booleanValue());
                }
                notifyBindingListener(evt);
            }
        });
    }

    public void bindTextFieldToLong(final JTextField textField, final Property<Long> longProperty) {
        textField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                onTextChange();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextChange();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextChange();
            }

            private void onTextChange() {
                Long newValue;
                boolean mustSet;
                try {
                    newValue = new Long(textField.getText());
                    mustSet = !newValue.equals(longProperty.get());
                } catch (Throwable t) {
                    newValue = null;
                    mustSet = longProperty.get() != null;
                }
                if (mustSet) {
                    longProperty.set(newValue);
                //else
                //    validatorHandler.notifyValidation(textField,longProperty);
                }
            }
        });

        longProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (longProperty.get() != null) {
                    Long textFieldValue;
                    Long propertyValue = longProperty.get();
                    try {
                        textFieldValue = new Long(textField.getText());
                    } catch (Throwable t) {
                        textFieldValue = null;
                    }

                    if (!propertyValue.equals(textFieldValue)) {
                        textField.setText(propertyValue.toString());
                    }
                }
                //validatorHandler.notifyValidation(textField,longProperty);
                notifyBindingListener(evt);
            }
        });

    }

    public <T> void bindMultiChoiceToObjects(final MultiChoiceField<T> choiceField, final ListProperty<T> listProperty) {

        choiceField.addChoiceChangeListener(new MultiChoiceField.ChoiceChangeListener() {

            public void onChoiceChanged() {
                List<T> viewValues = choiceField.getChoice();
                List<T> modelValue = listProperty.getList();
                if ((viewValues != null && !viewValues.equals(modelValue)) || modelValue != null) {
                    listProperty.assignData(viewValues);
                    //choiceField.setChoice(modelValue);
                }
            }
        });

        listProperty.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                changeList();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onAddItem(int index) {
                changeList();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onRemoveItem(int index) {
                changeList();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            private void changeList() {
                List<T> viewValues = choiceField.getChoice();
                List<T> modelValue = listProperty.getList();
                if ((modelValue != null && !modelValue.equals(viewValues)) || viewValues != null) {
                    choiceField.setChoice(modelValue);
                    //listProperty.assignData(modelValue);
                }
            }
        });


    }

    public <T> void bindSingleSelectionList(final JXList list, final ListProperty<T> listProperty, final Property<T> objectProperty) {
        listProperty.addListener(new ListProperty.Listener() {

            @SuppressWarnings("unchecked")
            public void onDataChange() {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireContentsChanged(model, 0, model.getSize() - 1);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            @SuppressWarnings("unchecked")
            public void onAddItem(int index) {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireIntervalAdded(model, index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            @SuppressWarnings("unchecked")
            public void onRemoveItem(int index) {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireIntervalRemoved(model, index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {

            @SuppressWarnings("unchecked")
            public void valueChanged(ListSelectionEvent arg0) {
                T viewValue = (T) list.getSelectedValue();
                T modelValue = objectProperty.get();

                if (viewValue != null && !viewValue.equals(modelValue) || (viewValue == null && modelValue != null)) {
                    objectProperty.set(viewValue);
                }
            }
        });

        objectProperty.addPropertyChangeListener(new PropertyChangeListener() {

            @SuppressWarnings("unchecked")
            public void propertyChange(PropertyChangeEvent evt) {
                T viewValue = (T) list.getSelectedValue();
                T modelValue = objectProperty.get();

                if (modelValue == null || (modelValue != null && !modelValue.equals(viewValue))) {
                    list.setSelectedValue(modelValue, true);
                }

                notifyBindingListener(evt);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> void bindMultiSelectionTable(final JXTable table, final ListProperty<T> listProperty, final ListProperty<Integer> listItemSelected) {
        listItemSelected.assignData(Collections.EMPTY_LIST);

        final ListTableModel<T> listTableModel = (ListTableModel<T>) table.getModel();
        listTableModel.setList(listProperty.getList());

        listProperty.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                listTableModel.fireTableDataChanged();
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onAddItem(int index) {
                listTableModel.fireTableRowsInserted(index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onRemoveItem(int index) {
                listTableModel.fireTableRowsDeleted(index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }
        });


        final ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int tableIndex = table.getSelectedRow();
                    int rowCount = table.getSelectedRowCount();
                    int tableIndexSlected[] = table.getSelectedRows();

                    if (rowCount == 1) {
                        if (tableIndex >= 0) {
                            int tableModelIndex = table.convertRowIndexToModel(tableIndex);
                            int indexProperty = (listItemSelected.getList().size() == 1 ? listItemSelected.getList().get(0) : -1);
                            if (tableModelIndex != indexProperty) {
                                Integer idx = new Integer(tableModelIndex);
                                List<Integer> list = new ArrayList<Integer>();
                                list.add(idx);
                                listItemSelected.assignData(list);
                            }

                        } else {
                            listItemSelected.assignData(Collections.EMPTY_LIST);
                        }
                    } else if (rowCount > 1) {
                        //Convierto los index seleccionados al modelo
                        List<Integer> listTableModelIndex = new ArrayList<Integer>();
                        for (Integer idx : tableIndexSlected) {
                            idx = table.convertRowIndexToModel(idx);
                            listTableModelIndex.add(new Integer(idx));
                        }

                        if (listTableModelIndex.size() != listItemSelected.getList().size()) {
                            listItemSelected.assignData(listTableModelIndex);
                        }
                    } else {
                        listItemSelected.assignData(Collections.EMPTY_LIST);
                    }
                }
            }
        });

        listItemSelected.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                onChange();
            }

            public void onAddItem(int index) {
                onChange();
            }

            public void onRemoveItem(int index) {
                onChange();
            }

            public void onChange() {
                List<Integer> modelRowSelected = listItemSelected.getList();


                if (modelRowSelected.size() > 0) {
                    for (Integer i : modelRowSelected) {
                        int viewValue = table.convertRowIndexToView(i);
                        selectionModel.addSelectionInterval(viewValue, viewValue);
                    }
                } else {
                    if (selectionModel.getMinSelectionIndex() != -1) {
                        selectionModel.clearSelection();
                    }
                }


                notifyBindingListener(new PropertyChangeEvent(listItemSelected, listItemSelected.getName(), null, null));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> void bindSingleSelectionTreeTable(final JXTreeTable treeTable, final ListProperty<T> treeListProperty, final Property<Integer> indexProperty) {
        indexProperty.set(NO_SELECTION_INDEX);

        final ListTreeTableModel<T> listTreeTableModel = (ListTreeTableModel<T>) treeTable.getModel();
        listTreeTableModel.assignTreeList(treeListProperty.getList());

        treeListProperty.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                //listTreeTableModel.fireTableDataChanged();
                notifyBindingListener(new PropertyChangeEvent(treeListProperty, treeListProperty.getName(), null, null));
            }

            public void onAddItem(int index) {
                //listTreeTableModel.fireTableRowsInserted(index, index);
                notifyBindingListener(new PropertyChangeEvent(treeListProperty, treeListProperty.getName(), null, null));
            }

            public void onRemoveItem(int index) {
                //listTreeTableModel.fireTableRowsDeleted(index, index);
                notifyBindingListener(new PropertyChangeEvent(treeListProperty, treeListProperty.getName(), null, null));
            }

        });
        
        final ListSelectionModel selectionModel = treeTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int tableIndex = treeTable.getSelectedRow();
                    if (tableIndex >= 0) {
                        int tableModelIndex = treeTable.convertRowIndexToModel(tableIndex);
                        if (tableModelIndex != indexProperty.get().intValue()) {
                            indexProperty.set(new Integer(tableModelIndex));
                        }
                    } else {
                        indexProperty.set(NO_SELECTION_INDEX);
                    }
                }
            }
        });



        indexProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                int modelValue = indexProperty.get().intValue();
                if (modelValue != NO_SELECTION_INDEX) {
                    int viewValue = treeTable.convertRowIndexToView(modelValue);
                    int selectedViewIndex = treeTable.getSelectedRow();
                    if (viewValue != selectedViewIndex) {
                        selectionModel.setSelectionInterval(viewValue, viewValue);
                    }
                } else {
                    if (selectionModel.getMinSelectionIndex() != -1) {
                        selectionModel.clearSelection();
                    }
                }

                notifyBindingListener(evt);
            }
        });


    }

    @SuppressWarnings("unchecked")
    public <K, V> void bindTreeTableMap(final JXTreeTable xtable, final MapProperty<K, V> mapProperty, final Property<K> keyProperty, final Property<V> childProperty, final Class<K> keyClass, final Class<V> childClass) {
        final DelegatingTreeTableModel listTreeModel = (DelegatingTreeTableModel) xtable.getTreeTableModel();
        listTreeModel.setTreeList(mapProperty.getMap());

        mapProperty.addMapListener(new MapProperty.MapListener() {

            public void onDataChange() {
                listTreeModel.fireBuildTreeTableModel();
                notifyBindingListener(new PropertyChangeEvent(mapProperty, mapProperty.getName(), null, null));
            }

            public void onAddKey(Object key) {
                listTreeModel.fireInsertNode(key);
                notifyBindingListener(new PropertyChangeEvent(mapProperty, mapProperty.getName(), null, null));
            }

            public void onDeleteKey(Object key) {
                listTreeModel.fireDeleteNode(key);
                notifyBindingListener(new PropertyChangeEvent(mapProperty, mapProperty.getName(), null, null));
            }

            public void onAddKeyItem(Object key, Object value) {
                listTreeModel.fireInsertChildToNode(key, value);
                notifyBindingListener(new PropertyChangeEvent(mapProperty, mapProperty.getName(), null, null));
            }

            public void onDeleteKeyItem(Object key, Object value) {
                listTreeModel.fireDeleteNode(value);
                notifyBindingListener(new PropertyChangeEvent(mapProperty, mapProperty.getName(), null, null));
            }
        });

        final TreeSelectionModel selectionModel = xtable.getTreeSelectionModel();
        selectionModel.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (e.getPath().getLastPathComponent() != null) {
                    DefaultMutableTreeNode nodeSelected = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                    Object userObject = nodeSelected.getUserObject();
                    K key = keyProperty.get();
                    V child = childProperty.get();

                    if (userObject != null) {
                        if (keyClass.isAssignableFrom(userObject.getClass()) && !userObject.equals(key)) {
                            keyProperty.set((K) userObject);
                            childProperty.set(null);
                        } else if (childClass.isAssignableFrom(userObject.getClass()) && !userObject.equals(child)) {
                            childProperty.set((V) userObject);
                            keyProperty.set(null);
                        }
//                        if (userObject.getClass().isAssignableFrom(keyClass) && !userObject.equals(key)) {
//                            keyProperty.set((K) userObject);
//                            childProperty.set(null);
//                        } else if (userObject.getClass().isAssignableFrom(childClass) && !userObject.equals(child)) {
//                            childProperty.set((V) userObject);
//                            keyProperty.set(null);
//                        }
                    }
                }
            }
        });

        keyProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                notifyBindingListener(evt);
            }
        });

        childProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                notifyBindingListener(evt);
            }
        });
    }


    @SuppressWarnings("unchecked")
    public <T> void bindIndexSelectionList(final JXList list, final ListProperty<T> listProperty, final Property<Integer> indexProperty) {
        indexProperty.set(NO_SELECTION_INDEX);
        final ListSelectionModel selectionModel = list.getSelectionModel();

        listProperty.addListener(new ListProperty.Listener() {

            public void onDataChange() {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireContentsChanged(model, 0, model.getSize() - 1);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onAddItem(int index) {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireIntervalAdded(model, index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onRemoveItem(int index) {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireIntervalRemoved(model, index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onUpdateItem(int index) {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireContentsChanged(model, index, index);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }

            public void onAddItem(int indexFrom, int indexTo) {
                DelegatingListModel<T> model = (DelegatingListModel<T>) list.getModel();
                model.fireIntervalAdded(model, indexFrom, indexTo);
                notifyBindingListener(new PropertyChangeEvent(listProperty, listProperty.getName(), null, null));
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int listIndex = list.getSelectedIndex();
                    int modelIndex = indexProperty.get() != null ? indexProperty.get().intValue() : NO_SELECTION_INDEX;
                    if (listIndex >= 0) {
                        int listModelIndex = list.convertIndexToModel(listIndex);
                        if (listModelIndex != modelIndex) {
                            indexProperty.set(new Integer(listModelIndex));
                        }
                    } else {
                        indexProperty.set(NO_SELECTION_INDEX);
                    }
                }
            }
        });

        indexProperty.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                int modelValue = indexProperty.get() != null ? indexProperty.get().intValue() : NO_SELECTION_INDEX;
                if (modelValue != NO_SELECTION_INDEX) {
                    int viewValue = list.convertIndexToView(modelValue);
                    int selectedViewIndex = list.getSelectedIndex();
                    if (viewValue != selectedViewIndex) {
                        selectionModel.setSelectionInterval(viewValue, viewValue);
                    }
                } else {
                    if (selectionModel.getMinSelectionIndex() != -1) {
                        selectionModel.clearSelection();
                    }
                }

                notifyBindingListener(evt);
            }
        });
    }

}
