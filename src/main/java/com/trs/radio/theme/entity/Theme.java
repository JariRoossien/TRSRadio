package com.trs.radio.theme.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Theme {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ChannelTheme> channelThemes = new ArrayList<>();

    public ChannelTheme getChannel(long id) {
        for (ChannelTheme theme : channelThemes) {
            if (theme.getChannelId() == id) return theme;
        }
        return null;
    }

    @Entity
    @Data
    public static class ChannelTheme {

        public String channelName;
        @Id
        @GeneratedValue
        long id;

        long channelId;

        @ManyToOne
        Theme theme;

        public ChannelTheme(long id, Theme theme, String channelName) {
            this.channelId = id;
            this.theme = theme;
            this.channelName = channelName;
        }

        public ChannelTheme() {

        }
    }
}
