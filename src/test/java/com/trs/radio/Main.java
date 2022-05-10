package com.trs.radio;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        String s = "21";
        int x = Integer.parseInt(s.substring(0, 1));
        int y = Integer.parseInt(s.substring(1, 2));
        System.out.println(x + y);
    }

}
