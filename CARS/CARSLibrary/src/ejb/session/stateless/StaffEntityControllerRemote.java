/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.StaffNotFoundException;

/**
 *
 * @author gem
 */
@Remote
public interface StaffEntityControllerRemote {
    
    long createNewStaffEntity(StaffEntity staffEntity);

    List<StaffEntity> retrieveAllStaffEntity();

    StaffEntity retrieveStaffByStaffId(long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;
    
    StaffEntity staffLogin(String username, String password) throws InvalidLoginException;

    void updateStaffEntity(StaffEntity staffEntity);

    void deleteStaffEntityById(StaffEntity staffId);
    
}
