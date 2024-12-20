package id.lariss.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectivityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConnectivityAllPropertiesEquals(Connectivity expected, Connectivity actual) {
        assertConnectivityAutoGeneratedPropertiesEquals(expected, actual);
        assertConnectivityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConnectivityAllUpdatablePropertiesEquals(Connectivity expected, Connectivity actual) {
        assertConnectivityUpdatableFieldsEquals(expected, actual);
        assertConnectivityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConnectivityAutoGeneratedPropertiesEquals(Connectivity expected, Connectivity actual) {
        assertThat(expected)
            .as("Verify Connectivity auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConnectivityUpdatableFieldsEquals(Connectivity expected, Connectivity actual) {
        assertThat(expected)
            .as("Verify Connectivity relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConnectivityUpdatableRelationshipsEquals(Connectivity expected, Connectivity actual) {
        // empty method
    }
}
