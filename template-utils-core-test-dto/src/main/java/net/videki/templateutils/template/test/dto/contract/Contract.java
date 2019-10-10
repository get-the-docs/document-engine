package net.videki.templateutils.template.test.dto.contract;

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
