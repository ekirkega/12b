//--------------------------------------------------------
//Emma Kirkegaard
//ekirkega
//12 B
//QueueTest.java
//Queue 
//--------------------------------------------------------

public class QueueTest{

	public static void main(String[] args){
		int l = 25;
		Queue Q = new Queue();

		if(Q.isEmpty()){System.out.println("Queue empty");}
		

		// Job A = new Job(1, 2);
		// Job B = new Job(2, 3);

		// String w = A.toString();
		// System.out.println(w);

		// Q.enqueue(A);

		// int s = Q.length();
		// System.out.println(s);

		// Q.enqueue(B);

		for(int i = 0; i < l; i++){
			Job J = new Job(i, i);
			Q.enqueue(J);
		}

		if(!Q.isEmpty()){System.out.println("Queue not empty");}
		

		String q = Q.toString();
		System.out.println(q);

		Job A;
		A = (Job)Q.peek();
		System.out.println(A.toString());

		Queue B = new Queue();
		B.enqueue(A);
		B.toString();

		// Q.dequeue();

		// System.out.println(Q.toString());

		for(int i = 0; i < l - 10; i++){
			Q.dequeue();
		}

		Q.dequeueAll();
		if(Q.isEmpty()){System.out.println("Queue empty");}

		System.out.println(Q.toString());
	}
}