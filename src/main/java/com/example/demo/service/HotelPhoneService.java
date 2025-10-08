package com.example.demo.service;

import com.example.demo.dao.HotelPhoneDAO;
import com.example.demo.model.HotelPhone;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelPhoneService {

    private final HotelPhoneDAO hotelPhoneDAO;

    public HotelPhoneService(HotelPhoneDAO hotelPhoneDAO) {
        this.hotelPhoneDAO = hotelPhoneDAO;
    }

    public List<HotelPhone> getPhonesByHotelId(Integer hotelId) {
        return hotelPhoneDAO.getPhoneNumbersByHotelId(hotelId);
    }

    public void addPhones(Integer hotelId, List<String> phoneNumbers) {
        hotelPhoneDAO.addPhoneNumbers(hotelId, phoneNumbers);
    }

    public boolean updatePhone(Integer hotelId, String oldPhone, String newPhone) {
        return hotelPhoneDAO.updatePhoneNumber(hotelId, oldPhone, newPhone) > 0;
    }

    public boolean deletePhone(Integer hotelId, String phone) {
        return hotelPhoneDAO.deletePhoneNumber(hotelId, phone) > 0;
    }

    public Integer getVendorIdByHotelId(Integer hotelId) {
        return hotelPhoneDAO.getVendorIdByHotelId(hotelId);
    }

}
