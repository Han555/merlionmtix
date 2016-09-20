/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PropertyManagement;

import entity.Equipment;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author catherinexiong
 */
@Local
public interface EquipmentBeanLocal {

    public List<Equipment> getEquipmentInProperty(Long propertyId);

    public List<Equipment> getNonSEquipmentInProperty(Long propertyId);
    
}
