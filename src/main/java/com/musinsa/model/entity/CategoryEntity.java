package com.musinsa.model.entity;

import com.musinsa.model.IdGettable;
import com.musinsa.model.dto.CategoryResponse;
import com.musinsa.model.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Category")
@Getter
public class CategoryEntity extends BaseEntity implements IdGettable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "category_name")
    @Enumerated(EnumType.STRING)
    private Category name;

    public CategoryResponse toResponse() {
        return new CategoryResponse(this.id, this.name.getJsonSerializeValue());
    }
}
