/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PropertyManagement;

import entity.Equipment;
import entity.Property;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author catherinexiong
 */
@Stateless
public class EquipmentBean implements EquipmentBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    SeatingPlanManagementBeanLocal spm;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Equipment> getEquipmentInProperty(Long propertyId) {
        
        Property property = spm.getPropertyById(propertyId);
        Query query = em.createQuery("SELECT eq FROM Equipment eq where eq.property=:inProperty");
        query.setParameter("inProperty", property);
        return query.getResultList();

    }
    @Override
    public List<Equipment> getNonSEquipmentInProperty(Long propertyId) {
        
        Property property = spm.getPropertyById(propertyId);
        Query query = em.createQuery("SELECT eq FROM Equipment eq where eq.property=:inProperty");
        query.setParameter("inProperty", property);
        return query.getResultList();

    }
}
