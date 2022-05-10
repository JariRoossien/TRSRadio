package com.trs.radio.meme.provider;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class EzTextProvider {

    String[] ezResults;

    public EzTextProvider() throws IOException {
        File f = new File("ez.txt");
        ezResults = Files.readAllLines(f.toPath()).toArray(new String[0]);
    }

    public String getNextEz() {
        return ezResults[(int) (Math.random() * ezResults.length)];
    }
}
