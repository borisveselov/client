package edu.courseproject.client.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Sklad {
    private long idSklad;
    private String address;
    private String fax;

    public Sklad() {
    }

    public Sklad(long idSklad, String address, String fax) {
        this.idSklad = idSklad;
        this.address = address;
        this.fax = fax;
    }

    public Sklad(ResultSet resultSet) {
        try {
            this.idSklad = resultSet.getLong("idsklad");
            this.fax = resultSet.getString("fax");
            this.address = resultSet.getString("address");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getIdSklad() {
        return idSklad;
    }

    public void setIdSklad(long idSklad) {
        this.idSklad = idSklad;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
