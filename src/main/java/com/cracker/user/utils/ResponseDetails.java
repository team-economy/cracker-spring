package com.cracker.user.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ResponseDetails {
    private Date timestamp;
    private Object data;
    private int httpStatus;
}