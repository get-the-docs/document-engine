package net.videki.templateutils.template.test.dto.contract;

import java.time.LocalDate;

public class Customer extends Person {
    private String phoneNumber;

    public Customer() {
    }

    public Customer(String name, LocalDate birthDate) {
        super(name, birthDate);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Customer setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

        return this;
    }
}
