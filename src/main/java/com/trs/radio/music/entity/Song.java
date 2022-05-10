package com.trs.radio.music.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@Entity
public class Song {

    @Enumerated(STRING)
    private Category category;

    @Id
    private String songUrl;

    private String title;
    private String singer;
    private long duration;

    public Song() {
    }

    public Song(Category cat, String songURL) {
        this.category = cat;
        this.songUrl = songURL;
        this.title = null;
        this.singer = null;
    }

    public void setDuration(long length) {
        this.duration = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(songUrl, song.songUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songUrl);
    }

    public enum Category {
        EDM("EDM"),
        HIPHOP("Hiphop"),
        ROCK("Rock"),
        POP("Pop"),
        DNB("DnB "),
        TWOTHOUSANDS("2000s"),
        CHILL("Chill");

        private final String display;

        Category(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}
