package com.cracker.community.service;

import com.cracker.community.entity.Community;
import com.cracker.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Transactional
    public Community communitySearch(Long id){
        return communityRepository.getById(id);
    }
}
