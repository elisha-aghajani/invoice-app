package invoice;

public class BusinessEntity {

    private final String name;

    private final Address address;

    public BusinessEntity(String name, String streetAddress, String city, String state, String zipcode) {
        this.name = name;
        this.address = new Address(streetAddress, city, state, zipcode);
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
