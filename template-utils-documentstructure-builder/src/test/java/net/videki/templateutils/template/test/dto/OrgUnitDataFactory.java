package net.videki.templateutils.template.test.dto;

import net.videki.templateutils.template.test.dto.organization.Address;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;

public class OrgUnitDataFactory {
    public static OrganizationUnit createOrgUnit() {
        final OrganizationUnit result = new OrganizationUnit();

        result.setOrgCode("PB");
        result.setName("Vintage Services - Palm beach");

        final Address address = new Address();
        address.setZip("Y-1234567");
        address.setCity("Simply City");
        address.setAddress("Main blvd 432");

        result.setAddress(address);

        return result;
    }

}
