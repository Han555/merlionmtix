/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author JingYing
 */
@Entity
public class SubEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date start;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date end; 
    
    @ManyToOne //Owning side, has foreign key!
    private Event event; 
    @OneToMany(mappedBy="subEvent")
    private Collection<SessionEntity> sessions = new ArrayList<SessionEntity>();
    @OneToMany(mappedBy = "subEvent")
    private Collection<Promotion> promotions = new ArrayList<Promotion>();
    @ManyToOne //Owning side, has foreign key!
    private UserEntity user; 
     @ManyToMany
    private List<Equipment> equipments = new ArrayList<Equipment>();
    @ManyToMany
    private List<Manpower> manpower = new ArrayList<Manpower>();
    @ManyToOne
    private Property property;

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public List<Manpower> getManpower() {
        return manpower;
    }

    public void setManpower(List<Manpower> manpower) {
        this.manpower = manpower;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
    
    
    public void createSubEvent(String name, String equipment,Integer manpower, Date start, Date end, int property){
        this.setName(name);
        
        this.setEnd(end);
        
        this.setStart(start);
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Collection<SessionEntity> getSessions() {
        return sessions;
    }

    public void setSessions(Collection<SessionEntity> sessions) {
        this.sessions = sessions;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    

    public Collection<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(Collection<Promotion> promotions) {
        this.promotions = promotions;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubEvent)) {
            return false;
        }
        SubEvent other = (SubEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.SubEvent[ id=" + id + " ]";
    }
    
}
