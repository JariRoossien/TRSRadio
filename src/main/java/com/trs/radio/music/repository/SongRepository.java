package com.trs.radio.music.repository;

import com.trs.radio.music.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {

    List<Song> findAllByCategoryIs(Song.Category c);

    Page<Song> findAllByCategoryIs(Song.Category c, Pageable pageable);

    Optional<Song> findByTitle(String title);

    long countAllByCategoryIs(Song.Category c);
}
