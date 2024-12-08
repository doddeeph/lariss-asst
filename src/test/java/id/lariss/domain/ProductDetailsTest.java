package id.lariss.domain;

import static id.lariss.domain.CaseSizeTestSamples.*;
import static id.lariss.domain.ColorTestSamples.*;
import static id.lariss.domain.ConnectivityTestSamples.*;
import static id.lariss.domain.DescriptionTestSamples.*;
import static id.lariss.domain.MaterialTestSamples.*;
import static id.lariss.domain.MemoryTestSamples.*;
import static id.lariss.domain.ProcessorTestSamples.*;
import static id.lariss.domain.ProductDetailsTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static id.lariss.domain.ScreenTestSamples.*;
import static id.lariss.domain.StorageTestSamples.*;
import static id.lariss.domain.StrapColorTestSamples.*;
import static id.lariss.domain.StrapSizeTestSamples.*;
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

    @Test
    void screenTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Screen screenBack = getScreenRandomSampleGenerator();

        productDetails.setScreen(screenBack);
        assertThat(productDetails.getScreen()).isEqualTo(screenBack);

        productDetails.screen(null);
        assertThat(productDetails.getScreen()).isNull();
    }

    @Test
    void connectivityTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Connectivity connectivityBack = getConnectivityRandomSampleGenerator();

        productDetails.setConnectivity(connectivityBack);
        assertThat(productDetails.getConnectivity()).isEqualTo(connectivityBack);

        productDetails.connectivity(null);
        assertThat(productDetails.getConnectivity()).isNull();
    }

    @Test
    void materialTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        productDetails.setMaterial(materialBack);
        assertThat(productDetails.getMaterial()).isEqualTo(materialBack);

        productDetails.material(null);
        assertThat(productDetails.getMaterial()).isNull();
    }

    @Test
    void caseSizeTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        CaseSize caseSizeBack = getCaseSizeRandomSampleGenerator();

        productDetails.setCaseSize(caseSizeBack);
        assertThat(productDetails.getCaseSize()).isEqualTo(caseSizeBack);

        productDetails.caseSize(null);
        assertThat(productDetails.getCaseSize()).isNull();
    }

    @Test
    void strapColorTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        StrapColor strapColorBack = getStrapColorRandomSampleGenerator();

        productDetails.setStrapColor(strapColorBack);
        assertThat(productDetails.getStrapColor()).isEqualTo(strapColorBack);

        productDetails.strapColor(null);
        assertThat(productDetails.getStrapColor()).isNull();
    }

    @Test
    void strapSizeTest() {
        ProductDetails productDetails = getProductDetailsRandomSampleGenerator();
        StrapSize strapSizeBack = getStrapSizeRandomSampleGenerator();

        productDetails.setStrapSize(strapSizeBack);
        assertThat(productDetails.getStrapSize()).isEqualTo(strapSizeBack);

        productDetails.strapSize(null);
        assertThat(productDetails.getStrapSize()).isNull();
    }
}
