package tablerows;

import javafx.beans.property.SimpleStringProperty;

public class InvoiceTableRow {

    private SimpleStringProperty description;
    private SimpleStringProperty price;

    public InvoiceTableRow(String description, String price) {
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleStringProperty(price);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }
}
