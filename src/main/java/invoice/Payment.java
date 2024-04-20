package invoice;

public class Payment {

    private final int subTotal;
    private final int paid;
    private final int total;

    public Payment(int subTotal, int paid, int total) {
        this.subTotal = subTotal;
        this.paid = paid;
        this.total = total;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public int getPaid() {
        return paid;
    }

    public int getTotal() {
        return total;
    }
}
