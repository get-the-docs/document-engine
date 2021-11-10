/*
 * Copyright (c) 2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.videki.templateutils.template.test.dto.contract;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Levente Ban
 * @since v1.0
 *
 * <p>A beneficiary of a contract.</p>
 *
 * A beneficiary is a customer (with a phone no) who may have other beneficiaries.
 */
public class Beneficiary extends Customer {
    /** The beneficiaries list for the custmer who the customer may call cheaper. */
    private List<Customer> beneficiaries;

    public Beneficiary() {
        init();
    }

    public Beneficiary(String name, LocalDate birthDate) {
        super(name, birthDate);

        init();
    }

    private void init() {
        beneficiaries = new LinkedList<>();
    }

    public List<Customer> getBeneficiaries() {
        return beneficiaries;
    }
}
