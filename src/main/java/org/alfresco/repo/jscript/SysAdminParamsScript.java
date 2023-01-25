package org.alfresco.repo.jscript;

import org.alfresco.repo.admin.SysAdminParams;
import org.alfresco.repo.processor.BaseProcessorExtension;

/**
 * Exposing some methods from SysAdminParams bean as JavaScript root object
 */
public class SysAdminParamsScript extends BaseProcessorExtension {

    private SysAdminParams sysAdminParams;

    public void setSysAdminParams(SysAdminParams sysAdminParams) {
        this.sysAdminParams = sysAdminParams;
    }

    public String getAlfrescoProtocol() {
        return sysAdminParams.getAlfrescoProtocol();
    }

    public String getAlfrescoHost() {
        return sysAdminParams.getAlfrescoHost();
    }

    public Integer getAlfrescoPort() {
        return sysAdminParams.getAlfrescoPort();
    }

    public String getAlfrescoContext() {
        return sysAdminParams.getAlfrescoContext();
    }

    public String getShareProtocol() {
        return sysAdminParams.getShareProtocol();
    }

    public String getShareHost() {
        return sysAdminParams.getShareHost();
    }

    public Integer getSharePort() {
        return sysAdminParams.getSharePort();
    }

    public String getShareContext() {
        return sysAdminParams.getShareContext();
    }

}
