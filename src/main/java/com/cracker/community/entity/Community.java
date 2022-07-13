package com.cracker.community.entity;

import com.cracker.comment.domain.Comment;
import com.cracker.place.domain.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    private String phoneNum;

    @Column
    private String cate;

    @OneToMany(mappedBy = "community")
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Community(String name, String addr, String addrRoad, String phoneNum, String cate) {
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.phoneNum = phoneNum;
        this.cate = cate;
    }
}
