package org.georchestra.cadastrapp.repository;


import org.georchestra.cadastrapp.model.request.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Pierre Jégo
 *
 * Repository to get UserRequest for database
 */
public interface UserRequestRepository
    extends JpaRepository<UserRequest, Long>
{
	
    /**
     *  Get userRequest from database using cni id
     *  
     * @param cni UserRequest cni ( User card number)
     * @return UserRequest using given cni
     */
    UserRequest findByCni(String cni);
    
    
    /**
     *  Get userRequest from database using cni id
     *  
     * @param cni UserRequest cni ( User card number)
     * @param type	type of user
     * @return UserRequest using given cni and type
     */
    UserRequest findByCniAndType(String cni, String type);
}
