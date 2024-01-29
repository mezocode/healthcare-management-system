package com.mezocode.healthcare.patient.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Embeddable
@NoArgsConstructor
@Setter
@Getter
public class Address {

    private String street;
    private String city;
    private String state;
    private String zipCode;

    public boolean isValid() {
        // Simple check for zip code validity (5 digits).
        // It could be made more complex, like checking against a database of real zip codes.
        if (zipCode == null || !zipCode.matches("\\d{5}")) {
            return false;
        }

        // Simple check for state validity (one of the 50 U.S. states).
        // This could be a hardcoded list or could be checked against a database.
        List<String> validStates = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
                "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN",
                "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");

        return state != null && validStates.contains(state);

    }
}
