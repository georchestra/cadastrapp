package org.georchestra.cadastrapp.scheduler;

import java.util.Calendar;
import java.util.List;

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.georchestra.cadastrapp.model.request.ObjectRequest;
import org.georchestra.cadastrapp.repository.ObjectRequestRepository;
import org.georchestra.cadastrapp.repository.RequestRepository;
import org.georchestra.cadastrapp.repository.UserRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("anonymizationSchedulerBean")
public class AnonymizationScheduler {
    @Autowired
    RequestRepository requestRepository;
    
    @Autowired
    ObjectRequestRepository objectRequestRepository;
    
    @Autowired
    UserRequestRepository userRequestRepository;
    
    public void anonymize() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        List<InformationRequest> ls = requestRepository.findAllByRequestDateBefore(cal.getTime());
        for (InformationRequest informationRequest : ls) {
            for (ObjectRequest objectRequest : informationRequest.getObjectsRequest()) {
                objectRequestRepository.delete(objectRequest);
            }
            userRequestRepository.delete(informationRequest.getUser());
        }
    }
}
