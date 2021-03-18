package net.openobject.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.openobject.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etest.class);
        Etest etest1 = new Etest();
        etest1.setId(1L);
        Etest etest2 = new Etest();
        etest2.setId(etest1.getId());
        assertThat(etest1).isEqualTo(etest2);
        etest2.setId(2L);
        assertThat(etest1).isNotEqualTo(etest2);
        etest1.setId(null);
        assertThat(etest1).isNotEqualTo(etest2);
    }
}
