package com.example.demo.dao;

import com.example.demo.dto.AddressDto;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.Map;

@Repository
public class PackageBookingDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public int createPackageBooking(int userId, PackageBooking packageBooking, int cost) {
        String sql = "INSERT INTO Package_Booking(" +
                "booking_date,start_date,status,number_of_people,total_cost,customer_id,package_id) " +
                "VALUES(?,?,?,?,?,?,?)";

        System.out.println("createPackageBooking called");
        System.out.println("SQL: " + sql);
        System.out.println("userId=" + userId + " cost=" + cost);
        System.out.println("packageBooking == null? " + (packageBooking == null));
        if (packageBooking != null) {
            System.out.println("package_id=" + packageBooking.getPackage_id()
                    + " number_of_people=" + packageBooking.getNumber_of_people()
                    + " start_date=" + packageBooking.getStart_date()
                    + " booking_date=" + packageBooking.getBooking_date());
        }

        if (jdbcTemplate == null) {
            System.err.println("jdbcTemplate is null");
            throw new IllegalStateException("jdbcTemplate is null");
        }
        if (packageBooking == null) {
            throw new IllegalArgumentException("packageBooking cannot be null");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                // 1) booking_date -> current timestamp
                Timestamp bookingTs = Timestamp.from(Instant.now());
                ps.setTimestamp(1, bookingTs);

                // 2) start_date -> java.sql.Date (must not be null)
                if (packageBooking.getStart_date() == null) {
                    System.err.println("start_date is null");
                    throw new IllegalArgumentException("start_date cannot be null");
                }
                java.sql.Date startSqlDate = (packageBooking.getStart_date() instanceof java.sql.Date)
                        ? (java.sql.Date) packageBooking.getStart_date()
                        : new java.sql.Date(packageBooking.getStart_date().getTime());
                ps.setDate(2, startSqlDate);

                // 3) status
                ps.setString(3, "PENDING");

                // 4) number_of_people
                ps.setInt(4, packageBooking.getNumber_of_people());

                // 5) total_cost
                ps.setInt(5, cost);

                // 6) customer_id
                ps.setInt(6, userId);

                // 7) package_id
                if (packageBooking.getPackage_id() == null) {
                    System.err.println("package_id is null");
                    throw new IllegalArgumentException("package_id cannot be null");
                }
                ps.setInt(7, packageBooking.getPackage_id());

                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            System.out.println("Inserted, generated key: " + key);
            return key == null ? -1 : key.intValue();

        } catch (Exception e) {
            // full diagnostic print
            e.printStackTrace();
            Throwable root = e;
            while (root.getCause() != null) root = root.getCause();
            System.err.println("Root cause: " + root.getClass() + " : " + root.getMessage());
            if (root instanceof SQLException) {
                SQLException sq = (SQLException) root;
                System.err.println("SQLState: " + sq.getSQLState() + " ErrorCode: " + sq.getErrorCode());
            }
            throw e;
        }
    }


//    public int createPackageBooking(int userId, PackageBooking packageBooking, int cost) {
//        String sql = "INSERT INTO Package_Booking (booking_date,start_date,status,number_of_people,total_cost,customer_id,package_id) VALUES(?,?,?,?,?,?,?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//
//            // convert booking_date to Timestamp
//            Timestamp bookingTs = new Timestamp(System.currentTimeMillis());
//
//            //System.out.println(bookingTs);
//            ps.setTimestamp(1, bookingTs);
//
//            Date startSqlDate = packageBooking.getStart_date();
//
//            //System.out.println(startSqlDate);
//            ps.setDate(2, startSqlDate);
//
//            ps.setString(3, "PENDING");
//
//            ps.setInt(4, packageBooking.getNumber_of_people());
//
//            ps.setInt(5, cost);
//
//            ps.setInt(6, userId);
//
//            ps.setInt(7, packageBooking.getPackage_id());
//
//            return ps;
//        }, keyHolder);
//
//        return keyHolder.getKey().intValue();
//    }



    public void assignCustomerToPackage(int userId, int packageBookingId) {
        String sql="INSERT INTO Package_Customer(customer_id,package_booking_id) VALUES(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setInt(2, packageBookingId);
            return ps;
        }, keyHolder);
    }

    public int createTraveller(Traveller theTraveller) {
        String sql="INSERT INTO Traveller(first_name,last_name,phone,email,emergency_contact_first_name,emergency_contact_last_name,emergency_contact_no,id_proof_type,id_proof_number) VALUES(?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, theTraveller.getFirst_name());
            ps.setString(2, theTraveller.getLast_name());
            ps.setString(3, theTraveller.getPhone() );
            ps.setString(4, theTraveller.getEmail());
            ps.setString(5, theTraveller.getEmergency_contact_first_name());
            ps.setString(6, theTraveller.getEmergency_contact_last_name());
            ps.setString(7,theTraveller.getEmergency_contact_no());
            ps.setString(8,  theTraveller.getId_proof_type());
            ps.setString(9,theTraveller.getId_proof_number());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void assignTravellerToPackageBooking(int travellerId, int packageBookingId) {
        String sql="INSERT INTO Package_Traveler(traveler_id,package_booking_id) VALUES(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, travellerId);
            ps.setInt(2, packageBookingId);
            return ps;
        }, keyHolder);
    }

    public PackageBooking getPackageBookingById(int packageBookingId) {
        String sql="SELECT * FROM Package_Booking WHERE booking_id=?";
        PackageBooking packageBooking = jdbcTemplate.queryForObject(sql,new PackageBookingRowMapper(),packageBookingId);
        String sql2 = "SELECT name FROM Tour_Package WHERE package_id=?";
        String result = jdbcTemplate.queryForObject(sql2, String.class, packageBooking.getPackage_id());
        packageBooking.setPackage_name(result);
        System.out.println(packageBooking);
        return packageBooking;
    }

    public Booking getBookingForPackageBooking(Integer packageBookingId){
        String sql = "SELECT * FROM Booking WHERE package_booking_id=?";
        return jdbcTemplate.queryForObject(sql,new PackageBookingDao.BookingRowMapper(),packageBookingId);
    }

    public Integer updatePackageBookingStatus(String status,Integer booking_id) {
        String sql= "UPDATE Package_Booking SET status=? WHERE booking_id=?";
        return jdbcTemplate.update(sql, status, booking_id);
    }

    public Integer updateBookingStatus(String status,Integer booking_id) {
        String sql= "UPDATE Booking SET booking_status=? WHERE booking_id=?";
        return jdbcTemplate.update(sql, status, booking_id);
    }

    public void assignGuidesToPackageBooking(int packageId, int itemId, int guideId, int packageBookingId, int guideCost, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String sql="INSERT INTO Guide_Assignment(assignment_date,duration,start_date,end_date,cost,status,package_booking_id,guide_id,item_id,package_id) VALUES(?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Timestamp endTime = Timestamp.valueOf(endDateTime);
        Timestamp startTime= Timestamp.valueOf(startDateTime);
        long minutesLong = Duration.between(startDateTime, endDateTime).toMinutes();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1,now );
            ps.setInt(2,(int) minutesLong);
            ps.setTimestamp(3, startTime );
            ps.setTimestamp(4,endTime);
            ps.setInt(5, guideCost);
            ps.setString(6, "ASSIGNED");
            ps.setInt(7,  packageBookingId);
            ps.setInt(8,guideId);
            ps.setInt(9,itemId);
            ps.setInt(10,packageId);
            return ps;
        }, keyHolder);
    }


    public void assignHotelBookingToPackageBooking(int packageBookingId, int hotelBookingId, int packageId, int itemId) {
        String sql="INSERT INTO Hotel_Assignment(created_at,package_booking_id,hotel_booking_id,package_id,item_id) VALUES(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1,now);

            ps.setInt(2, packageBookingId);
            ps.setInt(3, hotelBookingId);

            ps.setInt(4, packageId);
            ps.setInt(5, itemId);

            return ps;
        }, keyHolder);
    }

    public void assignTransportToPackageBooking(int packageId, int itemId, int driverId, int packageBookingId, int transportCost, LocalDateTime startDateTime, AddressDto pickup, AddressDto drop) {
        String sql="INSERT INTO Transport_Assignment(pickup_street,pickup_city,pickup_state,pickup_pin,drop_street,drop_city,drop_state,drop_pin,est_time,start_date,end_date,total_distance,cost,status,package_booking_id,item_id,package_id,driver_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Timestamp endTime = Timestamp.valueOf(startDateTime);
        Timestamp startTime= Timestamp.valueOf(startDateTime);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pickup.getStreet());
            ps.setString(2, pickup.getCity());
            ps.setString(3, pickup.getState());
            ps.setString(4, pickup.getPin());

            ps.setString(5, drop.getStreet());
            ps.setString(6, drop.getCity());
            ps.setString(7, drop.getState());
            ps.setString(8, drop.getPin());

            ps.setString(9, "");
            ps.setTimestamp(10, startTime);
            ps.setTimestamp(11, endTime);
            ps.setInt(12, 0);
            ps.setInt(13, transportCost);
            ps.setString(14, "ASSIGNED"); // default status

            ps.setInt(15, packageBookingId);
            ps.setInt(16, itemId);
            ps.setInt(17, packageId);
            ps.setInt(18, driverId);

            return ps;
        }, keyHolder);

    }

    public int findFreeGuide(LocalDate date) {
        String sql = """
        SELECT g.guide_id
        FROM Guide g
        WHERE g.availability = 1
          AND NOT EXISTS (
            SELECT 1
            FROM Guide_Assignment ga
            WHERE ga.guide_id = g.guide_id
              AND DATE(ga.start_date) <= ?
              AND DATE(ga.end_date)   >= ?
          )
        LIMIT 1
    """;

        Timestamp ts = Timestamp.valueOf(( date).atStartOfDay());
         // java.sql.Date

        try {
            Integer guideId = jdbcTemplate.queryForObject(sql, new Object[]{ts, ts}, Integer.class);
            return guideId == null ? 0 : guideId;
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    public int findFreeDriver(LocalDate date) {
        String sql = """
        SELECT d.driver_id
        FROM Transport d
        WHERE d.availability = 1
          AND NOT EXISTS (
            SELECT 1
            FROM Transport_Assignment ta
            WHERE ta.driver_id = d.driver_id
              AND DATE(ta.start_date) <= ?
              AND DATE(ta.end_date)   >= ?
          )
        LIMIT 1
    """;

        Timestamp ts = Timestamp.valueOf(( date).atStartOfDay());

        try {
            Integer driverId = jdbcTemplate.queryForObject(sql, new Object[]{ts, ts}, Integer.class);
            return driverId == null ? 0 : driverId;
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    // Add these methods to PackageBookingDao.java

    public void updatePackageBookingStatus(Integer bookingId, String status) {
        String sql = "UPDATE Package_Booking SET status = ? WHERE booking_id = ?";
        jdbcTemplate.update(sql, status, bookingId);
    }

    public List<PackageBooking> getAllPackageBookingsByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM Package_Booking WHERE customer_id = ? ORDER BY booking_date DESC";
        return jdbcTemplate.query(sql, new PackageBookingRowMapper(), customerId);
    }

    public List<PackageBooking> getPackageBookingsByCustomerIdAndStatus(Integer customerId, String status) {
        String sql = "SELECT * FROM Package_Booking WHERE customer_id = ? AND status = ? ORDER BY booking_date DESC";
        return jdbcTemplate.query(sql, new PackageBookingRowMapper(), customerId, status);
    }

    public void changePackageBookingStatus(String newStatus, Integer packageBookingId) {
        String sql="UPDATE Package_Booking SET status=? WHERE booking_id=?";
        jdbcTemplate.update(sql,newStatus,packageBookingId);
    }

<<<<<<< Updated upstream
    /**
     * Fetches a specific GuideAssignment by its composite key.
     * @return A GuideAssignment object, or null if not found.
     */
    public GuideAssignment getGuideAssignment(Integer packageBookingId, Integer packageId, Integer itemId) {
        String sql = "SELECT * FROM Guide_Assignment WHERE package_booking_id = ? AND package_id = ? AND item_id = ?";
        try {
            // FIXED: Added the GuideAssignmentRowMapper as the second argument
            return jdbcTemplate.queryForObject(sql, new GuideAssignmentRowMapper(), packageBookingId, packageId, itemId);
        } catch (EmptyResultDataAccessException e) {
            // This is expected if no assignment is found.
            return null;
        }
    }

    /**
     * Fetches a specific GuideAssignment by its composite key.
     * @return A GuideAssignment object, or null if not found.
     */
    public TransportAssignment getTransportAssignment(Integer packageBookingId, Integer packageId, Integer itemId) {
        String sql = "SELECT * FROM Transport_Assignment WHERE package_booking_id = ? AND package_id = ? AND item_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new TransportAssignmentRowMapper(), packageBookingId, packageId, itemId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Fetches a specific HotelAssignment by its composite key.
     * @return A HotelAssignment object, or null if not found.
     */
    public HotelAssignment getHotelAssignment(Integer packageBookingId, Integer packageId, Integer itemId) {
        String sql = "SELECT * FROM Hotel_Assignment WHERE package_booking_id = ? AND package_id = ? AND item_id = ?";
        try {
            // FIXED: Now uses the correct HotelAssignmentRowMapper
            return jdbcTemplate.queryForObject(sql, new HotelAssignmentRowMapper(), packageBookingId, packageId, itemId);
        } catch (EmptyResultDataAccessException e) {
            // Expected if no assignment is found
            return null;
        }
    }

=======
    // Add these methods to PackageBookingDao.java

    public List<Map<String, Object>> getGuideAssignmentsByBookingId(Integer packageBookingId) {
        String sql = """
        SELECT ga.*, g.first_name, g.last_name, g.primary_phone, g.primary_email,
               i.title as activity_title, i.day_number, i.start_time, i.end_time,
               i.street_name, i.city, i.state
        FROM Guide_Assignment ga
        JOIN Guide g ON ga.guide_id = g.guide_id
        JOIN Itinerary_Item i ON ga.item_id = i.item_id AND ga.package_id = i.package_id
        WHERE ga.package_booking_id = ?
        ORDER BY i.day_number, i.start_time
    """;

        return jdbcTemplate.queryForList(sql, packageBookingId);
    }

    public List<Map<String, Object>> getTransportAssignmentsByBookingId(Integer packageBookingId) {
        String sql = """
        SELECT ta.*, t.first_name, t.last_name, t.vehicle_model, t.vehicle_type,
               t.vehicle_reg_no, t.primary_phone, t.vehicle_seating_capacity,
               i.title as activity_title, i.day_number
        FROM Transport_Assignment ta
        JOIN Transport t ON ta.driver_id = t.driver_id
        JOIN Itinerary_Item i ON ta.item_id = i.item_id AND ta.package_id = i.package_id
        WHERE ta.package_booking_id = ?
        ORDER BY ta.start_date
    """;

        return jdbcTemplate.queryForList(sql, packageBookingId);
    }

    public List<Map<String, Object>> getHotelAssignmentsByBookingId(Integer packageBookingId) {
        String sql = """
        SELECT ha.*, hb.check_in_date, hb.check_out_date, hb.no_of_rooms, 
               hb.room_type, hb.guest_count, hb.cost as hotel_cost,
               h.name as hotel_name, h.street, h.city, h.state, h.pin,
               h.primary_phone as hotel_phone, h.rating,
               i.title as activity_title, i.day_number
        FROM Hotel_Assignment ha
        JOIN Hotel_Booking hb ON ha.hotel_booking_id = hb.booking_id
        JOIN Hotel h ON hb.hotel_id = h.hotel_id
        LEFT JOIN Itinerary_Item i ON ha.item_id = i.item_id AND ha.package_id = i.package_id
        WHERE ha.package_booking_id = ?
        ORDER BY hb.check_in_date
    """;

        return jdbcTemplate.queryForList(sql, packageBookingId);
    }

    public List<Map<String, Object>> getTravellersByBookingId(Integer packageBookingId) {
        String sql = """
        SELECT t.*
        FROM Traveller t
        JOIN Package_Traveler pt ON t.traveller_id = pt.traveler_id
        WHERE pt.package_booking_id = ?
    """;

        return jdbcTemplate.queryForList(sql, packageBookingId);
    }
>>>>>>> Stashed changes

    private static class PackageBookingRowMapper implements RowMapper<PackageBooking> {
        @Override
        public PackageBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
            PackageBooking booking = new PackageBooking();
            booking.setBooking_id(rs.getInt("booking_id"));
            booking.setStart_date(rs.getDate("start_date")); // or setCheckInDate(checkInSql) if your model uses java.sql.Date
            booking.setBooking_date(rs.getTimestamp("booking_date"));
            booking.setNumber_of_people(rs.getInt("number_of_people"));
            booking.setStatus(rs.getString("status"));
            booking.setTotal_cost(rs.getInt("total_cost"));
            booking.setPackage_id(rs.getInt("package_id"));
            booking.setCustomer_id(rs.getInt("customer_id"));
            return booking;
        }
    }

    /**
     * A RowMapper to map a row from the Booking table to a Booking model object.
     */
    private static class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();

            booking.setBooking_id(rs.getInt("booking_id"));
            booking.setBooking_type(rs.getString("booking_type"));
            booking.setCreated_at(rs.getTimestamp("created_at"));
            booking.setBooking_status(rs.getString("booking_status"));

            // Use getObject for nullable integer columns to correctly handle NULLs.
            booking.setPackage_booking_id(rs.getObject("package_booking_id", Integer.class));
            booking.setHotel_booking_id(rs.getObject("hotel_booking_id", Integer.class));
            booking.setPayment_id(rs.getObject("payment_id", Integer.class));

            return booking;
        }
    }

    /**
     * A private RowMapper class to map a row from the Guide_Assignment table
     * to a GuideAssignment Java object.
     */
    private static class GuideAssignmentRowMapper implements RowMapper<GuideAssignment> {
        @Override
        public GuideAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            GuideAssignment assignment = new GuideAssignment();

            assignment.setAssignment_id(rs.getInt("assignment_id"));
            assignment.setAssignment_date(rs.getTimestamp("assignment_date"));
            assignment.setDuration(rs.getInt("duration"));
            assignment.setStart_date(rs.getTimestamp("start_date"));
            assignment.setStart_time(rs.getObject("start_time", LocalTime.class));
            assignment.setEnd_date(rs.getTimestamp("end_date"));
            assignment.setEnd_time(rs.getObject("end_time", LocalTime.class));
            assignment.setCost(rs.getInt("cost"));
            assignment.setStatus(rs.getString("status"));
            assignment.setPackage_booking_id(rs.getInt("package_booking_id"));
            assignment.setGuide_id(rs.getInt("guide_id"));
            assignment.setItem_id(rs.getInt("item_id"));
            assignment.setPackage_id(rs.getInt("package_id"));

            return assignment;
        }
    }

    public class TransportAssignmentRowMapper implements RowMapper<TransportAssignment> {
        @Override
        public TransportAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            TransportAssignment assignment = new TransportAssignment();

            assignment.setAssignment_id(rs.getInt("assignment_id"));
            assignment.setPickup_street(rs.getString("pickup_street"));
            assignment.setPickup_city(rs.getString("pickup_city"));
            assignment.setPickup_state(rs.getString("pickup_state"));
            assignment.setPickup_pin(rs.getString("pickup_pin"));
            assignment.setDrop_street(rs.getString("drop_street"));
            assignment.setDrop_city(rs.getString("drop_city"));
            assignment.setDrop_state(rs.getString("drop_state"));
            assignment.setDrop_pin(rs.getString("drop_pin"));
            assignment.setEst_time(rs.getString("est_time"));

            // Map DATE and TIME columns correctly
            assignment.setStart_date(rs.getObject("start_date", LocalDate.class));
            assignment.setEnd_date(rs.getObject("end_date", LocalDate.class));

            // Map DECIMAL columns to BigDecimal
            assignment.setTotal_distance(rs.getInt("total_distance"));
            assignment.setCost(rs.getInt("cost"));

            assignment.setStatus(rs.getString("status"));

            // Map nullable foreign keys safely
            assignment.setPackage_booking_id(rs.getObject("package_booking_id", Integer.class));
            assignment.setItem_id(rs.getObject("item_id", Integer.class));
            assignment.setPackage_id(rs.getObject("package_id", Integer.class));
            assignment.setDriver_id(rs.getObject("driver_id", Integer.class));

            return assignment;
        }
    }



    /**
     * A private RowMapper class to map a row from the Hotel_Assignment table
     * to a HotelAssignment Java object.
     */
    private static class HotelAssignmentRowMapper implements RowMapper<HotelAssignment> {
        @Override
        public HotelAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            HotelAssignment assignment = new HotelAssignment();

            assignment.setHotel_assignment_id(rs.getInt("hotel_assignment_id"));
            assignment.setCreated_at(rs.getTimestamp("created_at"));

            // Use getObject for nullable foreign keys to safely handle NULLs
            assignment.setPackage_booking_id(rs.getObject("package_booking_id", Integer.class));
            assignment.setHotel_booking_id(rs.getObject("hotel_booking_id", Integer.class));
            assignment.setPackage_id(rs.getObject("package_id", Integer.class));
            assignment.setItem_id(rs.getObject("item_id", Integer.class));

            return assignment;
        }
    }



}
