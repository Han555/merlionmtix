/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PropertyManagement;

import entity.Event;
import entity.Property;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import util.exception.ReservationConflictException;

/**
 *
 * @author catherinexiong
 */
@Local
public interface ReservePropertyBeanLocal {

   

    public Event getEventById(long id);

    public Long addNewEvent(String eventName, String eventDescription, Date startDateTime, Date endDateTime, Long propertyId) throws ReservationConflictException;

    public List<Property> getReservationSearchResult(String visual, String eventScale, String daterange) throws ParseException;

    public List<Property> checkRecommendation(List<Property> properties, String visual);


    
}
