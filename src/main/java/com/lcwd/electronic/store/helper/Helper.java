package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dto.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U , V>PageableResponse<V> getPageableResponse(Page<U> page , Class<V> type){

        List<U> entitis = page.getContent();

        List<V> dtoList = entitis.stream().map(entity -> new ModelMapper().map(entity, type)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();

        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setLastPage(page.isLast());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());

        return response;
    }



}
