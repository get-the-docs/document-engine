package net.videki.templateutils.template.test.dto;

import net.videki.templateutils.template.test.dto.officer.Officer;

public class OfficerDataFactory {
    public static Officer createOfficer() {
        final Officer result = new Officer();

        result.setName("Chuck Norris");
        result.setOrgCode("PB-001");
        result.setLogin("PB\\cnorris");

        return result;
    }

}
