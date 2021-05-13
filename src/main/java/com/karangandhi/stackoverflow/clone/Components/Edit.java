package com.karangandhi.stackoverflow.clone.Components;

import java.util.Objects;
import java.util.UUID;

public class Edit {
    public UUID id;
    public UUID userID;
    public String body;
    public String status;

    public static final String STATUS_NORMAL = "normal";
    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_REJECTED = "rejected";

    public Edit(UUID userID, String body) {
        this.userID = userID;
        this.body = body;
        this.id = UUID.randomUUID();
        this.status = STATUS_ACCEPTED;
    }

    public Edit(UUID userID, String body, String status) {
        this.userID = userID;
        this.body = body;
        this.id = UUID.randomUUID();
        this.status = status;
    }

    public Edit(UUID userID, UUID id, String body, String status) {
        this.userID = userID;
        this.id = id;
        this.body = body;
        this.status = status;
    }


    @Override
    public String toString() {
        return "Edit{" +
                "id=" + id +
                ", userID=" + userID +
                ", body='" + body + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edit edit = (Edit) o;
        return Objects.equals(body, edit.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, body, status);
    }
}
