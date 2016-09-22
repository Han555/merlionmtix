/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PropertyManagement;

import java.util.Date;
import java.util.List;
import entity.MaintenanceSchedule;
import javax.ejb.Local;

@Local
public interface MaintenanceBeanLocal {
    
    public Boolean addMaintenance(Long propertyId, Date start, Date end);
    public Boolean removeMaintenance(Long id);
    public Boolean updateMaintenance(Long id, Date start, Date end);
    public List<MaintenanceSchedule> getMaintenanceInProperty(Long propertyId);
    
    public MaintenanceSchedule getMaintenanceScheduleById(Long id);
    
}
