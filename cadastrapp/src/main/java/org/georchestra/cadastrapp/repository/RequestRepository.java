package org.georchestra.cadastrapp.repository;

import java.util.Date;
import java.util.List;

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * 
 * @author Pierre jego
 * 
 * Repository to get InformationRequest for database
 *
 */
public interface RequestRepository
    extends JpaRepository<InformationRequest, Long>
{
    
	/**
	 *  Return all informationRequest where user card number is like given param
	 *  
	 * @param cni id of "carte nationale d'identitée"
	 * @return list of InformationRequest for this cni
	 */
    List<InformationRequest> findByUserCniLike(String cni);
    
    /**
     *  Give the information request corresponding to id given
     *  
     * @param requestId - requestId is generated and increment in database
     * @return InformationRequest
     */
    InformationRequest findByRequestId(long requestId);
    
    /**
     *  Count how many information request had been made by a user given is card number since a date
     *  
     * @param cni UserRequest cni ( User card number)
     * @param type UserRequest type (A, P1, P2 or P3)
     * @param date	Date to check
     * @return number of Information request for this cni after a given date
     */
    int countByUserCniAndUserTypeAndRequestDateAfter(String cni, String type, Date date);
    
    /**
     *  Count how many object had been requested by a user given is card number since a date
     *  
     * @param cni UserRequest cni ( User card number)
     * @param type UserRequest type (A, P1, P2 or P3)
     * @param date Date to check
     * @return number of Information request for this cni after a given date
     */
    @Query(value="select COALESCE(SUM(ir.objectNumber),0) from InformationRequest ir inner join ir.user u where u.cni= ?1 and u.type = ?2 and ir.requestDate >= ?3")
    int sumObjectNumberByUserCniAndUserTypeAndRequestDateAfter(String cni, String type, Date date);
    
    
}
