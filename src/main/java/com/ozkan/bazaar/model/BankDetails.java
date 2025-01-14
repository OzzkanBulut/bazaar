package com.ozkan.bazaar.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BankDetails {

    private String accountNumber;

    private String accountHolderName;

    private String ifscCode;


}
