package net.videki.templateutils.examples.singletemplatepojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.videki.templateutils.template.core.dto.ITemplate;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements ITemplate {
    private String name;
    private LocalDate birthDate;
    private LocalDate signDate;
}
