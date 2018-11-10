//--------------------------------------------------------
//Emma Kirkegaard
//ekirkega
//12 B
//Queue.java
//Queue of objects 
//--------------------------------------------------------

public class Queue implements QueueInterface{

	private class Node{
		Object job;
		Node next;

		public Node(Object Job){
			job = Job;
			next = null;
		}
	}

	private Node head;
	private Node tail;
	private int numItems;

	public Queue(){
		head = null;
		tail = null;
		numItems = 0;
	}

	public boolean isEmpty(){
		if(numItems == 0){
			return true;
		}else{
			return false;
		}
	}

	public int length(){
		return numItems;
	}

	public void enqueue(Object Job){
		if(numItems == 0){
			Node N = new Node(Job);
			head = N;
			tail = N;
		}else{
			Node N = tail;
			Node T = new Node(Job);
			N.next = T;
			tail = T;
		}
		numItems++;
	}

	public Object dequeue() throws QueueEmptyException{
		if (numItems == 0){
			throw new QueueEmptyException("cannot dequeue from empty queue");
		}
		Node N = head;
		Node H = N.next;
		head = H;
		numItems--;
		return N.job;


	}

	public Object peek() throws QueueEmptyException{
		if(numItems == 0){
			throw new QueueEmptyException("cannot peek from empty queue");
		}else{
			Node H = head;
			return H.job;
		}
	}

	public void dequeueAll() throws QueueEmptyException{
		if(numItems == 0){
			throw new QueueEmptyException("cannot dequeueAll from empty queue");
		}else{
			head = null;
			tail = null;
			numItems = 0;
		}
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();

		for(Node N = head; N != null; N = N.next){
			sb.append((N.job).toString()).append(" ");
		}
		return new String(sb);
	}

}//end class