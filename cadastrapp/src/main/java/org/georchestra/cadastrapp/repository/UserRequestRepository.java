package org.georchestra.cadastrapp.repository;


import org.georchestra.cadastrapp.model.request.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author gfi
 *
 * Repository to get UserRequest for database
 */
public interface UserRequestRepository
    extends JpaRepository<UserRequest, Long>
{
	
    /**
     *  Get userRequest from database using cni id
     *  
     * @param cni
     * @return UserRequest using given cni
     */
    UserRequest findByCni(String cni);
    
}
