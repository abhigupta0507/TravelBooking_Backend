package com.example.demo.daor;

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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        return jdbcTemplate.queryForObject(sql,new PackageBookingRowMapper(),packageBookingId);
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
}
