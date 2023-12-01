package com.accountbook.domain;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class Test {

    private Long id;

    private String name;

    private String address;

    private LocalDateTime createdAt;

    private boolean trueYn;
}



