package api;

import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import model.Reservation;
import service.ReservationService;
import model.Customer;
import java.util.Date;

public class HotelResource {
    private static final HotelResource instance = new HotelResource();
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();


    private HotelResource() { }

    public static HotelResource getInstance() {
        return instance;
    }

    // Get customer by email
    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    // Create a new customer
    public void createCustomer(String firstName, String lastName, String email) {
        customerService.addCustomer(firstName, lastName, email);
    }

    // ✅ Fixed: Now returns an `IRoom`
    public IRoom createRoom(String roomNumber, double price, String roomType) {
        RoomType type;
        try {
            type = RoomType.valueOf(roomType.toUpperCase()); // Ensure correct enum format
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: Invalid room type. Use SINGLE or DOUBLE.");
            return null;
        }

        IRoom room = new Room(roomNumber, price, type);
        reservationService.addRoom(room);

        // ✅ Print room details after adding
        System.out.println(room);
        return room;
    }

    // ✅ Fixed: Method for booking a room
    public void bookRoom(String email, IRoom room, Date checkIn, Date checkOut) {
        Customer customer = getCustomer(email);
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return;
        }
        reservationService.reserveRoom(customer, room, checkIn, checkOut);
    }
    public IRoom getRoomByNumber(String roomNumber) {
        return getRoom(roomNumber);  // ✅ Calls the existing method
    }

    public Collection<Reservation> getCustomerReservations(String email) {
        Customer customer = customerService.getCustomer(email);
        if (customer == null) {
            return Collections.emptyList();  // ✅ Return an empty list if customer is not found
        }
        return reservationService.getCustomerReservations(customer);
    }



    // ✅ Fixed: Method to retrieve all customers
    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // ✅ Fixed: Method to print all reservations
    public void printAllReservations() {
        reservationService.printAllReservations();
    }

    // ✅ Fixed: Method to get a room by room number
    public IRoom getRoom(String roomNumber) {
        return reservationService.getRoom(roomNumber);  // ✅ Calls the method in ReservationService
    }


    // ✅ Fixed: Method to get all available rooms
    public Collection<IRoom> findAvailableRooms(Date checkIn, Date checkOut) {
        return reservationService.findAvailableRooms(checkIn, checkOut);
    }

    // ✅ Fixed: Method to get all rooms
    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }
}
