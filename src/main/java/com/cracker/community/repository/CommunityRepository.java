package com.cracker.community.repository;

import com.cracker.community.entity.Community;
import com.cracker.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Community findByAddr(String addr);
}
