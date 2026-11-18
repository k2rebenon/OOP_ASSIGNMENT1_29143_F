package com.auca.interfaces;

public interface Identifiable {

    // Six constants
    String ID_PREFIX = "AUCA";
    int MIN_ID_LENGTH = 4;
    int MAX_ID_LENGTH = 30;
    String ID_SEPARATOR = "-";
    String DEFAULT_STATUS = "ACTIVE";
    int ENTITY_VERSION = 1;

    // Three methods
    String getId();

    String getDisplayName();

    boolean isActive();
}