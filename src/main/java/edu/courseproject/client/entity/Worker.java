package edu.courseproject.client.entity;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class Worker extends User {
    private long idWorker;
    private String surname;
    private String name;
    private double seniority;
    private String phone;
    private String regionWorker;

    public Worker() {
    }

    public Worker(long idWorker, String surname, String name, double seniority, String phone, String regionWorker) {
        this.idWorker = idWorker;
        this.surname = surname;
        this.name = name;
        this.seniority = seniority;
        this.phone = phone;
        this.regionWorker = regionWorker;
    }

    public Worker(String surname, String name, double seniority, String phone) {
        this.surname = surname;
        this.name = name;
        this.seniority = seniority;
        this.phone = phone;
    }

    public long getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(long idWorker) {
        this.idWorker = idWorker;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegionWorker() {
        return regionWorker;
    }

    public void setRegionWorker(String regionWorker) {
        this.regionWorker = regionWorker;
    }

    //TODO refactor equals hasCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        Worker worker = (Worker) o;
        return idWorker == worker.idWorker &&
                Double.compare(worker.seniority, seniority) == 0 &&
                surname.equals(worker.surname) &&
                name.equals(worker.name) &&
                regionWorker.equals(worker.regionWorker);
    }

    @Override
    public int hashCode() {
        final int factor = 31;
        int result = 1;
        result += factor * idWorker;
        result += factor + ((surname == null) ? 0 : surname.hashCode());
        result += factor + ((name == null) ? 0 : name.hashCode());
        result *= factor + (int) seniority;
        result += ((regionWorker == null) ? 0 : regionWorker.hashCode());
        return result;
    }
}
