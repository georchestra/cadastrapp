package org.georchestra.cadastrapp.service.exception;

/**
 *  Cadastrapp exception create to handle all server error and return empty data to client.
 */
public class CadastrappServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8241651815784429335L;
	
	public CadastrappServiceException () {

    }

    public CadastrappServiceException (String message) {
        super (message);
    }

    public CadastrappServiceException (Throwable cause) {
        super (cause);
    }

    public CadastrappServiceException (String message, Throwable cause) {
        super (message, cause);
    }

}
