package com.example.demo.service;

import com.example.demo.dao.HotelEmailDao;
import com.example.demo.model.HotelEmail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelEmailService {

    private final HotelEmailDao hotelEmailDao;

    public HotelEmailService(HotelEmailDao hotelEmailDao) {
        this.hotelEmailDao = hotelEmailDao;
    }

    public int addHotelEmail(Integer vendorId, Integer hotelId, List<String> emails) {
        // vendorId can be used later for ownership validation if required
        int count = 0;
        for (String email : emails) {
            try {
                hotelEmailDao.addHotelEmail(hotelId, email);
                count++;
            } catch (RuntimeException ignored) {
                // Skip duplicates or invalid emails
                System.out.println(ignored.getMessage());
            }
        }
        return count;
    }

    public List<HotelEmail> getEmailsByHotel(Integer vendorId, Integer hotelId) {
        return hotelEmailDao.getEmailsByHotel(hotelId);
    }

    public int deleteHotelEmail(Integer vendorId, Integer hotelId, String email) {
        return hotelEmailDao.deleteHotelEmail(hotelId, email);
    }

    public Integer getVendorIdByHotelId(Integer hotelId) {
        return hotelEmailDao.getVendorIdByHotelId(hotelId);
    }

}
