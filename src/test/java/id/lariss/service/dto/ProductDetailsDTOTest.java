package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDetailsDTO.class);
        ProductDetailsDTO productDetailsDTO1 = new ProductDetailsDTO();
        productDetailsDTO1.setId(1L);
        ProductDetailsDTO productDetailsDTO2 = new ProductDetailsDTO();
        assertThat(productDetailsDTO1).isNotEqualTo(productDetailsDTO2);
        productDetailsDTO2.setId(productDetailsDTO1.getId());
        assertThat(productDetailsDTO1).isEqualTo(productDetailsDTO2);
        productDetailsDTO2.setId(2L);
        assertThat(productDetailsDTO1).isNotEqualTo(productDetailsDTO2);
        productDetailsDTO1.setId(null);
        assertThat(productDetailsDTO1).isNotEqualTo(productDetailsDTO2);
    }
}
