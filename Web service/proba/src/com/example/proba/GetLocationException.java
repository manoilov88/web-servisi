package com.example.proba;

public class GetLocationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetLocationException() {
		try {
			throw new Exception("Error in getting location!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GetLocationException(String message) {
		try {
			throw new Exception(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
