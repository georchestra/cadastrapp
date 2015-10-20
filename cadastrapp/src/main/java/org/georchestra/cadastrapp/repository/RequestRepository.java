package org.georchestra.cadastrapp.repository;

import java.util.List;

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RequestRepository
    extends JpaRepository<InformationRequest, Long>
{
    
    List<InformationRequest> findByRequestIdLike(String nom);
    
}
