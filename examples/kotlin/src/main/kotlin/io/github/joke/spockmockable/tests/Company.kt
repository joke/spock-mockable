package io.github.joke.spockmockable.tests

class Company(val name: String, val city: String) {
    private val employees = mutableListOf<Person>()

    fun hire(candidates: List<Person>): List<Person> {
        employees.addAll(candidates)
        // the predicate lambda is compiled to private static method with special characters in the name
        // which triggers an  Illegal method name boolean  because byte-buddy is too strict
        employees.removeIf { person -> person.address.street != this.city }
        return employees
    }
}
