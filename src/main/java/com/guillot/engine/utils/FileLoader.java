package com.guillot.engine.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.util.ResourceLoader;

public class FileLoader {

    public static InputStream streamFromResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static File fileFromResource(String path) {
        return new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
    }

    public static Font fontFromResource(String path) throws FontFormatException, IOException {
        InputStream inputStream = ResourceLoader.getResourceAsStream(path);

        return Font.createFont(Font.TRUETYPE_FONT, inputStream);
    }
}
