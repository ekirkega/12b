//--------------------------------------------------------------
//ekirkega
//Emma Kirkegaard
//12 M
//7 August 2018
//DuplicateKeyException.java
//throws an exception if Queue is empty
//--------------------------------------------------------------

public class QueueEmptyException extends RuntimeException{
	public QueueEmptyException(String message){
		super(message);
	}
}