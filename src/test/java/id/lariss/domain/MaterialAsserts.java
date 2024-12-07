package id.lariss.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MaterialAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialAllPropertiesEquals(Material expected, Material actual) {
        assertMaterialAutoGeneratedPropertiesEquals(expected, actual);
        assertMaterialAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialAllUpdatablePropertiesEquals(Material expected, Material actual) {
        assertMaterialUpdatableFieldsEquals(expected, actual);
        assertMaterialUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialAutoGeneratedPropertiesEquals(Material expected, Material actual) {
        assertThat(expected)
            .as("Verify Material auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialUpdatableFieldsEquals(Material expected, Material actual) {
        assertThat(expected)
            .as("Verify Material relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialUpdatableRelationshipsEquals(Material expected, Material actual) {
        // empty method
    }
}
