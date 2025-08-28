package org.alfresco.repo.jscript;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;

/**
 * Exposing some methods from SysAdminParams bean as JavaScript root object
 */
public class PackagesScript extends BaseProcessorExtension {

    /**
     * @return Springcontext
     */
    public Object getContext() {
        return org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
    }

    /**
     * @param className
     * @return getBean from the Spring context for the provided className
     */
    public Object getBean(String bean, String className) {
        try {
            return org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext().getBean(bean,
                    Class.forName(className));
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param className
     * @return
     */
    public Class<?> getPackage(String className) {
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
