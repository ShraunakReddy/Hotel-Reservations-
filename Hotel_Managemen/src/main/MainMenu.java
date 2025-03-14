package main;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.ReservationService;
import service.CustomerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) {
        startMainMenu();
    }

    public static void startMainMenu() {
        while (true) {
            try {
                System.out.println("\n==================================");
                System.out.println("\uD83C\uDFE8 Welcome to the Hotel Management System");
                System.out.println("==================================");
                System.out.println("1. Find and reserve a room");
                System.out.println("2. See my reservations");
                System.out.println("3. Create an account");
                System.out.println("4. View all rooms");
                System.out.println("5. Admin options");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> findAndReserveRoom();
                    case 2 -> seeReservations();
                    case 3 -> createAccount();
                    case 4 -> viewAllRooms();
                    case 5 -> startAdminMenu();
                    case 6 -> {
                        System.out.println("Exiting... Thank you for using the Hotel Management System!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Please enter a number between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
            }
        }
    }

    private static void findAndReserveRoom() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            Customer customer = hotelResource.getCustomer(email);
            if (customer == null) {
                System.out.println("❌ No account found with this email. Please create an account first.");
                return;
            }

            System.out.print("Enter check-in date (dd-MM-yyyy): ");
            Date checkIn = parseDate(scanner.nextLine());

            System.out.print("Enter check-out date (dd-MM-yyyy): ");
            Date checkOut = parseDate(scanner.nextLine());

            if (!checkIn.before(checkOut)) {
                System.out.println("❌ Check-in date must be before the check-out date.");
                return;
            }

            Collection<IRoom> availableRooms = ReservationService.getInstance().findAvailableRooms(checkIn, checkOut);


            if (availableRooms.isEmpty()) {
                System.out.println("❌ No available rooms for the selected dates.");
                return;
            }

            System.out.println("\uD83C\uDFE8 Available rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room);
            }

            System.out.print("Enter room number to book: ");
            String roomNumber = scanner.nextLine().trim();
            IRoom room = hotelResource.getRoomByNumber(roomNumber);


            if (room == null) {
                System.out.println("❌ Room not found.");
                return;
            }

            hotelResource.bookRoom(email, room, checkIn, checkOut);
            System.out.println("✅ Room booked successfully!");

        } catch (ParseException e) {
            System.out.println("❌ Invalid date format. Please use dd-MM-yyyy.");
        }
    }

    private static void seeReservations() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        Customer customer = hotelResource.getCustomer(email);
        if (customer == null) {
            System.out.println("❌ No account found with this email.");
            return;
        }

        Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(System.out::println);
        }
    }

    private static void createAccount() {
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("❌ Error: Invalid email format! Please enter a valid email.");
            return;
        }

        hotelResource.createCustomer(firstName, lastName, email);
        System.out.println("✅ Account successfully created!");
    }

    private static void viewAllRooms() {
        Collection<IRoom> rooms = hotelResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.parse(dateString);
    }

    private static void startAdminMenu() {
        while (true) {
            try {
                System.out.println("\n========== Admin Menu ==========");
                System.out.println("1. Add a room");
                System.out.println("2. View all customers");
                System.out.println("3. View all reservations");
                System.out.println("4. Back to Main Menu");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> addRoom();
                    case 2 -> viewAllCustomers();
                    case 3 -> viewAllReservations();
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
            }
        }
    }

    private static void addRoom() {
        try {
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine().trim();

            System.out.print("Enter price per night: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter room type (SINGLE/DOUBLE): ");
            String roomType = scanner.nextLine().trim().toUpperCase();

            if (!roomType.equals("SINGLE") && !roomType.equals("DOUBLE")) {
                System.out.println("❌ Invalid room type. Please enter SINGLE or DOUBLE.");
                return;
            }

            IRoom room = hotelResource.createRoom(roomNumber, price, roomType);
            System.out.println("✅ Room added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price. Please enter a valid number.");
        }
    }

    private static void viewAllCustomers() {
        Collection<Customer> customers = hotelResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void viewAllReservations() {
        hotelResource.printAllReservations();
    }
}
