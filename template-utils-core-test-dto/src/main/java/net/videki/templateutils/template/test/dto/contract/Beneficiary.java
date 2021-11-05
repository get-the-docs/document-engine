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
