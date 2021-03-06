package com.bitwisekaizen.sdss.agent.service;

import com.bitwisekaizen.sdss.agent.entity.IscsiTargetEntity;
import com.bitwisekaizen.sdss.agent.repository.IscsiTargetEntityRepository;
import com.bitwisekaizen.sdss.agentclient.AccessibleIscsiTarget;
import com.bitwisekaizen.sdss.agentclient.IscsiTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Service to manage ISCSI targets.
 */
@Service
public class AccessibleIscsiTargetService {

    private IscsiTargetEntityRepository accessibleIscsiTargetRepository;
    private LioBackedIscsiTargetService lioBackedStorageService;

    private List<String> storageNetworkAddresses = new ArrayList<>();

    @Autowired
    public AccessibleIscsiTargetService(@Value("#{'${app.storage.network.addresses}'.split(',')}")
                                            List<String> storageNetworkAddresses,
                                        IscsiTargetEntityRepository accessibleIscsiTargetRepository,
                                        LioBackedIscsiTargetService lioBackedStorageService) {
        this.storageNetworkAddresses = storageNetworkAddresses;
        this.accessibleIscsiTargetRepository = accessibleIscsiTargetRepository;
        this.lioBackedStorageService = lioBackedStorageService;
    }

    /**
     * Create the ISCSI target with the given spec.
     *
     * @param iscsiTarget ISCSI target to create
     * @return the created ISCSI target.
     * @throws DuplicateTargetNameException if the target name already exists.
     */
    public AccessibleIscsiTarget createAccessbileIscsiTarget(IscsiTarget iscsiTarget) {
        IscsiTargetEntity existingTarget = accessibleIscsiTargetRepository.findOne(
                iscsiTarget.getTargetName());
        if (existingTarget != null) {
            throw new DuplicateTargetNameException(iscsiTarget.getTargetName());
        }

        IscsiTargetEntity newTarget = convertToPersistenceEntity(iscsiTarget);

        lioBackedStorageService.createTargets(convertToDto(Arrays.asList(newTarget)));
        accessibleIscsiTargetRepository.save(newTarget);

        return convertToDto(newTarget);
    }

    /**
     * Delete the ISCSI target with the specified ID.
     *
     * @param targetName name of the target to delete
     * @throws IscsiTargetNotFoundException if target is not found
     */
    public void deleteAccessibleIscsiTarget(String targetName) {
        IscsiTargetEntity existingTarget = accessibleIscsiTargetRepository.findOne(targetName);
        if (existingTarget == null) {
            throw new IscsiTargetNotFoundException(targetName);
        }

        lioBackedStorageService.deleteTargets(convertToDto(Arrays.asList(existingTarget)));
        accessibleIscsiTargetRepository.delete(targetName);
    }

    /**
     * Get all the ISCSI targets in the system.
     *
     * @return all the ISCSI targets in the system.
     */
    public List<AccessibleIscsiTarget> getAllAccessibleIscsiTargets() {
        List<IscsiTargetEntity> entities = newArrayList(accessibleIscsiTargetRepository.findAll());
        return convertToDto(entities);
    }


    private List<AccessibleIscsiTarget> convertToDto(List<IscsiTargetEntity> targetsToUpdate) {
        List<AccessibleIscsiTarget> targetsConverted = new ArrayList<>();
        for (IscsiTargetEntity entity : targetsToUpdate) {
            targetsConverted.add(convertToDto(entity));
        }

        return targetsConverted;
    }

    private AccessibleIscsiTarget convertToDto(IscsiTargetEntity entity) {
        IscsiTarget iscsiTarget = new IscsiTarget(
                entity.getHostIscsiQualifiedNames(), entity.getCapacityInMb(), entity.getTargetName(),
                entity.getAffinityKey());
        return new AccessibleIscsiTarget(iscsiTarget, storageNetworkAddresses);
    }

    private IscsiTargetEntity convertToPersistenceEntity(IscsiTarget iscsiTarget) {
        return new IscsiTargetEntity(iscsiTarget.getHostIscsiQualifiedNames(),
                iscsiTarget.getCapacityInMb(), iscsiTarget.getTargetName(), iscsiTarget.getAffinityKey());
    }
}
