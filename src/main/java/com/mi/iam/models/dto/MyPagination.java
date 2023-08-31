package com.mi.iam.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPagination<T> {
  private List<T> data;
  private long totalData;
  private int page;
  private int totalPage;
  private int size;
}
