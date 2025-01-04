import java.util.*;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;
    private double pricePerDay;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
        this.pricePerDay = basePricePerDay;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    // Dynamic pricing based on time (example: higher price during weekends)
    public void updatePrice(String rentalDay) {
        if (rentalDay.equals("Saturday") || rentalDay.equals("Sunday")) {
            this.pricePerDay = basePricePerDay * 1.2; // 20% more during weekends
        } else {
            this.pricePerDay = basePricePerDay;
        }
    }

    public double calculatePrice(int rentalDays) {
        return pricePerDay * rentalDays;
    }
}

class Customer {
    private String customerId;
    private String name;
    private int loyaltyPoints;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.loyaltyPoints = 0;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        loyaltyPoints += points;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;
    private double totalPrice;

    public Rental(Car car, Customer customer, int days, double totalPrice) {
        this.car = car;
        this.customer = customer;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(String carId, String customerId, int days, String rentalDay) {
        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId().equals(carId) && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar == null) {
            System.out.println("Car is not available or invalid car ID.");
            return;
        }

        Customer customer = null;
        for (Customer cust : customers) {
            if (cust.getCustomerId().equals(customerId)) {
                customer = cust;
                break;
            }
        }

        if (customer == null) {
            System.out.println("Invalid customer ID.");
            return;
        }

        selectedCar.updatePrice(rentalDay); // Dynamic pricing
        double totalPrice = selectedCar.calculatePrice(days);
        selectedCar.rent();
        rentals.add(new Rental(selectedCar, customer, days, totalPrice));

        // Add loyalty points
        customer.addLoyaltyPoints(days);

        System.out.printf("Car rented successfully! Total price: $%.2f%n", totalPrice);
        System.out.println("Loyalty Points: " + customer.getLoyaltyPoints());
    }

    public void returnCar(String carId) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equals(carId)) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove == null) {
            System.out.println("Car is not rented or invalid car ID.");
            return;
        }

        rentalToRemove.getCar().returnCar();
        rentals.remove(rentalToRemove);
        System.out.println("Car returned successfully.");
    }

    public void viewAvailableCars(String brand, double maxPrice) {
        System.out.println("\nAvailable Cars (Filtered):");
        for (Car car : cars) {
            if (car.isAvailable() && car.getBrand().equalsIgnoreCase(brand) && car.getBasePricePerDay() <= maxPrice) {
                System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel() +
                        " ($" + car.calculatePrice(1) + "/day)");
            }
        }
    }

    public void viewRentalHistory() {
        System.out.println("\nRental History:");
        for (Rental rental : rentals) {
            System.out.println("Customer: " + rental.getCustomer().getName() +
                    " | Car: " + rental.getCar().getBrand() + " " + rental.getCar().getModel() +
                    " | Days: " + rental.getDays() +
                    " | Total Price: $" + rental.getTotalPrice());
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. View Rental History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter car brand to filter: ");
                    String brand = scanner.nextLine();
                    System.out.print("Enter max price per day: ");
                    double maxPrice = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    viewAvailableCars(brand, maxPrice);
                    break;
                case 2:
                    System.out.print("Enter your customer ID: ");
                    String customerId = scanner.nextLine();

                    System.out.print("Enter the car ID to rent: ");
                    String carId = scanner.nextLine();

                    System.out.print("Enter rental days: ");
                    int days = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter the rental day (e.g., Monday, Saturday): ");
                    String rentalDay = scanner.nextLine();

                    rentCar(carId, customerId, days, rentalDay);
                    break;
                case 3:
                    System.out.print("Enter the car ID to return: ");
                    String returnCarId = scanner.nextLine();
                    returnCar(returnCarId);
                    break;
                case 4:
                    viewRentalHistory();
                    break;
                case 5:
                    System.out.println("Thank you for using the Car Rental System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        // Add sample cars
        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));

        // Add sample customers
        rentalSystem.addCustomer(new Customer("CUS001", "John Doe"));
        rentalSystem.addCustomer(new Customer("CUS002", "Jane Smith"));

        rentalSystem.menu();
    }
}
