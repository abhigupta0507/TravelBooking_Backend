package com.example.demo.dao;

import com.example.demo.model.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class TransportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ 1. Get all transports (all vendors)
    public List<Transport> getAllTransports() {
        String sql = "SELECT * FROM Transport";
        return jdbcTemplate.query(sql, new TransportRowMapper());
    }

    // ✅ 2. Get all transports of a vendor
    public List<Transport> getTransportsByVendor(Integer vendorId) {
        String sql = "SELECT * FROM Transport WHERE vendor_id = ?";
        return jdbcTemplate.query(sql, new TransportRowMapper(), vendorId);
    }


    public Transport getTransportByVendorAndId(Integer vendorId, Integer driverId) {
        String sql = "SELECT * FROM Transport WHERE vendor_id = ? AND driver_id = ?";
        return jdbcTemplate.queryForObject(sql, new TransportRowMapper(), vendorId, driverId);
    }

    public Transport getTransportById(Integer driverId){
        String sql = "SELECT * FROM Transport WHERE driver_id = ?";
        return jdbcTemplate.queryForObject(sql, new TransportRowMapper(),driverId);
    }

    // ✅ 4. Create a new transport
    public Integer createTransport(Transport transport, Integer vendorId) {
        String sql = "INSERT INTO Transport " +
                "(first_name, last_name, license_no, vehicle_model, vehicle_type, vehicle_reg_no, " +
                "vehicle_ac, vehicle_seating_capacity, cost, availability, vendor_id,primary_phone) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, transport.getFirst_name());
            ps.setString(2, transport.getLast_name());
            ps.setString(3, transport.getLicense_no());
            ps.setString(4, transport.getVehicle_model());
            ps.setString(5, transport.getVehicle_type());
            ps.setString(6, transport.getVehicle_reg_no());
            ps.setBoolean(7, transport.getVehicle_ac() != null ? transport.getVehicle_ac() : false);
            ps.setObject(8, transport.getVehicle_seating_capacity(), Types.INTEGER);
            ps.setBigDecimal(9, transport.getCost());
            ps.setBoolean(10, transport.getAvailability() != null ? transport.getAvailability() : true);
            ps.setInt(11, vendorId);
            ps.setString(12,transport.getPrimary_phone());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }


    public int updateTransport(Integer vendorId, Integer driverId, Transport transport) {
        String sql = "UPDATE Transport SET first_name=?, last_name=?, license_no=?, vehicle_model=?, vehicle_type=?, " +
                "vehicle_reg_no=?, vehicle_ac=?, vehicle_seating_capacity=?, cost=?, availability=?, primary_phone=? " +
                "WHERE vendor_id=? AND driver_id=?";

        return jdbcTemplate.update(sql,
                transport.getFirst_name(),
                transport.getLast_name(),
                transport.getLicense_no(),
                transport.getVehicle_model(),
                transport.getVehicle_type(),
                transport.getVehicle_reg_no(),
                transport.getVehicle_ac(),
                transport.getVehicle_seating_capacity(),
                transport.getCost(),
                transport.getAvailability(),
                transport.getPrimary_phone(),
                vendorId,
                driverId
        );
    }


    public int deleteTransport(Integer vendorId, Integer driverId) {
        String sql = "DELETE FROM Transport WHERE vendor_id=? AND driver_id=?";
        return jdbcTemplate.update(sql, vendorId, driverId);
    }

    // ✅ RowMapper for Transport
    private static class TransportRowMapper implements RowMapper<Transport> {
        @Override
        public Transport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transport transport = new Transport();
            transport.setDriver_id(rs.getInt("driver_id"));
            transport.setFirst_name(rs.getString("first_name"));
            transport.setLast_name(rs.getString("last_name"));
            transport.setLicense_no(rs.getString("license_no"));
            transport.setVehicle_model(rs.getString("vehicle_model"));
            transport.setVehicle_type(rs.getString("vehicle_type"));
            transport.setVehicle_reg_no(rs.getString("vehicle_reg_no"));
            transport.setVehicle_ac(rs.getBoolean("vehicle_ac"));
            transport.setVehicle_seating_capacity(rs.getObject("vehicle_seating_capacity", Integer.class));
            transport.setCost(rs.getBigDecimal("cost"));
            transport.setAvailability(rs.getBoolean("availability"));
            transport.setPrimary_phone(rs.getString("primary_phone"));
            transport.setVendor_id(rs.getInt("vendor_id"));
            return transport;
        }
    }
}
