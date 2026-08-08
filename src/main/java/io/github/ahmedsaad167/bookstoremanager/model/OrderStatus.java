package io.github.ahmedsaad167.bookstoremanager.model;

public enum OrderStatus {
    PENDING("قيد الانتظار"),
    COMPLETED("مكتمل"),
    CANCELLED("ملغي");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
