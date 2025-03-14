package main;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;
import service.ReservationService;

import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminResource adminResource = new AdminResource();

    public static void startAdminMenu() {
        while (true) {
            try {
                System.out.println("\n==================================");
                System.out.println("🔧 Admin Panel - Hotel Management");
                System.out.println("==================================");
                System.out.println("1. View all customers");
                System.out.println("2. View all rooms");
                System.out.println("3. View all reservations");
                System.out.println("4. Add a room");
                System.out.println("5. Back to Main Menu");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewAllCustomers();
                        break;
                    case 2:
                        viewAllRooms();
                        break;
                    case 3:
                        viewAllReservations();
                        break;
                    case 4:
                        addRoom();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("❌ Invalid choice! Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
            }
        }
    }

    private static void viewAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void viewAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private static void viewAllReservations() {
        ReservationService.getInstance().printAllReservations();
    }

    private static void addRoom() {
        while (true) {
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine();

            System.out.print("Enter price per night: ");
            double price;
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid price! Please enter a valid number.");
                continue;
            }

            System.out.print("Enter room type (1 for SINGLE, 2 for DOUBLE): ");
            int roomTypeChoice = Integer.parseInt(scanner.nextLine());

            RoomType roomType;
            if (roomTypeChoice == 1) {
                roomType = RoomType.SINGLE;
            } else if (roomTypeChoice == 2) {
                roomType = RoomType.DOUBLE;
            } else {
                System.out.println("❌ Invalid room type! Please enter 1 or 2.");
                continue;
            }

            IRoom room = new Room(roomNumber, price, roomType);
            adminResource.addRoom(room);
            System.out.println("✅ Room added successfully!");

            System.out.print("Would you like to add another room? (Y/N): ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("Y")) {
                break;
            }
        }
    }
}
