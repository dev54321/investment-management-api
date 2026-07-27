package com.example.investmentmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "fund_investor",
            joinColumns = @JoinColumn(name = "fund_id"),
            inverseJoinColumns = @JoinColumn(name = "investor_id")
    )
    private Set<Investor> investors = new HashSet<>();
}
