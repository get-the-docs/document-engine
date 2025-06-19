package org.getthedocs.documentengine.test.dto.contract;

/*-
 * #%L
 * docs-core-dto-test
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

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Levente Ban
 * @since v1.0
 *
 * <p>Sample contract data for an imagined telephone company.</p>
 *
 * The sample describes a phone fleet contract where one person pays the bill
 * for more than one beneficiaries where the beneficiaries may also have discounted calls.
 */
public class Contract {
    /** The contractor person */
    private Contractor contractor;
    /** The actual contract type */
    private ContractType contractType;
    /** The list of the beneficiaries within the contran */
    private final List<Beneficiary> beneficiaries = new LinkedList<>();
    /** Contract dign date */
    private LocalDate signDate;

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }
}
