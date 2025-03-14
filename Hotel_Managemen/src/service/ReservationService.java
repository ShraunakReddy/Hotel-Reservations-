package service;

import model.Customer;
import model.Reservation;
import model.IRoom;
import model.Room;
import model.RoomType;
import java.util.*;

public class ReservationService {
    private static final ReservationService instance = new ReservationService();
    private final Set<Reservation> reservations = new HashSet<>();
    private final Map<String, IRoom> rooms = new HashMap<>();

    private ReservationService() {
        initializeRooms();
    }

    public static ReservationService getInstance() {
        return instance;
    }

    // ✅ Initialize rooms
    private void initializeRooms() {
        rooms.put("101", new Room("101", 100.0, RoomType.SINGLE));
        rooms.put("102", new Room("102", 150.0, RoomType.DOUBLE));
        rooms.put("103", new Room("103", 200.0, RoomType.TRIPLE));
    }

    // ✅ Fix: Retrieve a room directly
    public IRoom getRoom(String roomNumber) {
        return rooms.get(roomNumber);  // ✅ Corrected
    }

    // ✅ Fix: Add a room
    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            System.out.println("❌ Room already exists.");
        } else {
            rooms.put(room.getRoomNumber(), room);
        }
    }

    // ✅ Fix: Get all rooms
    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    // ✅ Fix: Find available rooms
    public Collection<IRoom> findAvailableRooms(Date checkIn, Date checkOut) {
        Set<String> bookedRoomNumbers = new HashSet<>();
        for (Reservation reservation : reservations) {
            if (reservationOverlaps(reservation, checkIn, checkOut)) {
                bookedRoomNumbers.add(reservation.getRoom().getRoomNumber());
            }
        }

        List<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : rooms.values()) {  
            if (!bookedRoomNumbers.contains(room.getRoomNumber())) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    // ✅ Fix: Check for reservation overlaps
    private boolean reservationOverlaps(Reservation reservation, Date checkIn, Date checkOut) {
        return !(checkOut.before(reservation.getCheckInDate()) || checkIn.after(reservation.getCheckOutDate()));
    }
    public Collection<Reservation> getCustomerReservations(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    // ✅ Fix: Reserve a room
    public Reservation reserveRoom(Customer customer, IRoom room, Date checkIn, Date checkOut) {
        if (customer == null || room == null) {
            throw new IllegalArgumentException("Customer and room cannot be null.");
        }
        if (!findAvailableRooms(checkIn, checkOut).contains(room)) {
            System.out.println("❌ Room is already booked for the selected dates.");
            return null;
        }
        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(reservation);
        return reservation;
    }

    // ✅ Fix: Print all reservations
    public void printAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }
}
