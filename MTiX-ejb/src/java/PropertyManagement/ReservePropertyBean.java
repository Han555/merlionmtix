/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PropertyManagement;

import entity.Event;
import entity.MaintenanceSchedule;
import entity.Property;
import entity.UserEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationConflictException;

/**
 *
 * @author catherinexiong
 */
@Stateless
public class ReservePropertyBean implements ReservePropertyBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    SeatingPlanManagementBeanLocal spm;

    /**
     * ****
     *
     * private methods
     */
    private UserEntity getUserEntity(Long userId) {
        UserEntity user = em.find(UserEntity.class, userId);
        return user;
    }

    private Boolean checkPropertyConflict(Date startDate, Date endDate, Long propertyId) {
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.property = :property AND e.start <= :endDate AND e.end >= :startDate");
        query.setParameter("property", spm.getPropertyById(propertyId));
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkMaintenanceConflict(Date startDate, Date endDate, Long propertyId) {
        List<MaintenanceSchedule> ms = spm.getPropertyById(propertyId).getMaintenanceSchedule();
        for (MaintenanceSchedule m : ms) {
            if (endDate.getTime() >= m.getStartDate().getTime()) {
                if (startDate.getTime() <= m.getEndDate().getTime()) {
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public List<Property> checkRecommendation(List<Property> properties, String visual) {
        Boolean vEvent = false;
        if (visual.equalsIgnoreCase("yes")) {
            vEvent = true;
        }
        List<Property> pRListFinal = new ArrayList();
        if (vEvent) {

            for (Property p : properties) {
                if (p.getPropertyName().equals("Merlion Star Theater")) {
                    pRListFinal.add(spm.getPropertyById(Long.valueOf(2)));

                }
     
            }
        }
        return pRListFinal;

    }

    @Override
    public Event getEventById(long id) {
        return (Event) em.find(Event.class, id);
    }

    public Long addNewEvent(String eventName, String eventDescription, Date startDateTime, Date endDateTime, Long propertyId) throws ReservationConflictException {
        if (!checkPropertyConflict(startDateTime, endDateTime, propertyId)) {
            // UserEntity user = getUserEntity(userId);
            Event event = new Event();
            event.setName(eventName);
            event.setStart(startDateTime);
            event.setEnd(endDateTime);
            event.setProperty(spm.getPropertyById(propertyId));
            //  event.setUser(user);
            //  event.setStatus("Pending");
            em.persist(event);
            em.flush();
            // em.getEvents().add(event);
            //  em.merge(systemUser);
            return event.getId();
        } else {
            throw new ReservationConflictException("Reservation conflict: " + propertyId);
        }
    }

    @Override
    public List<Property> getReservationSearchResult(String visual, String eventScale, String daterange) throws ParseException {
        Boolean vEvent = false;
        if (visual.equalsIgnoreCase("yes")) {
            vEvent = true;
        }
        String[] parts = daterange.split("-");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = df.parse(parts[0]);
        Date endDate = df.parse(parts[1]);

        List<Property> pList = spm.getAllProperties();
        List<Property> aList = new ArrayList();
        List<Property> aListFinal = new ArrayList();
        for (Property p : pList) {
            if ((!checkPropertyConflict(startDate, endDate, p.getId())) && (!checkMaintenanceConflict(startDate, endDate, p.getId()))) {
                aList.add(p);

            }
        }
        if (eventScale.equalsIgnoreCase("SS")) {
            for (Property pa : aList) {
                if (pa.getCapacity() <= 3000) {
                    aListFinal.add(pa);
                }
            }

        } else if (eventScale.equalsIgnoreCase("MS")) {
            for (Property pa : aList) {
                if (pa.getCapacity() <= 8000) {
                    aListFinal.add(pa);
                }
            }
        } else {
            for (Property pa : aList) {
                if (pa.getCapacity() > 8000) {
                    aListFinal.add(pa);
                }
            }
        }

        return aListFinal;

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
