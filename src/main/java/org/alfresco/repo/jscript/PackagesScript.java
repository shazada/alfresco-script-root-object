package org.alfresco.repo.jscript;

import org.alfresco.repo.processor.BaseProcessorExtension;
import java.lang.reflect.Method;

/**
 * Exposing some methods from SysAdminParams bean as JavaScript root object
 */
public class PackagesScript extends BaseProcessorExtension {

    public Object getContext() {
        return org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
    }

    /**
     * @param className
     * @return
     */
    public Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param className
     * @return
     */
    public Method[] getMethods(String className) {
        try {
            return Class.forName(className).getMethods();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
