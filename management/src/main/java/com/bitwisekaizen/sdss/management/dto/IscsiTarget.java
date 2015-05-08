package com.bitwisekaizen.sdss.management.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Spec that describes the ISCSI target
 */
public class IscsiTarget {

    private List<String> hostIscsiQualifiedNames;
    private int capacityInMb;
    private String targetName;

    // Json serialization
    private IscsiTarget(){}

    public IscsiTarget(List<String> hostIscsiQualifiedNames, int capacityInMb, String targetName) {
        this.hostIscsiQualifiedNames = hostIscsiQualifiedNames;
        this.capacityInMb = capacityInMb;
        this.targetName = targetName;
    }

    /**
     * Get the LUN target capacity in MB.
     *
     * @return the LUN target capacity in MB.
     */
    public int getCapacityInMb() {
        return capacityInMb;
    }

    /**
     * Get the list of ISCSI qualified names of the hosts, permissible to access this target.
     *
     * @return the list of ISCSI qualified names of the hosts, permissible to access this target.
     */
    public List<String> getHostIscsiQualifiedNames() {
        return hostIscsiQualifiedNames;
    }

    /**
     * Get the target name.
     *
     * @return target name.
     */
    public String getTargetName() {
        return targetName;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}