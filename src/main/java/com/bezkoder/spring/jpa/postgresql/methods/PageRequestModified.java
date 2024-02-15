package com.bezkoder.spring.jpa.postgresql.methods;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestModified extends PageRequest {

  public PageRequestModified(int page, int size, Sort sort) {
    super(page, size, sort);
  }
}
