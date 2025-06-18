package org.getthedocs.documentengine.test.dto.contract;

/*-
 * #%L
 * docs-core-test-dto
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

public class Customer extends Person {
    private String phoneNumber;

    public Customer() {
    }

    public Customer(String name, LocalDate birthDate) {
        super(name, birthDate);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Customer setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

        return this;
    }
}
