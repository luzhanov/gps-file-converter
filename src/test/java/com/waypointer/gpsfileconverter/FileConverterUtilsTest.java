package com.waypointer.gpsfileconverter;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class FileConverterUtilsTest {

    @Test
    @Parameters({"one, two, one / two",
            "One, oNe, One",
            ", one, one",
            "one, , one"})
    public void mergesStringIfNotTheSame(String str1, String str2, String result) {
        assertThat(FileConverterUtils.mergeStringIfNotTheSame(str1, str2)).isEqualTo(result);
    }

    @Test
    public void mergesStringIfNotTheSameForNulls() {
        assertThat(FileConverterUtils.mergeStringIfNotTheSame(null, "str")).isEqualTo("str");
        assertThat(FileConverterUtils.mergeStringIfNotTheSame("str", null)).isEqualTo("str");
        assertThat(FileConverterUtils.mergeStringIfNotTheSame(null, null)).isEqualTo(null);
    }

}
