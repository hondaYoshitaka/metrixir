package com.github.hondaYoshitaka.model.entity.host;

import lombok.Data;
import org.seasar.doma.*;

@Entity
@Table(name = "hosts")
@Data
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;

    private String tag;
}
