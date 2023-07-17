package com.lcwd.electronic.store.entity;

import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @Column(name="id")
    private String categoryId;
    @Column(name = "category_title" , length = 20)
    private String title;
    @Column(name = "category_desc" , length =100)
    private String description;
    private String coverImage;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Product> products=new ArrayList<>();
}
