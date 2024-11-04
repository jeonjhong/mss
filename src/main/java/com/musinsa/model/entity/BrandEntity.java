package com.musinsa.model.entity;

import com.musinsa.model.IdGettable;
import com.musinsa.model.dto.CommonIdResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Entity
@Table(name = "Brand")
@Getter
@Setter
public class BrandEntity extends BaseEntity implements IdGettable, Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;
    @Column(name = "brand_name")
    private String name;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }

    public CommonIdResponse toResponse() {
        return CommonIdResponse.of(this);
    }
}
