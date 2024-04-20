package invoice;

public class InvoiceRow {

    private final String description;
    private final int price;

    public InvoiceRow(String description, int price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
