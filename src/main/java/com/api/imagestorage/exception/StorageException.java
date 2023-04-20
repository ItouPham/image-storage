package com.api.imagestorage.exception;

public class StorageException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1957664191531576084L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Exception e) {
        super(message, e);
    }
}
