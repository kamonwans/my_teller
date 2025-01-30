package com.teller.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TellerResponse<T>  {
    private String code;
    private String status;
    private T data;
}
