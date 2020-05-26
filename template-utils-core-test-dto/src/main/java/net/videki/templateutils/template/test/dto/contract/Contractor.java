package net.videki.templateutils.template.test.dto.contract;

import java.time.LocalDate;
import java.time.Period;

public class Contractor {
    private String name;
    private LocalDate birthDate;

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

    public int getAge() {
        if (this.birthDate != null) {
            return Period.between(this.birthDate, LocalDate.now()).getYears();
        } else {
            return 0;
        }
    }
}
