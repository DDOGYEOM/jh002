package net.openobject.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.openobject.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CovidTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Covid.class);
        Covid covid1 = new Covid();
        covid1.setId(1L);
        Covid covid2 = new Covid();
        covid2.setId(covid1.getId());
        assertThat(covid1).isEqualTo(covid2);
        covid2.setId(2L);
        assertThat(covid1).isNotEqualTo(covid2);
        covid1.setId(null);
        assertThat(covid1).isNotEqualTo(covid2);
    }
}
