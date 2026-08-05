package io.github.ahmedsaad167.bookstoremanager.model;

public enum AgeGroup {
    AGE_0_2("أقل من 3 سنوات"),
    AGE_3_5("من 3 الى 5 سنوات"),
    AGE_6_8("من 6 الى 8 سنوات"),
    AGE_9_12("من 9 الى 12 سنة"),
    AGE_13_PLUS("13 سنة فأكثر"),
    ALL_AGES("كل الأعمار");
    
    private final String displayName;

    AgeGroup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
