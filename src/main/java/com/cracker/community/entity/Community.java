package com.cracker.community.entity;

import com.cracker.comment.domain.Comment;
import com.cracker.place.domain.Place;
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

    @OneToMany(mappedBy = "community")
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();
}
