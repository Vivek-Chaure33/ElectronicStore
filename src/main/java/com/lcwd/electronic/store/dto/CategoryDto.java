package com.lcwd.electronic.store.dto;

import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {



    private String categoryId;

    @NotBlank(message = "title is required !!")
    @Size(min = 4 , message = "title must be min 4 character")
    private String title;

    @NotBlank(message = "Description must be required !!")
    @Size(min = 6 ,message = "Description must be min 6 character")
    private String description;

    @ImageNameValid
    private String coverImage;




}
