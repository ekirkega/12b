//--------------------------------------------------------
//Emma Kirkegaard
//ekirkega
//12 B
//7 August 2018
//Simulation.java
//simulation of processors handling queues of jobs
//--------------------------------------------------------

import java.io.*;
import java.util.Scanner;




public class Simulation{

	public static void main(String[] args) throws IOException{

		Scanner in = null;
		PrintWriter report = null;
		PrintWriter trace = null;
		Queue jobQ = new Queue();
		Queue jobF = new Queue();
		Queue[] finishedJobs;
		int numJobs, maxWait, totalWait;
		double avgWait;
		int wait[];

		if(args.length != 1){
			System.out.println("Usage: Simulation <input file>");
			System.exit(1);
		}

		in = new Scanner(new File(args[0]));
		report = new PrintWriter(new FileWriter(args[0] + ".rpt"));
		trace = new PrintWriter(new FileWriter(args[0] + ".trc"));
		
		
		//get number of jobs
		String nj = in.nextLine();
		numJobs = Integer.parseInt(nj);

		//load each job into an initial queue
		for(int i = 0; i < numJobs; i++){
			jobQ.enqueue(getJob(in));
		}

		in.close();

		finishedJobs = new Queue[numJobs];

		report.println("Report file: " + args[0] + ".rpt");
		report.println(numJobs + " Jobs:");
		report.println(jobQ.toString() + "\n");
		report.println("***********************************************************");
		
		trace.println("Trace file: " + args[0] + ".trc");
		trace.println(numJobs + " Jobs:");
		trace.println(jobQ.toString());
		

		for(int p = 1; p < numJobs; p++){ //main loop for 1 - m-1 processors

			int time = 0;
			avgWait = 0; 
			totalWait = 0; 
			maxWait = 0;



			Queue[] processorQ = new Queue[p+1];//array of processor queueus 
			//fill array with queues
			for(int i = 0; i <= p; i++){
				processorQ[i] = new Queue();
			}
			
			if(!processorQ[0].isEmpty()){//make sure the job copy array is empty
				processorQ[0].dequeueAll();
			}

			if(!jobF.isEmpty()){
				jobF.dequeueAll();
			}
			
			for(int i = 0; i < numJobs; i++){//copies jobs into job copy queue
				Job J = (Job)jobQ.dequeue();
				J.resetFinishTime();
				jobQ.enqueue(J);//(circular)by end of processing first job will be head again
				processorQ[0].enqueue(J);
			}


			trace.println("\n*****************************");
			if(p == 1){
				trace.println(p + " processor:");
			}else{
				trace.println(p + " processors:");
			}
			trace.println("*****************************");
			trace.println("time="+ time);
					for(int i = 0; i <= p; i++){
						trace.println((i) + ": " + processorQ[i].toString());
					}

			time = 1;//start checking for arrival times

			while( jobF.length() < numJobs ){
				Job A = null;
				Job D = null;
				int a = 0, d = -2;
				boolean tp = false;

				for(int i = 1; i <= p; i++){//dequeue finished jobs
					if(!processorQ[i].isEmpty()){
						D = (Job)processorQ[i].peek();
						if(D.getFinish() == -1){
							D.computeFinishTime(time);//job is at front of processor queue
							d = D.getFinish();
						}else{
							d = D.getFinish();
						}
						if(d == time){
							tp = true;
							D = (Job)processorQ[i].dequeue();
							if(!processorQ[i].isEmpty()){
								Job N = (Job)processorQ[i].peek();
								N.computeFinishTime(time);
							}
							processorQ[0].enqueue(D);
							jobF.enqueue(D);

						}
					}
					
				}

				while( !processorQ[0].isEmpty() && ((Job)processorQ[0].peek()).getArrival() <= time && ((Job)processorQ[0].peek()).getFinish() == -1){
					A = (Job)processorQ[0].peek();
					a = A.getArrival();
					if(a == time){
						tp = true;
						//find the shortest queue
						int m = minQ(processorQ, 1, processorQ.length-1);
						//enqueue into shortest queue
						Job J = (Job)processorQ[0].dequeue();
						processorQ[m].enqueue(J);
					}
				}

				for(int i = 1; i <= p; i++){//dequeue finished jobs
					if(!processorQ[i].isEmpty()){
						D = (Job)processorQ[i].peek();
						if(D.getFinish() == -1){
							D.computeFinishTime(time);//job is at front of processor queue
							d = D.getFinish();
						}else{
							d = D.getFinish();
						}
						if(d == time){
							tp = true;
							D = (Job)processorQ[i].dequeue();
							if(!processorQ[i].isEmpty()){
								Job N = (Job)processorQ[i].peek();
								N.computeFinishTime(time);
							}
							processorQ[0].enqueue(D);
							jobF.enqueue(D);

						}
					}
					
				}


				if(tp){//trace print stuff
					trace.println("\ntime="+ time);
					for(int i = 0; i <= p; i++){
						trace.println((i) + ": " + processorQ[i].toString());
					}
				}

				time++;

			}//end while

			//report stuff
			wait = new int[numJobs];
			for(int i = 0; i < numJobs; i ++){// array is filled with each jobs wait time
				while(!jobF.isEmpty()){
					Job F = (Job)jobF.dequeue();
					wait[i] = F.getWaitTime();
					totalWait += wait[i];
					if(wait[i] > maxWait){
						maxWait = wait[i];
					}
				}
			}
			avgWait = totalWait / (double)numJobs;
			String averageWait = String.format("%.2f", avgWait);

			if(p == 1){
				report.print(p + " processor");
			}else{
				report.print(p + " processors");
			}
			report.print(": totalWait=" + totalWait + ", maxWait=" + maxWait + ", averageWait="+ averageWait + "\n");

		}//end processor num loop


		trace.close();
		report.close();
	}//end main

	public static Job getJob(Scanner in) {
      String[] s = in.nextLine().split(" ");
      int a = Integer.parseInt(s[0]);
      int d = Integer.parseInt(s[1]);
      return new Job(a, d);
    }


	static int minQ(Queue[] X, int p, int r){//returns index of processor queue with min length
		int q = (p+r)/2;
		int f = 0, l = 0;
		if(p < r){
			f = minQ(X, p, q); 
			l = minQ(X, q+1, r);
		}else if(p == r){
			return p;
		}
		if (X[f].length() > X[l].length()){
				return l;
		}else {
			return f;
		}
	}



}
