package com.lcwd.electronic.store.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String prodructId;
    private String title;
    private int price;
    @Column(length = 1000)
    private String description;
    private int quantity;
    private int discountedPrice;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


}
