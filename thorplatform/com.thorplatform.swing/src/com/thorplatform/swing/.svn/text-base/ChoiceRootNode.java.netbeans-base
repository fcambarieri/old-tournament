package com.thorplatform.swing;

import com.thorplatform.utils.observer.ClientObservable;
import com.thorplatform.utils.observer.Observer;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;

/**
 *
 * @author Fernando
 */
public abstract class ChoiceRootNode extends AbstractNode {
    
    private ValidatorFilter validatorFilter;
    private String titleForSearch = "Search for...";
    private ClientObservable clientObservable = Lookup.getDefault().lookup(ClientObservable.class);
    
    public ChoiceRootNode(ChoiceRootNodeChildren children) {
        super(children);
    }
    
    public void replaceKeys(String pattern) {
        ChoiceRootNodeChildren children = (ChoiceRootNodeChildren) getChildren();
        children.setKeys(Collections.EMPTY_LIST);
        
        if (validatorFilter != null && validatorFilter.validate(pattern)) {
            List keys = loadKeys(pattern);
            children.setKeys(keys);
        }
        
    }
    
    protected abstract List loadKeys(String pattern);

    public ValidatorFilter getValidatorFilter() {
        return validatorFilter;
    }

    public void setValidatorFilter(ValidatorFilter validatorFilter) {
        this.validatorFilter = validatorFilter;
    }

    public String getTitleForSearch() {
        return titleForSearch;
    }

    public void setTitleForSearch(String titleForSearch) {
        this.titleForSearch = titleForSearch;
    }
    
    public void addObserver(Class classToObserver, Observer o) {
        if (clientObservable != null)
            clientObservable.addObserver(classToObserver, o);
    }
}