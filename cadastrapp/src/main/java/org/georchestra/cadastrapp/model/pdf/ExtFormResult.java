package org.georchestra.cadastrapp.model.pdf;

public class ExtFormResult {

	private boolean success = false;
	
	private Object data = null;
	

	public ExtFormResult(boolean success, Object data) {
		this.success = success;
		this.data = data;
	}


	public boolean getSuccess() {
		return success;
	}


	public Object getData() {
		return data;
	}
}
