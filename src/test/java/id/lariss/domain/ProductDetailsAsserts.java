package id.lariss.domain;

import static id.lariss.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDetailsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductDetailsAllPropertiesEquals(ProductDetails expected, ProductDetails actual) {
        assertProductDetailsAutoGeneratedPropertiesEquals(expected, actual);
        assertProductDetailsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductDetailsAllUpdatablePropertiesEquals(ProductDetails expected, ProductDetails actual) {
        assertProductDetailsUpdatableFieldsEquals(expected, actual);
        assertProductDetailsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductDetailsAutoGeneratedPropertiesEquals(ProductDetails expected, ProductDetails actual) {
        assertThat(expected)
            .as("Verify ProductDetails auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductDetailsUpdatableFieldsEquals(ProductDetails expected, ProductDetails actual) {
        assertThat(expected)
            .as("Verify ProductDetails relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getPrice()).as("check price").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPrice()))
            .satisfies(e -> assertThat(e.getThumbnail()).as("check thumbnail").isEqualTo(actual.getThumbnail()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductDetailsUpdatableRelationshipsEquals(ProductDetails expected, ProductDetails actual) {
        assertThat(expected)
            .as("Verify ProductDetails relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getColor()).as("check color").isEqualTo(actual.getColor()))
            .satisfies(e -> assertThat(e.getProcessor()).as("check processor").isEqualTo(actual.getProcessor()))
            .satisfies(e -> assertThat(e.getMemory()).as("check memory").isEqualTo(actual.getMemory()))
            .satisfies(e -> assertThat(e.getStorage()).as("check storage").isEqualTo(actual.getStorage()))
            .satisfies(e -> assertThat(e.getScreen()).as("check screen").isEqualTo(actual.getScreen()))
            .satisfies(e -> assertThat(e.getConnectivity()).as("check connectivity").isEqualTo(actual.getConnectivity()))
            .satisfies(e -> assertThat(e.getMaterial()).as("check material").isEqualTo(actual.getMaterial()))
            .satisfies(e -> assertThat(e.getCaseSize()).as("check caseSize").isEqualTo(actual.getCaseSize()))
            .satisfies(e -> assertThat(e.getStrapColor()).as("check strapColor").isEqualTo(actual.getStrapColor()))
            .satisfies(e -> assertThat(e.getStrapSize()).as("check strapSize").isEqualTo(actual.getStrapSize()));
    }
}
