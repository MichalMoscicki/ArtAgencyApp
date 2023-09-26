package com.immpresariat.ArtAgencyApp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static <T, E> PageResponse<T> createResponse(Page<E> page, List<T> content) {
        PageResponse<T> response = new PageResponse<>();

        response.setContent(content);
        response.setPageSize(page.getSize());
        response.setPageNo(page.getNumber());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }
}
