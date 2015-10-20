package org.georchestra.cadastrapp.repository;

import java.util.List;

import org.georchestra.cadastrapp.model.request.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRequestRepository
    extends JpaRepository<UserRequest, Long>
{
    
    List<UserRequest> findByCniLike(String nom);
    
}
