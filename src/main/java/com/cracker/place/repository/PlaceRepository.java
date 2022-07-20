package com.cracker.place.repository;

import com.cracker.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByAddr(String addr);
}
