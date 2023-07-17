package com.lcwd.electronic.store.dto;

import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String prodructId;
    @NotBlank(message = "Title must be required")
    @Size(min = 4,message = "Minimum 4 character required")
    private String title;
    @NotBlank(message = "price must be required")
    private int price;
    @NotBlank(message = "Description must be required")
    @Size(min = 10 , message = "Minimum 10 character required")
    private String description;
    @NotBlank(message = "Quantity must be required")
    private int quantity;
    private int discountedPrice;
    @NotBlank(message = "Date must be required")
    private Date addedDate;
    private boolean live;
    private boolean stock;
    @ImageNameValid
    private String productImage;
    private CategoryDto category;


}
