package com.trs.radio.core.entity;

import com.trs.radio.music.entity.Song;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TRSUser {
    @Id
    long id;

    private int balance;

    public TRSUser(long id) {
        this.id = id;
    }

    public TRSUser() {

    }

}
