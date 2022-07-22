package com.cracker.community.repository;

import com.cracker.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Object> {
    Community findByAddr(String addr);
}
