package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.dto.AddressDto;
import com.example.demo.dto.PackageAfterBookingDayDto;
import com.example.demo.dto.PackageBookingDto;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class PackageBookingService {

    @Autowired
    private com.example.demo.dao.PackageBookingDao packageBookingDao;

    @Autowired
    private PackageDAO packageDAO;

    @Autowired
    private TransportDao transportDao;

    @Autowired
    private GuideDao guideDao;

    @Autowired
    private HotelDAO hotelDao;

    @Autowired
    private HotelBookingDao hotelBookingDao;

    @Transactional
    public int createPackageBooking(int userId,PackageBooking packageBooking) {
        try{
            //find the package for which booking will be created
            TourPackage thePackage = packageDAO.findPackageById(packageBooking.getPackage_id());
            int packageId= thePackage.getPackageId();

            //calculate cost of package
            int costPerPersonForRequiredPackage = thePackage.getPrice();
            int personCount = packageBooking.getNumber_of_people();
            int cost = costPerPersonForRequiredPackage * personCount;

            //right now we are not factoring hotel cost
            //assuming that while packageCreation the creator will factor the cost;

            //create entry in packageBooking table with status "PENDING"
            int packageBookingId = packageBookingDao.createPackageBooking(userId,packageBooking,cost);

            //attach customer to package_booking
            packageBookingDao.assignCustomerToPackage(userId,packageBookingId);

            return packageBookingId;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void assignTravellersToPackageBooking(List<Traveller> travellerList, int userId, int packageBookingId){
        try{
            List<Integer> travellerIds = new ArrayList<>();;
            for(Traveller theTraveller : travellerList){
                int travellerId = packageBookingDao.createTraveller(theTraveller);
                System.out.println(travellerId);
                travellerIds.add(travellerId);

                //assign the traveller to packageBooking
                packageBookingDao.assignTravellerToPackageBooking(travellerId,packageBookingId);
            }

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private int findFreeGuideId(LocalDate date){
        try{
            return packageBookingDao.findFreeGuide(date);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private int findFreeDriver(LocalDate date){
        try{
            return packageBookingDao.findFreeDriver(date);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void assignGuidesToPackageBooking(int packageBookingId){
        try{
            PackageBooking thePackageBooking = packageBookingDao.getPackageBookingById(packageBookingId);
            int packageId = thePackageBooking.getPackage_id();
            //assignments for each itinerary item...

            //for each itinerary
            //  - find free guide and assign it

            List<ItineraryItem> itineraryItemList = packageDAO.findAllItineraryItemsByPackageId(packageId);
            int numberOfItems= itineraryItemList.size();

            System.out.println("Number of Items:: "+numberOfItems);
            Date startDate = thePackageBooking.getStart_date();
            ZoneId zone = ZoneId.systemDefault();

            LocalDate startLocalDate;
            if (startDate == null) {
                throw new IllegalArgumentException("start_date is null for booking " + packageBookingId);
            }
            if (startDate instanceof java.sql.Date) {
                startLocalDate = ((java.sql.Date) startDate).toLocalDate();
            } else {
                startLocalDate = startDate.toInstant().atZone(zone).toLocalDate();
            }

            for (ItineraryItem item : itineraryItemList) {
                int itemId = item.getItem_id();
                int dayNumber = item.getDay_number();
                LocalDate targetDate = startLocalDate.plusDays((long) dayNumber - 1);

                // convert start/end times (handle java.sql.Time or java.time.LocalTime)
                LocalTime startTime;
                LocalTime endTime;
                Object s = item.getStart_time();
                Object e = item.getEnd_time();

                if (s instanceof java.sql.Time) {
                    startTime = ((java.sql.Time) s).toLocalTime();
                } else if (s instanceof LocalTime) {
                    startTime = (LocalTime) s;
                } else {
                    throw new IllegalStateException("Unsupported start_time type: " + (s == null ? "null" : s.getClass()));
                }

                if (e instanceof java.sql.Time) {
                    endTime = ((java.sql.Time) e).toLocalTime();
                } else if (e instanceof LocalTime) {
                    endTime = (LocalTime) e;
                } else {
                    throw new IllegalStateException("Unsupported end_time type: " + (e == null ? "null" : e.getClass()));
                }

                LocalDateTime startDateTime = LocalDateTime.of(targetDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(targetDate, endTime);

                int guideId = findFreeGuideId(targetDate);
                System.out.println("Guide number : "+guideId);
                if (guideId == 0) {
                    throw new RuntimeException("Not enough guides");
                }
                Guide theGuide = guideDao.getGuideById(guideId);
                BigDecimal guideCost = theGuide.getCost_per_hour();
                int guideCostInInt = guideCost.intValue();
                packageBookingDao.assignGuidesToPackageBooking(packageId, itemId, guideId, packageBookingId, guideCostInInt, startDateTime, endDateTime);
            }

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void assignTransportToPackageBooking(int packageBookingId){
        try{
            PackageBooking thePackageBooking = packageBookingDao.getPackageBookingById(packageBookingId);
            int packageId = thePackageBooking.getPackage_id();

            List<ItineraryItem> itineraryItemList = packageDAO.findAllItineraryItemsByPackageId(packageId);
            int numberOfItems= itineraryItemList.size();

            List<IncludeRooms> hotelsWithPackage = packageDAO.findAllIncludeRoomsByPackageId(packageId);

           Date startDate = thePackageBooking.getStart_date();
//            ZoneId zone = ZoneId.systemDefault();
//            LocalDate startLocalDate = startDate.toInstant().atZone(zone).toLocalDate();
            ZoneId zone = ZoneId.systemDefault();

            LocalDate startLocalDate;
            if (startDate == null) {
                throw new IllegalArgumentException("start_date is null for booking " + packageBookingId);
            }
            if (startDate instanceof java.sql.Date) {
                startLocalDate = ((java.sql.Date) startDate).toLocalDate();
            } else {
                startLocalDate = startDate.toInstant().atZone(zone).toLocalDate();
            }
            int currentDay = 1;
            for(ItineraryItem item : itineraryItemList){
                int itemId= item.getItem_id();
                int hotelId = findHotelForDay(currentDay,hotelsWithPackage);
                if(hotelId==0){
                    throw new RuntimeException("Hotel couldn't be found for a itinerary item");
                }
                Hotel hotel = hotelDao.findHotelById(hotelId);
                int dayNumber = item.getDay_number();
                LocalDate targetDate = startLocalDate.plusDays((long)dayNumber - 1);
                // convert start/end times (handle java.sql.Time or java.time.LocalTime)
                LocalTime startTime;
                LocalTime endTime;
                Object s = item.getStart_time();
                Object e = item.getEnd_time();

                if (s instanceof java.sql.Time) {
                    startTime = ((java.sql.Time) s).toLocalTime();
                } else if (s instanceof LocalTime) {
                    startTime = (LocalTime) s;
                } else {
                    throw new IllegalStateException("Unsupported start_time type: " + (s == null ? "null" : s.getClass()));
                }

                if (e instanceof java.sql.Time) {
                    endTime = ((java.sql.Time) e).toLocalTime();
                } else if (e instanceof LocalTime) {
                    endTime = (LocalTime) e;
                } else {
                    throw new IllegalStateException("Unsupported end_time type: " + (e == null ? "null" : e.getClass()));
                }

                LocalDateTime startDateTime = LocalDateTime.of(targetDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(targetDate, endTime);

                int driverId = findFreeDriver(targetDate);
                if(driverId==0){
                    throw new RuntimeException("Not enough Drivers");
                }
                Transport theTransport = transportDao.getTransportById(driverId);
                BigDecimal transportCost = theTransport.getCost();

                int transportCostInInt = transportCost.intValue();
                AddressDto hotelAdd = new AddressDto(hotel.getStreet(),hotel.getCity(),hotel.getState(),hotel.getPin());
                AddressDto locationAdd = new AddressDto(item.getStreet_name(),item.getCity(),item.getState(),item.getPin());
                //hotel to location
                packageBookingDao.assignTransportToPackageBooking(packageId,itemId,driverId,packageBookingId,transportCostInInt,startDateTime,hotelAdd,locationAdd);

                //location to hotel
                packageBookingDao.assignTransportToPackageBooking(packageId,itemId,driverId,packageBookingId,transportCostInInt,endDateTime,locationAdd,hotelAdd);
                currentDay++;
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private int findHotelForDay(int day, List<IncludeRooms> roomsList){
        for(IncludeRooms packageWithRoom : roomsList){
            int checkInDay = packageWithRoom.getCheck_in_day();
            int checkOutDay = packageWithRoom.getCheck_out_day();
            if(day<= checkOutDay && day >=checkInDay){
                return packageWithRoom.getHotel_id();
            }
        }
        return 0;
    }

    public void bookHotelsForPackageBooking(int packageBookingId, int customerId){
        try{
            PackageBooking thePackageBooking = packageBookingDao.getPackageBookingById(packageBookingId);
            int packageId = thePackageBooking.getPackage_id();

            //find the hotel,checkDayNumbers for package in form of a list
            List<IncludeRooms> theRoomsToStay = packageDAO.findAllIncludeRoomsByPackageId(packageId);

            Date startDate = thePackageBooking.getStart_date();
            ZoneId zone = ZoneId.systemDefault();

            LocalDate startLocalDate;
            if (startDate == null) {
                throw new IllegalArgumentException("start_date is null for booking " + packageBookingId);
            }
            if (startDate instanceof java.sql.Date) {
                startLocalDate = ((java.sql.Date) startDate).toLocalDate();
            } else {
                startLocalDate = startDate.toInstant().atZone(zone).toLocalDate();
            }

            int currentDay=1;

            //for each item in the list book a corresponding hotel with appropriate checkin and checkout dates
            for(IncludeRooms currRoom : theRoomsToStay){
                int checkIn = currRoom.getCheck_in_day();
                int checkOut = currRoom.getCheck_out_day();

                LocalDate targetCheckinDate = startLocalDate.plusDays((long)checkIn - 1);
                LocalDate targetCheckoutDate = startLocalDate.plusDays((long)checkOut - 1);

                java.sql.Date checkinDate = java.sql.Date.valueOf(targetCheckinDate);
                java.sql.Date checkoutDate = java.sql.Date.valueOf(targetCheckoutDate);

                int hotelBookingId = hotelBookingDao.createHotelBooking(checkinDate, checkoutDate, 1, "ROOMTYPE",
                        thePackageBooking.getNumber_of_people(), 100, checkOut - checkIn,
                        currRoom.getHotel_id(), currRoom.getRoom_id(), customerId);


                //assign this booking to packageBooking itinerary wise...

                //major flaw here
                //assuming each itinerary runs for one day only hence for item id i am taking as currentDay
                while(currentDay!=checkOut){
                    packageBookingDao.assignHotelBookingToPackageBooking(packageBookingId,hotelBookingId,packageId,currentDay);
                    currentDay++;
                }


            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

//    public PackageBooking getPackageBookingById(int packageBookingId) {
//        return packageBookingDao.getPackageBookingById(packageBookingId);
//    }

    // Add these methods to PackageBookingService.java

    @Transactional
    public int createPackageBookingWithTravellers(int userId, PackageBooking packageBooking, List<Traveller> travellers) {
        try {
            // Find the package
            TourPackage thePackage = packageDAO.findPackageById(packageBooking.getPackage_id());

            if(thePackage.getMax_capacity()<travellers.size()){
                throw new RuntimeException("More travellers than max_capacity of a tour!");
            }

            // Calculate cost
            int costPerPerson = thePackage.getPrice();
            int personCount = packageBooking.getNumber_of_people();
            int cost = costPerPerson * personCount;

            // Create package booking with PENDING status
            int packageBookingId = packageBookingDao.createPackageBooking(userId, packageBooking, cost);

            // Attach customer to package_booking
            packageBookingDao.assignCustomerToPackage(userId, packageBookingId);

            // Create and assign travellers
            for (Traveller traveller : travellers) {
                int travellerId = packageBookingDao.createTraveller(traveller);
                packageBookingDao.assignTravellerToPackageBooking(travellerId, packageBookingId);
            }

            return packageBookingId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create package booking: " + e.getMessage(), e);
        }
    }

    public PackageBooking getPackageBookingById(int packageBookingId) {
        try {
            return packageBookingDao.getPackageBookingById(packageBookingId);
        } catch (Exception e) {
            throw new RuntimeException("Package booking not found", e);
        }
    }

    public List<PackageAfterBookingDayDto> getPackageAfterBookingDay(PackageBooking thePackageBooking) {
        List<ItineraryItem> theItems = packageDAO.findAllItineraryItemsByPackageId(thePackageBooking.getPackage_id());
        List<PackageAfterBookingDayDto> packageAfterBookingDayDtoList = new ArrayList<>();
        for(ItineraryItem theItem: theItems){
            GuideAssignment guideAssignment = packageBookingDao.getGuideAssignment(thePackageBooking.getBooking_id(),theItem.getPackage_id(),theItem.getItem_id());
            Guide guide = guideDao.getGuideById(guideAssignment.getGuide_id());
            TransportAssignment transportAssignment = packageBookingDao.getTransportAssignment(thePackageBooking.getBooking_id(),theItem.getPackage_id(),theItem.getItem_id());
            Transport transport = transportDao.getTransportById(transportAssignment.getDriver_id());
            HotelAssignment hotelAssignment = packageBookingDao.getHotelAssignment(thePackageBooking.getBooking_id(),theItem.getPackage_id(),theItem.getItem_id());
            HotelBooking hotelBooking = hotelBookingDao.getHotelBookingById(hotelAssignment.getHotel_booking_id());
            Hotel hotel = hotelDao.findHotelById(hotelBooking.getHotel_id());
            RoomType room = hotelDao.findRoomByHotelAndRoomId(hotelBooking.getHotel_id(),hotelBooking.getRoom_id());
            PackageAfterBookingDayDto packageAfterBookingDayDto = new PackageAfterBookingDayDto(room,hotel,hotelBooking,hotelAssignment,transport,transportAssignment,guide,guideAssignment);
            packageAfterBookingDayDtoList.add(packageAfterBookingDayDto);
        }
        return packageAfterBookingDayDtoList;
    }

    public List<PackageBookingDto> getAllPackageBookingsOfCustomer(Integer userId, String status) {
        try {
            List<PackageBooking> thePackageBookings;
            List<PackageBookingDto> thePackageBookingDtos = new ArrayList<>();
            if (status == null || status.equals("all")) {
                thePackageBookings = packageBookingDao.getAllPackageBookingsByCustomerId(userId);

                for(PackageBooking thePackageBooking: thePackageBookings){
                    Booking theParentBooking = packageBookingDao.getBookingForPackageBooking(thePackageBooking.getBooking_id());
                    thePackageBookingDtos.add(new PackageBookingDto(thePackageBooking,theParentBooking));
                }
            } else {
                thePackageBookings = packageBookingDao.getPackageBookingsByCustomerIdAndStatus(userId, status);

                for(PackageBooking thePackageBooking: thePackageBookings){
                    Booking theParentBooking = packageBookingDao.getBookingForPackageBooking(thePackageBooking.getBooking_id());
                    thePackageBookingDtos.add(new PackageBookingDto(thePackageBooking,theParentBooking));
                }
            }
            return thePackageBookingDtos;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch package bookings: " + e.getMessage(), e);
        }
    }

    public void confirmPackageBookingStatus(Integer packageBookingId) {
        try{
            packageBookingDao.changePackageBookingStatus("CONFIRMED",packageBookingId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Cancels a hotel booking if the user is authorized and the booking is cancellable.
     * @param bookingId The ID of the booking to cancel.
     * @param userId The ID of the user attempting the cancellation.
     * @return The updated PackageBooking object.
     * @throws com.example.demo.exception.ResourceNotFoundException if the booking is not found.
     * @throws UnauthorizedException if the user is not the owner of the booking.
     * @throws IllegalStateException if the booking is already cancelled or completed.
     */
    public PackageBooking cancelPackageBooking(int bookingId, int userId) {
        // Step 1: Fetch the booking. Handles "not found".
        PackageBooking thePackageBooking = packageBookingDao.getPackageBookingById(bookingId);
        Booking theParentBooking = packageBookingDao.getBookingForPackageBooking(thePackageBooking.getBooking_id());


        // Step 2: Verify ownership.
        if (thePackageBooking.getCustomer_id() != userId) {
            throw new UnauthorizedException("You are not authorized to cancel this booking.");
        }

        // Step 3: Check if the booking is already in a final state.
        String currentStatus = thePackageBooking.getStatus();
        if ("CANCELLED".equalsIgnoreCase(currentStatus) || "COMPLETED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException("Booking is already " + currentStatus + " and cannot be cancelled.");
        }

        // Step 4: Update the status in the database.
        int packageBookingRowsAffected = packageBookingDao.updatePackageBookingStatus("CANCELLED", bookingId);
        int bookingRowsAffected = packageBookingDao.updateBookingStatus("CANCELLED",theParentBooking.getBooking_id());
        if (packageBookingRowsAffected == 0) {
            // This could happen in a rare race condition or if the DB connection failed mid-way
            throw new RuntimeException("Failed to update booking status in the database.");
        }

        // Step 5: Update the status in our local object and return it.
        thePackageBooking.setStatus("CANCELLED");
        return thePackageBooking;
    }

}
