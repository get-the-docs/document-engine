package org.getthedocs.documentengine.examples.singletemplatepojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.getthedocs.documentengine.core.dto.ITemplate;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements ITemplate {
    private String name;
    private LocalDate birthDate;
    private LocalDate signDate;
}
