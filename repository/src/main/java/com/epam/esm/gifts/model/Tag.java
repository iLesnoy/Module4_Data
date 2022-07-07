package com.epam.esm.gifts.model;


import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tag")
@Entity
public class Tag /*extends Auditable<String>*/{

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, updatable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "tagList")
    private Set<GiftCertificate> certificates;

}
