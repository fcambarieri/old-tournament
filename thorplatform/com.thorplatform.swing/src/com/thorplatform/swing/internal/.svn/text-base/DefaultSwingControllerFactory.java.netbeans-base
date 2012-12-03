package com.thorplatform.swing.internal;

import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.utils.DateTimeUtils;
import com.thorplatform.utils.GuiUtils;
import com.thorplatform.utils.NumericUtils;
import org.openide.util.Lookup;

/**
 *
 * @author Fernando
 */
public class DefaultSwingControllerFactory implements SwingControllerFactory {

    private NumericUtils numericUtils;
    private DateTimeUtils dateTimeUtils;
    private GuiUtils guiUtils;
    
    public DefaultSwingControllerFactory() {
        numericUtils = Lookup.getDefault().lookup(NumericUtils.class);
        dateTimeUtils = Lookup.getDefault().lookup(DateTimeUtils.class);
        guiUtils = Lookup.getDefault().lookup(GuiUtils.class);
    }
    
    public <T extends SwingController> T createController(Class<T> controllerClass) {
        try {
            T controller = controllerClass.newInstance();
            controller.setNumericUtils(numericUtils);
            controller.setDateTimeUtils(dateTimeUtils);
            controller.setGuiUtils(guiUtils);
            controller.setSwingValidator(new DefaultSwingValidatorHandler());
            controller.setSwingBinder(new DefaultSwingBinder());
            controller.initController();
            return controller;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
