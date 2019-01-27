package com.github.hondaYoshitaka.model.entity.visitor;

import lombok.Data;
import org.seasar.doma.*;

@Entity
@Table(name = "visitors")
@Data
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hostId;
}
