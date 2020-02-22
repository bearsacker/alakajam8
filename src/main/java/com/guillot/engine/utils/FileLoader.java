package com.guillot.engine.utils;

import java.io.File;
import java.io.InputStream;

public class FileLoader {

    public static InputStream streamFromResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static File fileFromResource(String path) {
        return new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
    }

}
