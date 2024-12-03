package id.lariss.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProcessorAllPropertiesEquals(Processor expected, Processor actual) {
        assertProcessorAutoGeneratedPropertiesEquals(expected, actual);
        assertProcessorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProcessorAllUpdatablePropertiesEquals(Processor expected, Processor actual) {
        assertProcessorUpdatableFieldsEquals(expected, actual);
        assertProcessorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProcessorAutoGeneratedPropertiesEquals(Processor expected, Processor actual) {
        assertThat(expected)
            .as("Verify Processor auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProcessorUpdatableFieldsEquals(Processor expected, Processor actual) {
        assertThat(expected)
            .as("Verify Processor relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProcessorUpdatableRelationshipsEquals(Processor expected, Processor actual) {
        // empty method
    }
}