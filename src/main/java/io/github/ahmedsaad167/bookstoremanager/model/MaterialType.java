package io.github.ahmedsaad167.bookstoremanager.model;

public enum MaterialType {

    PAPER("ورق"),
    CARDBOARD("كرتون"),
    CLOTH("قماش"),
    FOAM("فوم"),
    OTHER("أخرى");

    private final String displayName;

    MaterialType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
