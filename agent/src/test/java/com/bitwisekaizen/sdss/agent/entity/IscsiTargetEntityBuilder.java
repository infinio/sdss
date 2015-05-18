package com.bitwisekaizen.sdss.agent.entity;

import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class IscsiTargetEntityBuilder implements Builder<IscsiTargetEntity> {
    private static final Random random = new Random();

    private List<String> hostIscsiQualifiedNames = new ArrayList<>();
    private int capacityInMb = ThreadLocalRandom.current().nextInt(100, 2000);
    private String targetName = UUID.randomUUID().toString();

    public static IscsiTargetEntityBuilder anIscsiTargetEntity() {
        return new IscsiTargetEntityBuilder();
    }

    @Override
    public IscsiTargetEntity build() {
        return new IscsiTargetEntity(hostIscsiQualifiedNames, capacityInMb, targetName);
    }

    public IscsiTargetEntityBuilder withTargetName(String targetName) {
        this.targetName = targetName;
        return this;
    }
}
