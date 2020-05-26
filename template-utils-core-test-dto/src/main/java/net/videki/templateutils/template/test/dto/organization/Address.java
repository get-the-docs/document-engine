package net.videki.templateutils.template.test.dto.organization;

public class Address {
    private String zip;
    private String city;
    private String address;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullAddress() {
        return String.format("%s %s, %s", this.zip, this.city, this.address);
    }
}
