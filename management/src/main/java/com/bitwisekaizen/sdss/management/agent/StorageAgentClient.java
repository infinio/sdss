package com.bitwisekaizen.sdss.management.agent;

import com.bitwisekaizen.sdss.management.dto.IscsiTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Client that can interact with the storage agent.
 */
public class StorageAgentClient {
    private String ipAddress;

    public StorageAgentClient(@Value("${app.storage.agent.ip}") String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Create an ISCSI target with the specified spec.
     *
     * @param iscsiTarget target to create.
     */
    public void createIscsiTarget(IscsiTarget iscsiTarget) {

    }

    /**
     * Delete the specified ISCSI target.
     *
     * @param uuid ID of the target to delete.
     */
    public void deleteIscsiTarget(String uuid) {

    }

    /**
     * Get the IP address that this client currently connects to.
     *
     * @return the IP address that this client currently connects to.
     */
    public String getStorageIpAddress() {
        return ipAddress;
    }
}