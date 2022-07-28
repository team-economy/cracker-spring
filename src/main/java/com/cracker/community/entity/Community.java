package com.cracker.community.entity;

import com.cracker.comment.entity.Comment;
import com.cracker.place.entity.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column
    private String addr;

    @Column
    private String addrRoad;

    @Column
    private String coordX;

    @Column
    private String coordY;

    @Column
    private String phoneNum;

    @Column
    private String cate;

    @Column
    private String markerPic;

    @Column
    private String url;

    @Column
    private Integer count;

    @OneToMany(mappedBy = "community")
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Community(String name, String addr, String addrRoad, String coordX, String coordY, String phoneNum, String cate, String markerPic, String url, Integer count) {
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.coordX = coordX;
        this.coordY = coordY;
        this.phoneNum = phoneNum;
        this.cate = cate;
        this.markerPic = markerPic;
        this.url = url;
        this.count = count;
    }

    public void increaseCount(){
        this.count += 1;
    }

    public void decreaseCount(){
        this.count -= 1;
    }
}
