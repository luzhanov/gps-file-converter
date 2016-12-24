package com.waypointer.gpsfileconverter;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public final class FileConverterUtils {

    private FileConverterUtils() {
    }

    //todo: use replaceAll
    public static String removeLineBreaks(String string) {
        if (string != null && !string.isEmpty()) {
            return string.replace("\n", "").replace("\r", "").trim();
        }
        return string;
    }

    public static String detectNullString(String string) {
        if (isNotBlank(string) && "null".equalsIgnoreCase(string.trim())) {
            return null;
        }
        return string;
    }

    public static String mergeStringIfNotTheSame(String string1, String string2) {
        if (isBlank(string1) || isBlank(string2)) {
            if (isBlank(string1) && isBlank(string2)) {
                return null;
            } else if (isBlank(string1)) {
                return string2;
            } else {
                return string1;
            }
        } else if (string1.trim().equalsIgnoreCase(string2.trim())) {
            return string1;
        } else {
            return string1 + " / " + string2;
        }
    }

    public static String getFirst2KBFromIs(InputStream is) {
        String firstString = "";
        try {
            byte[] buffer = new byte[2048];
            int readCnt = is.read(buffer, 0, buffer.length);
            if (readCnt != -1) {
                firstString = new String(buffer);
            }
        } catch (IOException e) {
            logger.error("Exception during getFirst2KBFromIs", e);
        }
        return firstString;
    }

}
