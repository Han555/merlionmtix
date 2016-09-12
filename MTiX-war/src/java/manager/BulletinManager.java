/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.util.ArrayList;
import session.stateless.BulletinSessionLocal;

/**
 *
 * @author Student-ID
 */
public class BulletinManager {
    private BulletinSessionLocal bulletinSession;
    
    public BulletinManager(BulletinSessionLocal bulletinSession) {
        this.bulletinSession = bulletinSession;
    }
    
    public void releaseMessage(String message, String subject) {
        bulletinSession.broadcast(message, subject);
    }
    
    public ArrayList<ArrayList<String>> getBoard() {
        return bulletinSession.retrieveBulletin();
    }
    
    public ArrayList<String> retrieveMessage(String id) {
        return bulletinSession.getMessage(id);
    }
}
