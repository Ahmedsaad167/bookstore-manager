package io.github.ahmedsaad167.bookstoremanager.model;

public class Customer {
    private int id;

    private String name;
    private String username;
    private String phone;
    private String email;
    private String address;
    private String notes;

    public Customer(int id, String name, String username, String phone) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
    }

    public Customer(String name, String username, String phone) {
        this.name = name;
        this.username = username;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
