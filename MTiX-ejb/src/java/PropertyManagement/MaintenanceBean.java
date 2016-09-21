/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PropertyManagement;

import java.util.Date;
import entity.MaintenanceSchedule;
import entity.Property;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


public class MaintenanceBean implements MaintenanceBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    SeatingPlanManagementBeanLocal spm;

    @Override
    public MaintenanceSchedule getMaintenanceScheduleById(Long id) {
        return (MaintenanceSchedule) em.find(MaintenanceSchedule.class, id);
    }

    @Override
    public Boolean addMaintenance(Long propertyId, Date start, Date end) {
        Boolean result = true;
        try {
            MaintenanceSchedule ms = new MaintenanceSchedule();
            ms.setEndDate(end);
            ms.setStartDate(start);
            ms.setProperty(spm.getPropertyById(propertyId));

            em.persist(ms);
            em.flush();
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    @Override
    public Boolean removeMaintenance(Long id) {
        Boolean result = true;
        try {
            em.remove(getMaintenanceScheduleById(id));
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    @Override
    public Boolean updateMaintenance(Long id, Date start, Date end) {

        Boolean result = true;
        try {
            MaintenanceSchedule ms = getMaintenanceScheduleById(id);
            ms.setEndDate(end);
            ms.setStartDate(start);

            em.merge(ms);
        } catch (Exception e) {
            result = false;
        }

        return result;

    }

    @Override
    public List<MaintenanceSchedule> getMaintenanceInProperty(Long propertyId) {
        Property property = spm.getPropertyById(propertyId);
        Query query = em.createQuery("SELECT ms FROM MaintenanceSchedule ms where ms.property=:inProperty");
        query.setParameter("inProperty", property);
        return query.getResultList();
    }

}
