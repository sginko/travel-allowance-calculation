package pl.sginko.travelallowance.model;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
class Travel {
    private Long id;

    BigDecimal travel;


}
