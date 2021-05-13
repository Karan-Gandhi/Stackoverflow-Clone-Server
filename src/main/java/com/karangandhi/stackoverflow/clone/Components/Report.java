package com.karangandhi.stackoverflow.clone.Components;

import java.util.UUID;

public class Report {
    public UUID id;
    public UUID userID;
    public String type;

    public static final String TYPE_DUPLICATE = "duplicate";
    public static final String TYPE_NEEDS_TO_BE_FOCUSED = "needs to be focused";

    public Report(UUID userID, String type) {
        this.id = UUID.randomUUID();
        this.userID = userID;
        this.type = type;
    }

    public Report(UUID id, UUID userID, String type) {
        this.id = id;
        this.userID = userID;
        this.type = type;
    }
}
