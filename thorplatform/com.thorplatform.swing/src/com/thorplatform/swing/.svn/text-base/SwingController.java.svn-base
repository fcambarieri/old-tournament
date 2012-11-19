package com.thorplatform.swing;

import com.thorplatform.swing.validator.SwingValidatorHandler;
import com.thorplatform.utils.DateTimeUtils;
import com.thorplatform.utils.GuiUtils;
import com.thorplatform.utils.NumericUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Fernando
 */
public abstract class SwingController implements PropertyChangeListener {
    
    private String title = "Titulo";
    
    private GuiUtils guiUtils;
    private NumericUtils numericUtils;
    private DateTimeUtils dateTimeUtils;
    private SwingBinder swingBinder;
    private SwingValidatorHandler swingValidator;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setGuiUtils(GuiUtils utils) {
        this.guiUtils = utils;
    }
    
    public GuiUtils getGuiUtils() {
        return guiUtils;
    }
    
    public void initController() {
        if (getFocusedComponent() != null)
            getFocusedComponent().requestFocusInWindow();
    }
    
    public NumericUtils getNumericUtils() {
        return numericUtils;
    }
    
    public void setNumericUtils(NumericUtils numericUtils) {
        this.numericUtils = numericUtils;
    }
    
    public DateTimeUtils getDateTimeUtils() {
        return dateTimeUtils;
    }
    
    public void setDateTimeUtils(DateTimeUtils dateTimeUtils) {
        this.dateTimeUtils = dateTimeUtils;
    }
    
    public SwingValidatorHandler getSwingValidator() {
        return swingValidator;
    }

    public void setSwingValidator(SwingValidatorHandler swingValidator) {
        this.swingValidator = swingValidator;
    }
    
    public void setSwingBinder(SwingBinder binder) {
        this.swingBinder = binder;
        
        if (this.swingBinder != null)
            this.swingBinder.removePropertyChangeListener(this);
        
        this.swingBinder = binder;
        
        this.swingBinder.addPropertyChangeListener(this);
        
    }
    
    public SwingBinder getSwingBinder() {
        return swingBinder;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        onPresentationModelChange(evt);
    }
    
    protected abstract JPanel getForm();
    
    /**
     * Se llama automóticamente cada vez que una bound property
     * cambia de valor.
     */
    protected abstract void onPresentationModelChange(PropertyChangeEvent evt);
    
    /*
     *  Hay que llamar a este método dentro del onPresentationModelChange
     *  de la clase que herede, así, des está forma se llama cunado quiere.
     */    
    protected boolean canAcceptDialog() {
        return getSwingValidator().validate();
    }
    
    protected JComponent getFocusedComponent() {
        return null;
    }
    
}

