// File Name: StockMarketSimulator.java

import java.util.*;

// ----------- Stock Class -------------
class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

// ----------- Portfolio Class -------------
class Portfolio {
    private Map<String, Integer> holdings; // stock symbol -> quantity

    public Portfolio() {
        holdings = new HashMap<>();
    }

    public void buyStock(String symbol, int quantity) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
    }

    public void sellStock(String symbol, int quantity) {
        if (holdings.containsKey(symbol)) {
            int currentQty = holdings.get(symbol);
            if (quantity >= currentQty) {
                holdings.remove(symbol);
            } else {
                holdings.put(symbol, currentQty - quantity);
            }
        }
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }
}

// ----------- Market Class -------------
class Market {
    private Map<String, Stock> stocks;
    private Random random;

    public Market() {
        stocks = new HashMap<>();
        random = new Random();

        // Initialize some stocks
        stocks.put("AAPL", new Stock("AAPL", 150.0));
        stocks.put("GOOG", new Stock("GOOG", 2800.0));
        stocks.put("TSLA", new Stock("TSLA", 700.0));
        stocks.put("AMZN", new Stock("AMZN", 3300.0));
    }

    public void updatePrices() {
        for (Stock stock : stocks.values()) {
            double changePercent = (random.nextDouble() * 10) - 5;
            double newPrice = stock.getPrice() + stock.getPrice() * (changePercent / 100);
            stock.setPrice(Math.round(newPrice * 100.0) / 100.0);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public Map<String, Stock> getAllStocks() {
        return stocks;
    }
}

// ----------- Main Application Class -------------
public class StockMarketSimulator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Market market = new Market();
        Portfolio portfolio = new Portfolio();

        boolean running = true;

        while (running) {
            System.out.println("\n--- Stock Market Simulator ---");
            System.out.println("1. View Stocks");
            System.out.println("2. Buy Stocks");
            System.out.println("3. Sell Stocks");
            System.out.println("4. View Portfolio");
            System.out.println("5. Update Market Prices");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.hasNextInt() ? scanner.nextInt() : -1;
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("\nAvailable Stocks:");
                    for (Stock stock : market.getAllStocks().values()) {
                        System.out.printf("%s : $%.2f%n", stock.getSymbol(), stock.getPrice());
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    Stock buyStock = market.getStock(buySymbol);
                    if (buyStock == null) {
                        System.out.println("Stock not found!");
                        break;
                    }
                    System.out.print("Enter quantity to buy: ");
                    int buyQty = scanner.nextInt();
                    scanner.nextLine();
                    if (buyQty <= 0) {
                        System.out.println("Invalid quantity.");
                        break;
                    }
                    portfolio.buyStock(buySymbol, buyQty);
                    System.out.println("Bought " + buyQty + " shares of " + buySymbol);
                    break;

                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    if (!portfolio.getHoldings().containsKey(sellSymbol)) {
                        System.out.println("You don't own any shares of this stock.");
                        break;
                    }
                    System.out.print("Enter quantity to sell: ");
                    int sellQty = scanner.nextInt();
                    scanner.nextLine();
                    if (sellQty <= 0) {
                        System.out.println("Invalid quantity.");
                        break;
                    }
                    portfolio.sellStock(sellSymbol, sellQty);
                    System.out.println("Sold " + sellQty + " shares of " + sellSymbol);
                    break;

                case 4:
                    System.out.println("\nYour Portfolio:");
                    Map<String, Integer> holdings = portfolio.getHoldings();
                    double totalValue = 0.0;
                    if (holdings.isEmpty()) {
                        System.out.println("Your portfolio is empty.");
                    } else {
                        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                            Stock stock = market.getStock(entry.getKey());
                            int qty = entry.getValue();
                            double value = stock.getPrice() * qty;
                            totalValue += value;
                            System.out.printf("%s: %d shares (Value: $%.2f)%n", entry.getKey(), qty, value);
                        }
                        System.out.printf("Total Portfolio Value: $%.2f%n", totalValue);
                    }
                    break;

                case 5:
                    market.updatePrices();
                    System.out.println("Market prices updated!");
                    break;

                case 6:
                    running = false;
                    System.out.println("Exiting simulator.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
