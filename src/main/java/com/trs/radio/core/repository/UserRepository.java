package com.trs.radio.core.repository;

import com.trs.radio.core.entity.TRSUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TRSUser, Long> {

}
