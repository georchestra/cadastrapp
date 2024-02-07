package org.georchestra.cadastrapp.scheduler;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.georchestra.cadastrapp.model.request.ObjectRequest;
import org.georchestra.cadastrapp.model.request.UserRequest;
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
    
    public void anonymize() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -30);
        if(cal.after(Calendar.getInstance())){
            cal.roll(Calendar.YEAR, -1);
        }
        
        List<InformationRequest> ls = requestRepository.findAllByRequestDateBefore(cal.getTime());
        
        for (InformationRequest informationRequest : ls) {
            UserRequest user = informationRequest.getUser();
            if( requestRepository.countByRequestDateBetweenAndUser(cal.getTime(),Calendar.getInstance().getTime(),user) == 0 ) {
                user.setAdress(null);
                user.setCni(null);
                user.setCodePostal(null);
                user.setCommune(null);
                user.setFirstName(null);
                user.setLastName(null);
                user.setMail(null);
                user.setType(null);
                userRequestRepository.save(user);
            }
            
            for (ObjectRequest objectRequest : informationRequest.getObjectsRequest()) {
                String prop = objectRequest.getProprietaire();
                if( !prop.startsWith("{HASH}") ) {
                    String hash = DatatypeConverter.printHexBinary(md.digest(prop.getBytes("UTF-8"))).toUpperCase();
                    objectRequest.setProprietaire("{HASH}"+hash);
                    objectRequestRepository.save(objectRequest);
                }
            }
        }
    }
}
