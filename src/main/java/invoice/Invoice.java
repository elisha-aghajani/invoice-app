package invoice;

import java.util.List;

public class Invoice {

    private final List<InvoiceRow> invoiceRows;
    private final BusinessEntity company;
    private final BusinessEntity customer;
    private final Payment payment;

    public Invoice(List<InvoiceRow> rows, BusinessEntity company, BusinessEntity customer, Payment payment) {
        this.invoiceRows = rows;
        this.company = company;
        this.customer = customer;
        this.payment = payment;
    }

    public List<InvoiceRow> getInvoiceRows() {
        return invoiceRows;
    }

    public BusinessEntity getCompany() {
        return company;
    }

    public BusinessEntity getCustomer() {
        return customer;
    }

    public Payment getPayment() {
        return payment;
    }
}
