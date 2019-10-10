package net.videki.templateutils.template.test.dto.contract;


import net.videki.templateutils.template.core.dto.ITemplate;

import java.time.LocalDate;

public class Person implements ITemplate {
    private String name;
    private LocalDate birthDate;

    public Person() {
    }

    public Person(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
