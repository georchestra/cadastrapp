package org.georchestra.cadastrapp.repository;

import org.georchestra.cadastrapp.model.request.ObjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * 
 * @author gfi
 * 
 * Repository to get InformationRequest for database
 *
 */
public interface ObjectRequestRepository
    extends JpaRepository<ObjectRequest, Long>
{
    
	
    
}
