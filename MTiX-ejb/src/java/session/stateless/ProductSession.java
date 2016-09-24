/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Alert;
import javax.ejb.Stateless;
import entity.Event;
import entity.Promotion;
import entity.PromotionType;
import entity.SectionCategory;
import entity.SessionCategoryPrice;
import entity.SessionEntity;
import entity.SubEvent;
import entity.UserEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Student-ID
 */
@Stateless
public class ProductSession implements ProductSessionLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    Event event;
    SessionEntity session = new SessionEntity();
    SubEvent subEvent;
    UserEntity user;
    int toggle = 1;

    @Override
    public void generateUser() {
        for (int i = 0; i < 10; i++) {
            user = new UserEntity();
            user.createAccount("name" + i, "password" + i, "salt" + i, "9847345" + i);
            em.persist(user);
            em.flush();
        }
        user = new UserEntity();

        for (int i = 1; i <= 3; i++) {
            SectionCategory section = new SectionCategory();
            section.createSectionCategory("CAT" + i, i);
            em.persist(section);
            em.flush();
        }

        PromotionType promotionType = new PromotionType();
        promotionType.setName("Credit Card");
        em.persist(promotionType);
        em.flush();

        promotionType = new PromotionType();
        promotionType.setName("Membership");
        em.persist(promotionType);
        em.flush();

        promotionType = new PromotionType();
        promotionType.setName("Ticket Pricing Category");
        em.persist(promotionType);
        em.flush();

        promotionType = new PromotionType();
        promotionType.setName("Volume Discount");
        em.persist(promotionType);
        em.flush();

        promotionType = new PromotionType();
        promotionType.setName("Bundled Event Promotion");
        em.persist(promotionType);
    }

    @Override
    public Long signIn(String name, String password) {
        event = new Event();
        subEvent = new SubEvent();
        session = new SessionEntity();
        Query q = em.createQuery("SELECT a FROM UserEntity a WHERE a.username=:name AND a.password=:pw");
        q.setParameter("name", name);
        q.setParameter("pw", password);
        user = (UserEntity) q.getSingleResult(); //The user will be point to the real user here
        return user.getUserId();
    }

    @Override
    public void createEvent(String name, String equipment, Integer manpower, Date start, Date end) {
        UserEntity u = new UserEntity();
        Query q = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = " + "'" + "is3102mtix@gmail.com" + "'");
        for (Object o : q.getResultList()) {
            u = (UserEntity) o;
            System.out.println(u.getUsername());
            System.out.println(u.getMobileNumber());
        }
        event = new Event();
        event.createEvent(name, equipment, manpower, start, end);
        System.out.println(name + " Event Created");
        event.setUser(u);//Cause they alr login and have the memory here!! NOTE
        em.persist(event);
        u.getEvents().add(event);

        if (toggle == 0) {
            toggle = 1;
        } else {
            toggle = 0;
        }
    }

    @Override
    public void createSubEvent(String name, String equipment, Integer manpower, Date start, Date end) {
        subEvent = new SubEvent();
        subEvent.createSubEvent(name, equipment, manpower, start, end, toggle);
        System.out.println(name + " SubEvent Created");
        subEvent.setEvent(event);
        subEvent.setUser(user);
        em.persist(subEvent);
        event.getSubEvents().add(subEvent);
        event.setHasSubEvent(true);                             //PROBLEM : Ask how come it will become unmanaged?
        em.merge(event);
        user.getSubEvents().add(subEvent);
        System.out.println(event.getHasSubEvent() + " " + event.getId());

        if (toggle == 0) {
            toggle = 1;
        } else {
            toggle = 0;
        }
    }

    @Override
    public List<ArrayList> getEventList() {
        Query q = em.createQuery("SELECT a FROM Event a WHERE a.user.userId=1");

        List<ArrayList> eventList = new ArrayList();
        ArrayList list;

        for (Object o : q.getResultList()) {
            Event eventEntity = (Event) o;
            list = new ArrayList();
            em.refresh(eventEntity);

            if (eventEntity.getSubEvents().isEmpty()) { //no subEvent
                list.add(eventEntity.getId());
                list.add(eventEntity.getName());
                list.add(eventEntity.getStart());
                list.add(eventEntity.getEnd());
                list.add("event");
                eventList.add(list);
            }
        }

        q = em.createQuery("SELECT a FROM SubEvent a WHERE a.user.userId=1");

        for (Object o : q.getResultList()) {
            SubEvent subEventEntity = (SubEvent) o;
            list = new ArrayList();
            list.add(subEventEntity.getId());
            list.add(subEventEntity.getName());
            list.add(subEventEntity.getStart());
            list.add(subEventEntity.getEnd());
            list.add("subevent");
            eventList.add(list);
        }
        return eventList;
    }

    @Override
    public int createSession(String name, ArrayList<Date> start, ArrayList<Date> end, String description, String type, Long id) {

        Integer errorChecking;
        if (type.equals("event")) {
            Event event = em.find(Event.class, id);
            for (int j = 0; j < start.size(); j++) {
                errorChecking = checkEventTime(event, start.get(j), end.get(j));
                if (errorChecking == 0) {
                    return 0;
                }
            }
            for (int i = 0; i < start.size(); i++) {
                session = new SessionEntity();
                session.createSession(name, start.get(i), end.get(i), description);
                session.setEvent(event);
                session.setSubEvent(null);
                em.persist(session);
                event.getSessions().add(session);
                System.out.println("persisted");
                em.flush();
            }
        } else {
            SubEvent subevent = em.find(SubEvent.class, id);
            for (int j = 0; j < start.size(); j++) {
                errorChecking = checkSubEventTime(subevent, start.get(j), end.get(j));
                if (errorChecking == 0) {
                    return 0;
                }
            }
            for (int i = 0; i < start.size(); i++) {
                session = new SessionEntity();
                session.createSession(name, start.get(i), end.get(i), description);
                session.setSubEvent(subevent);
                session.setEvent(null);
                em.persist(session);
                subevent.getSessions().add(session);
                System.out.println("persisted");
                em.flush();
            }
        }

        return 1;

    }

    @Override
    public List<ArrayList> searchEventSessions(Long id, String type) {
        try {
            if (type.equals("event")) {
                List<ArrayList> eventSessions = new ArrayList();
                Event event = em.find(Event.class, id);
                em.refresh(event);

                if (event.getSessions().isEmpty()) {
                    return null;
                } else {
                    for (Object obj : event.getSessions()) {
                        ArrayList attributes = new ArrayList();
                        SessionEntity sessionEntity = (SessionEntity) obj;
                        attributes.add(sessionEntity.getId()); //0
                        attributes.add(sessionEntity.getName()); //1
                        attributes.add(sessionEntity.getDescriptions()); //2

                        String start = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sessionEntity.getTimeStart());
                        String end = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sessionEntity.getTimeEnd());

                        attributes.add(start); //3
                        attributes.add(end);  //4
                        attributes.add(sessionEntity.getEvent().getProperty());  //5

                        eventSessions.add(attributes);
                    }
                    return eventSessions;
                }
            } else {
                List<ArrayList> subEventSessions = new ArrayList();
                SubEvent subEvent = em.find(SubEvent.class, id);
                em.refresh(subEvent);

                if (subEvent.getSessions().isEmpty()) {
                    return null;
                } else {
                    for (Object obj : subEvent.getSessions()) {
                        ArrayList attributes = new ArrayList();
                        SessionEntity sessionEntity = (SessionEntity) obj;
                        attributes.add(sessionEntity.getId());
                        attributes.add(sessionEntity.getName());
                        attributes.add(sessionEntity.getDescriptions());

                        String start = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sessionEntity.getTimeStart());
                        String end = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sessionEntity.getTimeEnd());

                        attributes.add(start);
                        attributes.add(end);
                        attributes.add(sessionEntity.getSubEvent().getProperty());

                        subEventSessions.add(attributes);
                    }

                    return subEventSessions;
                }

            }
        } catch (Exception ex) {
            System.out.println("No session for the event");
            return null;
        }
    }

    @Override
    public ArrayList editSessions(Long id, String type) {
        ArrayList attributes = new ArrayList();
        System.out.println(id);

        SessionEntity sessionEntity = em.find(SessionEntity.class, id);

        attributes.add(sessionEntity.getId());
        attributes.add(sessionEntity.getName());
        attributes.add(sessionEntity.getDescriptions());

        String start = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sessionEntity.getTimeStart());
        String end = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sessionEntity.getTimeEnd());

        attributes.add(start);
        attributes.add(end);

        if (type.equals("event")) {
            attributes.add(sessionEntity.getEvent().getId());
        } else {
            attributes.add(sessionEntity.getSubEvent().getId());
        }

        attributes.add(type);

        return attributes;
    }

    @Override
    public int writeSession(ArrayList data) {
        try {
            String type = data.get(7).toString();
            Long i = Long.valueOf(data.get(0).toString());
            Long eventId = Long.valueOf(data.get(6).toString());
            String name = data.get(1).toString();
            String desc = data.get(2).toString();

            //Combine date
            String date = data.get(3).toString().substring(6);
            String start = data.get(4).toString() + " " + date;
            String end = data.get(5).toString() + " " + date;

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date startDate = formatter.parse(start);
            Date endDate = formatter.parse(end);

            boolean timeError = false;

            if (startDate.after(endDate)) {
                return 0;
            }

            if (type.equals("event")) {
                Event eventEntity = em.find(Event.class, eventId);
                for (Object obj : eventEntity.getSessions()) {
                    SessionEntity sessionEntity = (SessionEntity) obj;
                    if (!sessionEntity.getId().toString().equals(i.toString())) {
                        if (sessionEntity.getTimeStart().before(startDate)) {
                            if (sessionEntity.getTimeEnd().before(startDate)) {
                                System.out.println("Start before start");
                                System.out.println(sessionEntity.getTimeEnd());
                                System.out.println(startDate);
                                System.out.println();
                                timeError = false;
                            } else {
                                timeError = true;
                                System.out.println("Start before start");
                                System.out.println("TIME ERROR");
                                System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                                        + sessionEntity.getTimeEnd());
                                System.out.println("Edited Time : " + i + " " + startDate + " " + endDate);
                            }
                        } else if (sessionEntity.getTimeStart().after(startDate)) {
                            if (sessionEntity.getTimeStart().after(endDate)) {
                                System.out.println("End before start");
                                System.out.println(sessionEntity.getTimeStart());
                                System.out.println(endDate);
                                System.out.println();
                                timeError = false;
                            } else {
                                timeError = true;
                                System.out.println("End before start");
                                System.out.println("TIME ERROR");
                                System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                                        + sessionEntity.getTimeEnd());
                                System.out.println("Edited Time : " + i + startDate + " " + endDate);

                            }

                        } else {
                            timeError = true;
                        }
                    }
                    if (timeError) {
                        return 0;
                    }
                }
            } else {
                SubEvent subEventEntity = em.find(SubEvent.class, eventId);
                for (Object obj : subEventEntity.getSessions()) {
                    SessionEntity sessionEntity = (SessionEntity) obj;
                    if (!sessionEntity.getId().toString().equals(i.toString())) {
                        if (sessionEntity.getTimeStart().before(startDate)) {
                            if (sessionEntity.getTimeEnd().before(startDate)) {
                                System.out.println("Start before start");
                                System.out.println(sessionEntity.getTimeEnd());
                                System.out.println(startDate);
                                System.out.println();
                                timeError = false;
                            } else {
                                timeError = true;
                                System.out.println("Start before start");
                                System.out.println("TIME ERROR");
                                System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                                        + sessionEntity.getTimeEnd());
                                System.out.println("Edited Time : " + i + " " + startDate + " " + endDate);
                            }
                        } else if (sessionEntity.getTimeStart().after(startDate)) {
                            if (sessionEntity.getTimeStart().after(endDate)) {
                                System.out.println("End before start");
                                System.out.println(sessionEntity.getTimeStart());
                                System.out.println(endDate);
                                System.out.println();
                                timeError = false;
                            } else {
                                timeError = true;
                                System.out.println("End before start");
                                System.out.println("TIME ERROR");
                                System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                                        + sessionEntity.getTimeEnd());
                                System.out.println("Edited Time : " + i + startDate + " " + endDate);

                            }
                        } else {
                            timeError = true;
                        }

                    }
                    if (timeError) {
                        return 0;
                    }
                }
            }

            SessionEntity sessionEntity = em.find(SessionEntity.class, i);
            sessionEntity.createSession(name, startDate, endDate, desc);
            em.flush();
            return 1;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public void deleteSessions(String[] id) {

        for (int i = 0; i < id.length; i++) {
            SessionEntity sessionEntity = em.find(SessionEntity.class, Long.parseLong(id[i]));
            em.remove(sessionEntity);
        }
    }

    @Override
    public ArrayList getSessionEvent(String type, Long id, int no) {
        ArrayList arr = new ArrayList();
        if (type.equals("event")) {
            Event event = em.find(Event.class, id);
            arr.add((String) new SimpleDateFormat("yyyy-MM-dd").format(event.getStart()));
            arr.add((String) new SimpleDateFormat("yyyy-MM-dd").format(event.getEnd()));;
            arr.add(no);
            arr.add(type);
            arr.add(id);
        } else {
            SubEvent subevent = em.find(SubEvent.class, id);
            arr.add((String) new SimpleDateFormat("yyyy-MM-dd").format(subevent.getStart()));
            arr.add((String) new SimpleDateFormat("yyyy-MM-dd").format(subevent.getEnd()));;
            arr.add(no);
            arr.add(type);
            arr.add(id);
        }
        return arr;
    }

    private Integer checkSubEventTime(SubEvent subEventEntity, Date startDate, Date endDate) {
        Boolean timeError = false;
        for (Object obj : subEventEntity.getSessions()) {
            SessionEntity sessionEntity = (SessionEntity) obj;
            if (sessionEntity.getTimeStart().before(startDate)) {
                if (sessionEntity.getTimeEnd().before(startDate)) {
                    System.out.println("Start before start");
                    System.out.println(sessionEntity.getTimeEnd());
                    System.out.println(startDate);
                    System.out.println();
                    timeError = false;
                } else {
                    timeError = true;
                    System.out.println("Start before start");
                    System.out.println("TIME ERROR");
                    System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                            + sessionEntity.getTimeEnd());
                }
            } else if (sessionEntity.getTimeStart().after(startDate)) {
                if (sessionEntity.getTimeStart().after(endDate)) {
                    System.out.println("End before start");
                    System.out.println(sessionEntity.getTimeStart());
                    System.out.println(endDate);
                    System.out.println();
                    timeError = false;
                } else {
                    timeError = true;
                    System.out.println("End before start");
                    System.out.println("TIME ERROR");
                    System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                            + sessionEntity.getTimeEnd());

                }
            } else {
                timeError = true;
            }

            if (timeError) {
                return 0;
            }
        }
        return 1;
    }

    private Integer checkEventTime(Event eventEntity, Date startDate, Date endDate) {
        Boolean timeError = false;
        for (Object obj : eventEntity.getSessions()) {
            SessionEntity sessionEntity = (SessionEntity) obj;
            if (sessionEntity.getTimeStart().before(startDate)) {
                if (sessionEntity.getTimeEnd().before(startDate)) {
                    System.out.println("Start before start");
                    System.out.println(sessionEntity.getTimeEnd());
                    System.out.println(startDate);
                    System.out.println();
                    timeError = false;
                } else {
                    timeError = true;
                    System.out.println("Start before start");
                    System.out.println("TIME ERROR");
                    System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                            + sessionEntity.getTimeEnd());
                    System.out.println("Edited Time : " + " " + startDate + " " + endDate);
                }
            } else if (sessionEntity.getTimeStart().after(startDate)) {
                if (sessionEntity.getTimeStart().after(endDate)) {
                    System.out.println("End before start");
                    System.out.println(sessionEntity.getTimeStart());
                    System.out.println(endDate);
                    System.out.println();
                    timeError = false;
                } else {
                    timeError = true;
                    System.out.println("End before start");
                    System.out.println("TIME ERROR");
                    System.out.println("One of the Session : " + sessionEntity.getId() + " " + sessionEntity.getTimeStart() + " "
                            + sessionEntity.getTimeEnd());
                    System.out.println("Edited Time : " + startDate + " " + endDate);

                }

            } else {
                timeError = true;
            }

            if (timeError) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int setPricing(Long id, Double cat1, Double cat2, Double cat3, String apply) {
        SessionEntity session = em.find(SessionEntity.class, id);
        if (session == null) {
            return 0;
        } else {
            if (apply.equals("yes")) {
                if (session.getEvent() != null) { //If it is a event session
                    Long eventID = session.getEvent().getId();
                    Event eventEntity = em.find(Event.class, eventID);
                    for (Object obj : eventEntity.getSessions()) {
                        SessionEntity sessionEntity = (SessionEntity) obj;
                        setIndividualPricing(sessionEntity, cat1, cat2, cat3);
                    }
                } else {
                    Long subEventID = session.getSubEvent().getId(); //If it is a sub event session
                    SubEvent subEvent = em.find(SubEvent.class, subEventID);
                    for (Object obj : subEvent.getSessions()) {
                        SessionEntity sessionEntity = (SessionEntity) obj;
                        setIndividualPricing(sessionEntity, cat1, cat2, cat3);
                    }
                }
            } else {
                setIndividualPricing(session, cat1, cat2, cat3);
            }
            return 1;
        }
    }

    private void setIndividualPricing(SessionEntity session, Double cat1, Double cat2, Double cat3) {
        if (session.getPrice().isEmpty()) { //Never set price at all
            int catNo = 1;
            Query q = em.createQuery("SELECT a FROM SectionCategory a WHERE a.categoryNum=:cat1");
            q.setParameter("cat1", catNo);
            SectionCategory section = (SectionCategory) q.getSingleResult();

            SessionCategoryPrice price = new SessionCategoryPrice();
            price.setPrice(cat1);
            price.setCategory(section);
            price.setSession(session);
            em.persist(price);
            em.flush();
            session.getPrice().add(price);

            q = em.createQuery("SELECT a FROM SectionCategory a WHERE a.categoryNum=:cat2");
            q.setParameter("cat2", catNo + 1);
            section = (SectionCategory) q.getSingleResult();
            price = new SessionCategoryPrice();
            price.setPrice(cat2);
            price.setCategory(section);
            price.setSession(session);
            em.persist(price);
            em.flush();
            session.getPrice().add(price);

            q = em.createQuery("SELECT a FROM SectionCategory a WHERE a.categoryNum=:cat3");
            q.setParameter("cat3", catNo + 2);
            section = (SectionCategory) q.getSingleResult();
            price = new SessionCategoryPrice();
            price.setPrice(cat3);
            price.setCategory(section);
            price.setSession(session);
            em.persist(price);
            em.flush();
            session.getPrice().add(price);
        } else {
            SessionCategoryPrice price;
            int catNo = 1;
            Query q = em.createQuery("SELECT a FROM SessionCategoryPrice a WHERE a.category.categoryNum=:cat1 AND a.session.id=:id");
            q.setParameter("cat1", catNo);
            q.setParameter("id", session.getId());
            price = (SessionCategoryPrice) q.getSingleResult();
            price.setPrice(cat1);

            catNo = 2;
            q = em.createQuery("SELECT a FROM SessionCategoryPrice a WHERE a.category.categoryNum=:cat2 AND a.session.id=:id");
            q.setParameter("cat2", catNo);
            q.setParameter("id", session.getId());
            price = (SessionCategoryPrice) q.getSingleResult();
            price.setPrice(cat2);

            catNo = 3;
            q = em.createQuery("SELECT a FROM SessionCategoryPrice a WHERE a.category.categoryNum=:cat3 AND a.session.id=:id");
            q.setParameter("cat3", catNo);
            q.setParameter("id", session.getId());
            price = (SessionCategoryPrice) q.getSingleResult();
            price.setPrice(cat3);
        }
    }

    @Override
    public List<ArrayList> getSessionsPricing(long id, String type) {
        List<ArrayList> sessionsPricing = new ArrayList();
        if (type.equals("event")) {
            Query q = em.createQuery("SELECT a FROM SessionCategoryPrice a where a.session.event.id=:id");
            q.setParameter("id", id);

            for (Object o : q.getResultList()) {
                ArrayList pricing = new ArrayList();
                SessionCategoryPrice priceEntity = (SessionCategoryPrice) o;
                pricing.add(priceEntity.getSession().getId());
                pricing.add(priceEntity.getCategory().getCategoryNum());
                pricing.add(priceEntity.getPrice());

                sessionsPricing.add(pricing);
            }
        } else {
            Query q = em.createQuery("SELECT a FROM SessionCategoryPrice a where a.session.subEvent.id=:id");
            q.setParameter("id", id);

            for (Object o : q.getResultList()) {
                ArrayList pricing = new ArrayList();
                SessionCategoryPrice priceEntity = (SessionCategoryPrice) o;
                pricing.add(priceEntity.getSession().getId());
                pricing.add(priceEntity.getCategory().getCategoryNum());
                pricing.add(priceEntity.getPrice());

                sessionsPricing.add(pricing);
            }
        }
        return sessionsPricing;
    }

    @Override
    public ArrayList getPricing(Long id) {
        Query q = em.createQuery("SELECT a FROM SessionCategoryPrice a where a.session.id=:id");
        q.setParameter("id", id);
        ArrayList price = new ArrayList();
        //Table is in order, so do not need to sort the cat in order before getting
        for (Object o : q.getResultList()) {
            SessionCategoryPrice priceEntity = (SessionCategoryPrice) o;
            price.add(priceEntity.getPrice());
        }
        return price;
    }

    @Override
    public void setPromotion_1(String[] info, String type, String name, double discount, String requirement, String desc) {
        if (type.equals("5")) {
            this.setPromotion_2(info, type, name, discount, requirement, desc);
        } else {
            long eventId;
            String eventType;
            long promotionType = Long.valueOf(type);
            Query q = em.createQuery("SELECT a FROM PromotionType a where a.id=:id");
            q.setParameter("id", promotionType);
            PromotionType promotionTypeEntity = (PromotionType) q.getSingleResult();
            Promotion promotion;

            for (int i = 0; i < info.length; i++) {
                String[] idType = info[i].split(" ");
                eventId = Long.valueOf(idType[0]);
                eventType = idType[1];
                promotion = new Promotion();
                promotion.create(name, discount, desc, requirement);
                promotion.getPromotionsType().add(promotionTypeEntity);
                if (eventType.equals("event")) {
                    q = em.createQuery("SELECT a FROM Event a where a.id=:id");
                    q.setParameter("id", eventId);
                    Event eventEntity = (Event) q.getSingleResult();
                    promotion.setEvent(eventEntity);
                    promotion.setSubEvent(null);
                    em.persist(promotion);
                    eventEntity.getPromotions().add(promotion);
                } else {
                    q = em.createQuery("SELECT a FROM SubEvent a where a.id=:id");
                    q.setParameter("id", eventId);
                    SubEvent subEventEntity = (SubEvent) q.getSingleResult();
                    promotion.setEvent(null);
                    promotion.setSubEvent(subEventEntity);
                    em.persist(promotion);
                    subEventEntity.getPromotions().add(promotion);
                }
            }
        }
    }

    private void setPromotion_2(String[] info, String type, String name, double discount, String requirement, String desc) {
        requirement = "";
        long eventId;
        String eventType;
        long promotionType = Long.valueOf(type);
        Query q = em.createQuery("SELECT a FROM PromotionType a where a.id=:id");
        q.setParameter("id", promotionType);
        PromotionType promotionTypeEntity = (PromotionType) q.getSingleResult();
        Promotion promotion;
        String[] idType;

        for (int j = 0; j < info.length; j++) {
            idType = info[j].split(" ");
            eventId = Long.valueOf(idType[0]);
            eventType = idType[1];
            if (eventType.equals("event")) {
                q = em.createQuery("SELECT a FROM Event a where a.id=:id");
                q.setParameter("id", eventId);
                Event eventEntity = (Event) q.getSingleResult();
                requirement += "Event : " + eventEntity.getId() + " ";
            } else {
                q = em.createQuery("SELECT a FROM SubEvent a where a.id=:id");
                q.setParameter("id", eventId);
                SubEvent subEventEntity = (SubEvent) q.getSingleResult();
                requirement += "SubEvent : " + subEventEntity.getId() + " ";
            }
        }

        for (int i = 0; i < info.length; i++) {
            idType = info[i].split(" ");
            eventId = Long.valueOf(idType[0]);
            eventType = idType[1];
            promotion = new Promotion();
            promotion.create(name, discount, desc, requirement);
            promotion.getPromotionsType().add(promotionTypeEntity);
            if (eventType.equals("event")) {
                q = em.createQuery("SELECT a FROM Event a where a.id=:id");
                q.setParameter("id", eventId);
                Event eventEntity = (Event) q.getSingleResult();
                promotion.setEvent(eventEntity);
                promotion.setSubEvent(null);
                em.persist(promotion);
                eventEntity.getPromotions().add(promotion);
            } else {
                q = em.createQuery("SELECT a FROM SubEvent a where a.id=:id");
                q.setParameter("id", eventId);
                SubEvent subEventEntity = (SubEvent) q.getSingleResult();
                promotion.setEvent(null);
                promotion.setSubEvent(subEventEntity);
                em.persist(promotion);
                subEventEntity.getPromotions().add(promotion);
            }
        }
    }

    @Override
    public void setPromotion_3(String[] type, String[] info, String name, double discount, String requirement, String desc) {
        String bundleEvent = "";
        long eventId;
        String eventType;
        String[] idType;
        Query q;
        long promotionType;
        boolean hasBundleEvent = false;
        Promotion promotion;
        for (int j = 0; j < info.length; j++) {
            idType = info[j].split(" ");
            eventId = Long.valueOf(idType[0]);
            eventType = idType[1];
            if (eventType.equals("event")) {
                q = em.createQuery("SELECT a FROM Event a where a.id=:id");
                q.setParameter("id", eventId);
                Event eventEntity = (Event) q.getSingleResult();
                bundleEvent += "Event : " + eventEntity.getId() + " ";
            } else {
                q = em.createQuery("SELECT a FROM SubEvent a where a.id=:id");
                q.setParameter("id", eventId);
                SubEvent subEventEntity = (SubEvent) q.getSingleResult();
                bundleEvent += "SubEvent : " + subEventEntity.getId() + " ";
            }
        }

        for (int i = 0; i < info.length; i++) {
            idType = info[i].split(" ");
            eventId = Long.valueOf(idType[0]);
            eventType = idType[1];
            promotion = new Promotion();
            for (int j = 0; j < type.length; j++) {
                if (type[j].equals("5")) {
                    hasBundleEvent = true;
                }
                promotionType = Long.valueOf(type[j]);
                q = em.createQuery("SELECT a FROM PromotionType a where a.id=:id");
                q.setParameter("id", promotionType);
                PromotionType promotionTypeEntity = (PromotionType) q.getSingleResult();
                promotion.getPromotionsType().add(promotionTypeEntity);
            }

            if (eventType.equals("event")) {
                q = em.createQuery("SELECT a FROM Event a where a.id=:id");
                q.setParameter("id", eventId);
                Event eventEntity = (Event) q.getSingleResult();
                promotion.setEvent(eventEntity);
                promotion.setSubEvent(null);
                if (hasBundleEvent) {
                    promotion.create(name, discount, desc, requirement + " " + bundleEvent);
                } else {
                    promotion.create(name, discount, desc, requirement);
                }
                em.persist(promotion);
                eventEntity.getPromotions().add(promotion);
            } else {
                q = em.createQuery("SELECT a FROM SubEvent a where a.id=:id");
                q.setParameter("id", eventId);
                SubEvent subEventEntity = (SubEvent) q.getSingleResult();
                promotion.setEvent(null);
                promotion.setSubEvent(subEventEntity);
                if (hasBundleEvent) {
                    promotion.create(name, discount, desc, requirement + " " + bundleEvent);
                } else {
                    promotion.create(name, discount, desc, requirement);
                }
                em.persist(promotion);
                subEventEntity.getPromotions().add(promotion);
            }
        }
    }

    @Override
    public int getCategory(Long id, String type) {
        Query q;
        int category = 0;
        SessionEntity sessionEntity;
        SessionCategoryPrice price;
        if (type.equals("event")) {
            q = em.createQuery("SELECT a FROM SessionEntity a where a.event.id=:id");
            q.setParameter("id", id);
            for (Object o : q.getResultList()) {
                sessionEntity = (SessionEntity) o;
                category = sessionEntity.getPrice().size();
                break;
            }
        } else {
            q = em.createQuery("SELECT a FROM SessionEntity a where a.subEvent.id=:id");
            q.setParameter("id", id);
            for (Object o : q.getResultList()) {
                sessionEntity = (SessionEntity) o;
                category = sessionEntity.getPrice().size();
                break;
            }
        }
        return category;
    }

    @Override
    public List<ArrayList> searchEventPromotion(Long id, String type) {
        try {
            System.out.println(type + id + "SEARCHEVENTPROMOTION");
            if (type.equals("event")) {
                List<ArrayList> eventPromotions = new ArrayList();
                Event event = em.find(Event.class, id);
                em.refresh(event);

                if (event.getPromotions().isEmpty()) {
                    return null;
                } else {
                    for (Object obj : event.getPromotions()) {
                        ArrayList attributes = new ArrayList();
                        Promotion promotion = (Promotion) obj;
                        attributes.add(promotion.getId()); //0
                        attributes.add(promotion.getName()); //1
                        attributes.add(promotion.getRequirements()); //2
                        attributes.add(promotion.getDiscountRate());

                        for (Object o : promotion.getPromotionsType()) {
                            PromotionType promotionType = (PromotionType) o;
                            attributes.add(promotionType.getName());
                        }

                        eventPromotions.add(attributes);
                    }
                    return eventPromotions;
                }
            } else {
                List<ArrayList> subEventPromotions = new ArrayList();
                SubEvent subEvent = em.find(SubEvent.class, id);
                em.refresh(subEvent);

                if (subEvent.getPromotions().isEmpty()) {
                    return null;
                } else {
                    for (Object obj : subEvent.getPromotions()) {
                        ArrayList attributes = new ArrayList();
                        Promotion promotion = (Promotion) obj;
                        attributes.add(promotion.getId()); //0
                        attributes.add(promotion.getName()); //1
                        attributes.add(promotion.getRequirements()); //2
                        attributes.add(promotion.getDiscountRate());

                        for (Object o : promotion.getPromotionsType()) {
                            PromotionType promotionType = (PromotionType) o;
                            attributes.add(promotionType.getName());
                        }

                        subEventPromotions.add(attributes);
                    }
                    return subEventPromotions;
                }

            }
        } catch (Exception ex) {
            System.out.println("No session for the event");
            return null;
        }
    }

    @Override
    public ArrayList editPromotion(Long id, String type) {
        ArrayList attributes = new ArrayList();

        Promotion promotionEntity = em.find(Promotion.class, id);

        if (type.equals("event")) {
            attributes.add(promotionEntity.getEvent().getId());
        } else {
            attributes.add(promotionEntity.getSubEvent().getId());
        }

        attributes.add(type);

        attributes.add(promotionEntity.getId());
        attributes.add(promotionEntity.getName());
        attributes.add(promotionEntity.getRequirements());
        attributes.add(promotionEntity.getDiscountRate());
        attributes.add(promotionEntity.getDescriptions());

        for (Object o : promotionEntity.getPromotionsType()) {
            PromotionType promotionType = (PromotionType) o;
            attributes.add(promotionType.getName());
        }
        return attributes;
    }

    @Override
    public void writePromotion(ArrayList data) {
        long eventId = Long.valueOf(data.get(0).toString());
        String eventType = data.get(1).toString();

        Query q = em.createQuery("SELECT a FROM Promotion a where a.id=:id");
        q.setParameter("id", Long.valueOf(data.get(2).toString()));
        Promotion promotionEntity = (Promotion) q.getSingleResult();
        promotionEntity.create(data.get(6).toString(), Double.valueOf(data.get(4).toString()), data.get(3).toString(), data.get(5).toString());
    }

    @Override
    public void deletePromotion(String[] id) {

        for (int i = 0; i < id.length; i++) {
            Promotion promotionEntity = em.find(Promotion.class, Long.parseLong(id[i]));
            em.remove(promotionEntity);
        }
    }

    @Override
    public ArrayList<ArrayList<String>> getPromotion() {
        Query q = em.createQuery("SELECT p FROM Promotion p");
        ArrayList<ArrayList<String>> promotion = new ArrayList();

        for (Object o : q.getResultList()) {
            Promotion p = new Promotion();
            p = (Promotion) o;
            ArrayList<String> promotionDetails = new ArrayList();

            promotionDetails.add(Long.toString(p.getId()));
            promotionDetails.add(p.getName());
            promotionDetails.add(p.getDescriptions());
            promotionDetails.add(String.valueOf(p.getNumberOfTickets()));
            promotion.add(promotionDetails);
        }

        return promotion;
    }

    @Override
    public void addTickets(String promotionId, String numOfTics) {
        Query q = em.createQuery("Select p FROM Promotion p WHERE p.id = " + promotionId);
        int existingNumber = 0;
        for (Object o : q.getResultList()) {
            Promotion p = (Promotion) o;
            existingNumber = p.getNumberOfTickets();
        }
        int newAmount = existingNumber + Integer.parseInt(numOfTics);
        String newAmount2 = String.valueOf(newAmount);
        Query q2 = em.createQuery("UPDATE Promotion p SET p.numberOfTickets = " + newAmount2 + " WHERE p.id = " + promotionId);
        q2.executeUpdate();
    }

    @Override
    public void deleteTickets(String promotionId, String numOfTics) {
        Query q = em.createQuery("Select p FROM Promotion p WHERE p.id = " + promotionId);
        int existingNumber = 0;
        for (Object o : q.getResultList()) {
            Promotion p = (Promotion) o;
            existingNumber = p.getNumberOfTickets();
        }
        // int newAmount = existingNumber-Integer.parseInt(numOfTics);
        //String newAmount2 = String.valueOf(newAmount);
        Query q2 = em.createQuery("UPDATE Promotion p SET p.numberOfTickets = 0" + " WHERE p.id = " + promotionId);
        q2.executeUpdate();
    }

    @Override
    public boolean checkTicketAmount(String promotionId, String numOfTics) {
        Query q = em.createQuery("Select p FROM Promotion p WHERE p.id = " + promotionId);
        int existingNumber = 0;
        for (Object o : q.getResultList()) {
            Promotion p = (Promotion) o;
            existingNumber = p.getNumberOfTickets();
        }
        //int amountToDelete = Integer.parseInt(numOfTics);

        if (existingNumber == 0) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void editTicketAmount(String promotionId, String ticketAmount) {
        Query q = em.createQuery("UPDATE Promotion p SET p.numberOfTickets =" + ticketAmount + " WHERE p.id = " + promotionId);
        q.executeUpdate();
    }

    @Override
    public ArrayList<ArrayList<String>> retrieveEvents() {
        Query q = em.createQuery("SELECT e FROM Event e");
        ArrayList<ArrayList<String>> events = new ArrayList();

        for (Object o : q.getResultList()) {
            Event e = new Event();
            e = (Event) o;
            ArrayList<String> event = new ArrayList();

            event.add(Long.toString(e.getId()));
            event.add(e.getName());
            event.add(e.getDescription());
            events.add(event);
        }

        return events;
    }

    @Override
    public void addAlert(String percentage, String alertType, String username, String date, String eventId) {
        try {
            Alert a = new Alert();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(date);

            a.createAlert(alertType, username, startDate, Integer.parseInt(percentage));
            Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = " + eventId);
            Event e = new Event();

            for (Object o : q.getResultList()) {
                e = (Event) o;
            }

            e.setAlert(a);
            em.merge(e);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void addSubEvent() {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = 2");
        Query q2 = em.createQuery("SELECT s FROM SubEvent s WHERE s.id = 1");
        Query q3 = em.createQuery("SELECT s FROM SubEvent s WHERE s.id = 2");
        Event e = new Event();
        SubEvent s1 = new SubEvent();
        SubEvent s2 = new SubEvent();

        for (Object o : q.getResultList()) {
            e = (Event) o;
        }
        for (Object o : q2.getResultList()) {
            s1 = (SubEvent) o;
        }
        for (Object o : q3.getResultList()) {
            s2 = (SubEvent) o;
        }
        List<SubEvent> subEvents = new ArrayList();
        s1.setEvent(e);
        s2.setEvent(e);
        subEvents.add(s1);
        subEvents.add(s2);
        e.setSubEvents(subEvents);
        em.merge(e);
    }

    @Override
    public boolean checkSubEvent(String eventId) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = " + eventId);
        Event e = new Event();

        for (Object o : q.getResultList()) {
            e = (Event) o;

        }

        if (e.getHasSubEvent()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void addSubEventAlert(String percentage, String alertType, String username, String date, String eventId) {
        try {
            System.out.println("percentage: " + percentage);
            System.out.println("Alert Type: " + alertType);
            System.out.println("person: " + username);
            System.out.println("date: " + date);
            System.out.println("eventId: " + eventId);
            Alert a = new Alert();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(date);
            Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = " + eventId);
            Event e = new Event();
            System.out.println("Entered subevent alert 3");
            for (Object o : q.getResultList()) {
                e = (Event) o;
                System.out.println("Entered subevent alert 4");
            }

            System.out.println("Entered subevent alert 5");

            List<SubEvent> subevents = e.getSubEvents();

            System.out.println("Entered subevent alert 6");

            for (Object o : subevents) {
                System.out.println("Entered subevent alert 7");
                SubEvent s = (SubEvent) o;
                Alert alert = new Alert();
                alert.createAlert(alertType, username, startDate, Integer.parseInt(percentage));
                s.setAlert(alert);
                em.merge(s);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void editAlert(String percentage, String alertType, String username, String date, String eventId) {
        try {
            System.out.println("percentage: " + percentage);
            System.out.println("Alert Type: " + alertType);
            System.out.println("person: " + username);
            System.out.println("date: " + date);
            System.out.println("eventId: " + eventId);
            Alert a = new Alert();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(date);
            Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = " + eventId);
            Event e = new Event();
            System.out.println("Entered subevent alert 3");
            for (Object o : q.getResultList()) {
                e = (Event) o;
                System.out.println("Entered subevent alert 4");
            }

            System.out.println("Entered subevent alert 5");

            String alertId = Long.toString(e.getAlert().getId());
            Query q2 = em.createQuery("UPDATE Alert a SET a.percentage =" + percentage + " WHERE a.id = " + alertId);
            q2.executeUpdate();
            Query q3 = em.createQuery("UPDATE Alert a SET a.alertType =" + "'" + alertType + "'" + " WHERE a.id = " + alertId);
            q3.executeUpdate();
            Query q4 = em.createQuery("UPDATE Alert a SET a.username =" + "'" + username + "'" + " WHERE a.id = " + alertId);
            q4.executeUpdate();
            Query q5 = em.createQuery("SELECT a FROM Alert a WHERE a.id = " + alertId);
            System.out.println("date: " + date);

            for (Object o : q5.getResultList()) {
                a = (Alert) o;
            }
            a.setAlertDate(startDate);
            em.merge(a);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void editSubEventAlert(String percentage, String alertType, String username, String date, String eventId) {
        try {
            System.out.println("percentage: " + percentage);
            System.out.println("Alert Type: " + alertType);
            System.out.println("person: " + username);
            System.out.println("date: " + date);
            System.out.println("eventId: " + eventId);
            Alert a = new Alert();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(date);
            Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = " + eventId);
            Event e = new Event();
            System.out.println("Entered subevent edit alert 3");
            for (Object o : q.getResultList()) {
                e = (Event) o;
                System.out.println("Entered subevent edit alert 4");
            }

            System.out.println("Entered subevent edit alert 5");
            for (Object o : e.getSubEvents()) {
                SubEvent s = (SubEvent) o;
                String alertId = Long.toString(s.getAlert().getId());
                System.out.println("alert id: " +alertId);
                Query q2 = em.createQuery("UPDATE Alert a SET a.percentage =" + percentage + " WHERE a.id = " + alertId);
                q2.executeUpdate();
                Query q3 = em.createQuery("UPDATE Alert a SET a.alertType =" + "'" + alertType + "'" + " WHERE a.id = " + alertId);
                q3.executeUpdate();
                Query q4 = em.createQuery("UPDATE Alert a SET a.username =" + "'" + username + "'" + " WHERE a.id = " + alertId);
                q4.executeUpdate();
                Query q5 = em.createQuery("SELECT a FROM Alert a WHERE a.id = " + alertId);
                for (Object t : q5.getResultList()) {
                    a = (Alert) t;
                }
                a.setAlertDate(startDate);
                em.merge(a);
            }

            

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
