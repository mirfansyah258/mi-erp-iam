package com.mi.iam.helpers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mi.iam.models.dto.MyPagination;

public class PaginationHelper {
  public Pageable PaginationController(String sortBy, Pageable pageable){
    // Parse sorting parameter for column name and sorting direction
    Sort sort = Sort.unsorted();
    if (sortBy != null && !sortBy.isEmpty()) {
      String[] sortParts = sortBy.split(",");
      String columnName = sortParts[0];
      String sortDirection = sortParts.length > 1 ? sortParts[1] : "asc";

      sort = Sort.by(Sort.Direction.fromString(sortDirection), columnName);
    }

    // Create a new pageable with custom sorting
    Integer pageNum = pageable.getPageNumber() > 0 ? (pageable.getPageNumber() - 1) : 0;
    return PageRequest.of(pageNum, pageable.getPageSize(), sort);
  }

  public static <T> MyPagination<T> PaginationService(Page<T> page) {
    return new MyPagination<T>(
      page.getContent(),
      page.getTotalElements(),
      page.getContent().size() > 0 ? page.getNumber() + 1 : 0,
      page.getTotalPages(),
      page.getSize()
    );
  }
}
