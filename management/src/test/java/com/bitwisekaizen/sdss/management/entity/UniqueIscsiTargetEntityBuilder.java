package com.bitwisekaizen.sdss.management.entity;

import com.bitwisekaizen.sdss.management.dto.IscsiTarget;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class UniqueIscsiTargetEntityBuilder implements Builder<UniqueIscsiTargetEntity> {

    private List<InitiatorIqnEntity> initiatorIqnEntities = new ArrayList<>();
    private String uuid = UUID.randomUUID().toString();
    private int capacityInMb = ThreadLocalRandom.current().nextInt(0, 100000);
    private String targetName = "targetName-" + UUID.randomUUID().toString();
    private String storageHost = "storageHost-" + UUID.randomUUID().toString();;

    private UniqueIscsiTargetEntityBuilder() {
    }

    public static UniqueIscsiTargetEntityBuilder anUnsavedUniqueIscsiTargetEntity() {
        UniqueIscsiTargetEntityBuilder uniqueIscsiTargetEntityBuilder = new UniqueIscsiTargetEntityBuilder();
        uniqueIscsiTargetEntityBuilder.uuid = null;

        return uniqueIscsiTargetEntityBuilder;
    }

    public static UniqueIscsiTargetEntityBuilder aUniqueIscsiTargetEntity() {
        return new UniqueIscsiTargetEntityBuilder();
    }

    public static UniqueIscsiTargetEntityBuilder aUniqueIscsiTargetEntityFrom(IscsiTarget iscsiTarget) {
        UniqueIscsiTargetEntityBuilder uniqueIscsiTargetEntityBuilder = aUniqueIscsiTargetEntity();
        for (String iqn : iscsiTarget.getHostIscsiQualifiedNames()) {
            uniqueIscsiTargetEntityBuilder.initiatorIqnEntities.add(new InitiatorIqnEntity(iqn));
        }

        uniqueIscsiTargetEntityBuilder.capacityInMb = iscsiTarget.getCapacityInMb();
        uniqueIscsiTargetEntityBuilder.targetName = iscsiTarget.getTargetName();
        return uniqueIscsiTargetEntityBuilder;
    }

    @Override public UniqueIscsiTargetEntity build() {
        UniqueIscsiTargetEntity uniqueIscsiTargetEntity =
                new UniqueIscsiTargetEntity(initiatorIqnEntities, capacityInMb, targetName, storageHost);

        uniqueIscsiTargetEntity.setUuid(uuid);
        return uniqueIscsiTargetEntity;
    }

    public UniqueIscsiTargetEntityBuilder withStorageHost(String storageHost) {
        this.storageHost = storageHost;
        return this;
    }

    public UniqueIscsiTargetEntityBuilder withInitiatorIqnEntity(InitiatorIqnEntityBuilder initiatorIqnEntityBuilder) {
        this.initiatorIqnEntities.add(initiatorIqnEntityBuilder.build());
        return this;
    }
}