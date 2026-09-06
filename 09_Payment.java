
abstract class Payment {

    private String transactionId;

    protected Payment() {
        transactionId = "TXN" + System.currentTimeMillis();
    }

    public String getTransactionId() {
        return transactionId;
    }

    // Abstraction: subclasses provide the actual payment behaviour.
    public abstract boolean pay(double amount);
}
