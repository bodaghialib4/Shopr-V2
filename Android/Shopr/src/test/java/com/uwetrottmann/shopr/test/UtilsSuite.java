
package com.uwetrottmann.shopr.test;

import static org.fest.assertions.api.Assertions.assertThat;

import com.uwetrottmann.shopr.utils.Utils;

import org.junit.Test;

public class UtilsSuite {

    @Test
    public void testExtractFirstUrl() {

        String actual = Utils
                .extractFirstUrl("http://i1.ztat.net/detail/MA/32/1D/03/UA/00/MA321D03U-A00@1.1.jpg | http://i2.ztat.net/detail/MA/32/1D/03/UA/00/MA321D03U-A00@2.1.jpg");
        String expected = "http://i1.ztat.net/detail/MA/32/1D/03/UA/00/MA321D03U-A00@1.1.jpg";
        assertThat(actual).isEqualTo(expected);

        // only one
        actual = Utils
                .extractFirstUrl("http://i1.ztat.net/detail/MA/32/1D/03/UA/00/MA321D03U-A00@1.1.jpg");
        expected = "http://i1.ztat.net/detail/MA/32/1D/03/UA/00/MA321D03U-A00@1.1.jpg";
        assertThat(actual).isEqualTo(expected);
    }

}
