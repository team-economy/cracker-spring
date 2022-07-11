package com.cracker.place.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String addr;

    @Column
    private String addrRoad;

    @Column(nullable = false)
    private String coordX;

    @Column(nullable = false)
    private String coordY;

    @Column
    private String phoneNum;

    @Column
    private String cate;

    @Builder
    public Place(String name, String addr, String addrRoad,
                 String coordX, String coordY,String phoneNum, String cate) {
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.coordX = coordX;
        this.coordY = coordY;
        this.phoneNum = phoneNum;
        this.cate = cate;
    }

}
