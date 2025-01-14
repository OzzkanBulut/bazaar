package com.ozkan.bazaar.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BusinessDetails {

    private String businessName;

    private String businessEmail;

    private String businessMobile;

    private String businessAddress;

    private String logo;

    private String banner;
}
