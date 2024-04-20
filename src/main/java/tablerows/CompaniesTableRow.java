package tablerows;

import javafx.beans.property.SimpleStringProperty;

public class CompaniesTableRow {

    private SimpleStringProperty name;
    private SimpleStringProperty street;
    private SimpleStringProperty city;
    private SimpleStringProperty state;
    private SimpleStringProperty zipcode;

    public CompaniesTableRow(String name, String street, String city, String state, String zipcode) {
        this.name = new SimpleStringProperty(name);
        this.street = new SimpleStringProperty(street);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zipcode = new SimpleStringProperty(zipcode);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStreet() {
        return street.get();
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getZipcode() {
        return zipcode.get();
    }

    public void setZipcode(String zipcode) {
        this.zipcode.set(zipcode);
    }
}
