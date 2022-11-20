package net.videki.templateutils.template.test.dto;

/*-
 * #%L
 * docs-documentstructure-builder
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
