package edu.courseproject.client.entity;

import java.util.Objects;

public class Customer extends User {
    //TODO validation
    private long idCustomer;
    private String name;
    private String representative;
    private String email;
    private String regionCustomer;

    public Customer() {
    }

    public Customer(long idCustomer, String name, String representative, String email, String regionCustomer) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.representative = representative;
        this.email = email;
        this.regionCustomer = regionCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegionCustomer() {
        return regionCustomer;
    }

    public void setRegionCustomer(String regionCustomer) {
        this.regionCustomer = regionCustomer;
    }

    //fixme equals hasCOde
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return idCustomer == customer.idCustomer &&
                Objects.equals(name, customer.name) &&
                Objects.equals(representative, customer.representative) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(regionCustomer, customer.regionCustomer);
    }

    @Override
    public int hashCode() {
        final int factor = 31;
        int result = 1;
        result += factor * idCustomer;
        result += factor + ((name == null) ? 0 : name.hashCode());
        result += factor + ((representative == null) ? 0 : representative.hashCode());
        result += factor + ((email == null) ? 0 : email.hashCode());
        result += ((regionCustomer == null) ? 0 : regionCustomer.hashCode());
        return result;
    }
}
