package io.github.joke.spockmockable.tests;

public final class Person {

    private String firstName = "John";
    private String lastName = "Doe";
    private final Address address = new Address("Murder Lane");

    private Person() {
    }

    protected final String getFirstName() {
        return firstName;
    }

    private String getLastName() {
        return lastName;
    }

    private final Address getAddress() {
        return address;
    }

    private final static class Address {
        private final String street;

        private Address(final String street) {
            this.street = street;
        }

        public String getStreet() {
            return street;
        }
    }

}
