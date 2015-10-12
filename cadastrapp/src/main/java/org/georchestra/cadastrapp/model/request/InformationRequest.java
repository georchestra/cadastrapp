package org.georchestra.cadastrapp.model.request;

import java.util.Date;

public class InformationRequest {
	

	private long requestId;
	private long userId;
	private Date requestDate;
	private String parcelleId;
	
	public InformationRequest(){}
	
	public InformationRequest(int userId, String parcelleId){
		this.userId = userId;
		this.parcelleId = parcelleId;
	}
	
	public String toString(){
		return String.format("Information Request : id=%d, user=%d, parcelle='%s'", requestId, userId, parcelleId);
	}

}
