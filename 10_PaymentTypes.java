
class UpiPayment extends Payment {

    @Override
    public boolean pay(double amount) {
        System.out.printf("Processing UPI payment of Rs. %.2f...%n", amount);
        return true;
    }
}

class CardPayment extends Payment {

    @Override
    public boolean pay(double amount) {
        System.out.printf("Processing Card payment of Rs. %.2f...%n", amount);
        return true;
    }
}

class CashPayment extends Payment {

    @Override
    public boolean pay(double amount) {
        System.out.printf("Processing Cash payment of Rs. %.2f...%n", amount);
        return true;
    }
}
