package com.cracker.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceCreateRequestDto {
    String name;
    String addr;
    String addrRoad;
    String coordX;
    String coordY;
    String phoneNum;
    String cate;
}
