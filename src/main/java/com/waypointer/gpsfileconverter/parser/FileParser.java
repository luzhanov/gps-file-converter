package com.waypointer.gpsfileconverter.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

public interface FileParser<T> {

    List<T> parseFile(BufferedInputStream is);

    boolean isParsableFile(BufferedInputStream bis) throws IOException;

    static boolean isParsableFileCheck(BufferedInputStream is, String... checks) throws IOException {
        boolean result = false;
        is.mark(2049);

        byte[] buffer = new byte[2048];
        int readCnt = is.read(buffer, 0, buffer.length);

        if (readCnt != -1) {
            String firstString = new String(buffer);
            for (String check : checks) {
                if (firstString.contains(check)) {
                    result = true;
                    break;
                }
            }
        }

        is.reset(); //restore stream position
        return result;
    }

}
