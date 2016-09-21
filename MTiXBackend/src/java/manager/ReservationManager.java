/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import PropertyManagement.ReservePropertyBeanLocal;
import PropertyManagement.SeatingPlanManagementBeanLocal;
import entity.Property;
import entity.Seat;
import entity.SectionEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import util.exception.ReservationConflictException;

/**
 *
 * @author catherinexiong
 */
public class ReservationManager {

    private final ReservePropertyBeanLocal rpb;

    public ReservationManager(ReservePropertyBeanLocal rpb) {
        this.rpb = rpb;
    }

    public Long addNewEvent(String eventName, String eventDescription, String startDateTime, String endDateTime, Long propertyId) throws ReservationConflictException, ParseException {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        return rpb.addNewEvent(eventName, eventDescription, df.parse(startDateTime), df.parse(endDateTime), propertyId);

    }

    public List<Property> getReservationSearchResult(HttpServletRequest request) throws ParseException {
        String visual = request.getParameter("radioV");
        String eventScale = request.getParameter("eventscale");
        String daterange = request.getParameter("daterange");
        return rpb.getReservationSearchResult(visual,eventScale,daterange);
    }
    
    public List<Property> checkRecommendation(List<Property> properties,HttpServletRequest request){
        String visual = request.getParameter("radioV");
        return rpb.checkRecommendation(properties,visual);
    }

}
