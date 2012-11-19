package com.thorplatform.swing;

/**
 *
 * @author Fernando
 */
public interface SwingControllerFactory {
    
    <T extends SwingController> T createController(Class<T> controllerClass);
    
}
