package com.lcwd.electronic.store.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private Long totalElements;

    private int totalPages;

    private boolean lastPage;

}
