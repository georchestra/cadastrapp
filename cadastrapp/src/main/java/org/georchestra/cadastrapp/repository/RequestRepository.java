package org.georchestra.cadastrapp.repository;

import java.util.Date;
import java.util.List;

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 
 * @author gfi
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
	 * @param cni
	 * @return List<InformationRequest>
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
     * @param date
     * @return number of Information request for this cni after a given date
     */
    int countByUserCniAndRequestDateAfter(String cni, Date date);
    
}
