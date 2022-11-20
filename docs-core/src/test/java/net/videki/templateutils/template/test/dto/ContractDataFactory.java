package net.videki.templateutils.template.test.dto;

/*-
 * #%L
 * docs-core
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

import net.videki.templateutils.template.test.dto.contract.*;

import java.time.LocalDate;
import java.time.Month;

public class ContractDataFactory {
    
    public static Contract createContract() {
        final Contract result = new Contract();

        final Contractor contractor = new Contractor();
        contractor.setName("John Doe");
        contractor.setBirthDate(LocalDate.of(1970, Month.JULY, 20));
        result.setContractor(contractor);

        final ContractType contractType = new ContractType();
        contractType.setContractTypeName("Vintage Gold");
        contractType.setFee(1_500);
        contractType.setPaymentFrequency(PaymentFrequency.MONTHLY);
        result.setContractType(contractType);

        result.setSignDate(LocalDate.now());

        final Beneficiary bfJohnDoe = new Beneficiary("Jane Doe", LocalDate.of(1971, Month.APRIL, 2));
        bfJohnDoe.setPhoneNumber("+1 800 1234 567");
        bfJohnDoe.getBeneficiaries().add(new Customer("Jim Doe", LocalDate.of(1975, Month.AUGUST, 11)).setPhoneNumber("+1 800 2234 567"));
        bfJohnDoe.getBeneficiaries().add(new Customer("Tim Doe", LocalDate.of(1976, Month.AUGUST, 12)).setPhoneNumber("+1 800 2234 568"));
        bfJohnDoe.getBeneficiaries().add(new Customer("Pim Doe", LocalDate.of(1977, Month.AUGUST, 13)).setPhoneNumber("+1 800 2234 569"));
        result.getBeneficiaries().add(bfJohnDoe);

        final Beneficiary bfJennyMack = new Beneficiary("Jenny Mack", LocalDate.of(1951, Month.AUGUST, 11));
        bfJennyMack.setPhoneNumber("+1 800 1234 568");
        bfJohnDoe.getBeneficiaries().add(new Customer("Jack Ryan", LocalDate.of(1962, Month.AUGUST, 11)).setPhoneNumber("+1 800 3234 567"));
        bfJohnDoe.getBeneficiaries().add(new Customer("John Goodall", LocalDate.of(1946, Month.AUGUST, 11)).setPhoneNumber("+1 800 3234 568"));
        bfJohnDoe.getBeneficiaries().add(new Customer("Mortimer Young", LocalDate.of(1991, Month.AUGUST, 11)).setPhoneNumber("+1 800 3234 569"));
        bfJohnDoe.getBeneficiaries().add(new Customer("Zack Black", LocalDate.of(1987, Month.AUGUST, 11)).setPhoneNumber("+1 800 3234 560"));
        result.getBeneficiaries().add(bfJennyMack);

        return result;
    }
}
