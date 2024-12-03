package id.lariss.domain;

import static id.lariss.domain.ColorTestSamples.*;
import static id.lariss.domain.DescriptionTestSamples.*;
import static id.lariss.domain.MemoryTestSamples.*;
import static id.lariss.domain.ProcessorTestSamples.*;
import static id.lariss.domain.ProductDetailsTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static id.lariss.domain.StorageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDetails.class);
        ProductDetails productDetails1 = getProductDetailsSample1();
        ProductDetails productDetails2 = new ProductDetails();
        assertThat(productDetails1).isNotEqualTo(productDetails2);

        productDetails2.setId(productDetails1.getId());
        assertThat(productDetails1).isEqualTo(productDetails2);

        productDetails2 = getProductDetailsSample2();
        assertThat(productDetails1).isNotEqualTo(productDetails2);
    }

    @Test
    void productTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productDetails.setProduct(productBack);
        assertThat(productDetails.getProduct()).isEqualTo(productBack);

        productDetails.product(null);
        assertThat(productDetails.getProduct()).isNull();
    }

    @Test
    void descriptionTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Description descriptionBack = getDescriptionRandomSampleGenerator();

        productDetails.setDescription(descriptionBack);
        assertThat(productDetails.getDescription()).isEqualTo(descriptionBack);

        productDetails.description(null);
        assertThat(productDetails.getDescription()).isNull();
    }

    @Test
    void colorTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Color colorBack = getColorRandomSampleGenerator();

        productDetails.setColor(colorBack);
        assertThat(productDetails.getColor()).isEqualTo(colorBack);

        productDetails.color(null);
        assertThat(productDetails.getColor()).isNull();
    }

    @Test
    void processorTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Processor processorBack = getProcessorRandomSampleGenerator();

        productDetails.setProcessor(processorBack);
        assertThat(productDetails.getProcessor()).isEqualTo(processorBack);

        productDetails.processor(null);
        assertThat(productDetails.getProcessor()).isNull();
    }

    @Test
    void memoryTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Memory memoryBack = getMemoryRandomSampleGenerator();

        productDetails.setMemory(memoryBack);
        assertThat(productDetails.getMemory()).isEqualTo(memoryBack);

        productDetails.memory(null);
        assertThat(productDetails.getMemory()).isNull();
    }

    @Test
    void storageTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Storage storageBack = getStorageRandomSampleGenerator();

        productDetails.setStorage(storageBack);
        assertThat(productDetails.getStorage()).isEqualTo(storageBack);

        productDetails.storage(null);
        assertThat(productDetails.getStorage()).isNull();
    }
}
