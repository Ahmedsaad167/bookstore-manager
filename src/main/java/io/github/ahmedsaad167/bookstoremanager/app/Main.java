package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;

public class Main {
    
    public static void main(String[] args) {
        MaterialType material = MaterialType.PAPER;
        System.out.println(material);
        System.out.println(material.getDisplayName());
    }
}