package com.thorplatform.swing;

import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;

/**
 *
 * @author Fernando
 */
public interface SwingBinder {
    
    void addPropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);
    
    void bindCheckBoxToBoolean(JCheckBox checkBox, Property<Boolean> booleanProperty);
    void bindRadioButtonToBoolean(JRadioButton radioButton, Property<Boolean> booleanProperty);
    void bindToggleButtonToBoolean(JToggleButton toggleButton, Property<Boolean> booleanProperty);
    void bindSpinnerToInteger(final JSpinner spinner, final Property<Integer> integerProperty);
    <T> void bindComboBoxToObject(final JComboBox comboBox, final Property<T> objectProperty, final ListProperty<T> listProperty);
    void bindDatePickerToDate(final JXDatePicker datePicker, final Property<Date> dateProperty);
    
    void bindTextFieldToBigDecimal(JTextField textField, Property<BigDecimal> bigDecimalProperty);
    void bindTextFieldToInteger(JTextField textField, Property<Integer> integerProperty);
    void bindTextFieldToLong(JTextField textField, Property<Long> longProperty);
    <T> void bindTextComponentToString(JTextComponent textComponent, Property<String> stringProperty);
    
    <T> void bindSingleSelectionTable(final JXTable table, final ListProperty<T> listProperty, final Property<Integer> indexProperty);
    <T> void bindMultiSelectionTable(final JXTable table, final ListProperty<T> listProperty, final ListProperty<Integer> listItemSelected);
    
    <T> void bindChoiceToObject(final ChoiceField<T> choiceField, final Property<T> objectProperty);
    <T> void bindMultiChoiceToObjects(final MultiChoiceField<T> choiceField, final ListProperty<T> listProperty);
    
    <T> void bindSingleSelectionList(final JXList list, final ListProperty<T> listProperty, final Property<T> objectProperty);

    <T> void bindIndexSelectionList(final JXList list, final ListProperty<T> listProperty, final Property<Integer> indexProperty);
    
    public <K, V> void bindTreeTableMap(final JXTreeTable xtable, final MapProperty<K, V> mapProperty, final Property<K> keyProperty, final Property<V> childProperty, final Class<K> keyClass, final Class<V> childClass);
}
