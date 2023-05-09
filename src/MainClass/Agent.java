package MainClass;

import java.util.HashMap;

public class Agent implements Runnable {

	private int id;
	private int sumOfAgent;
	private int domainSize;
	private Mailer mailer;
	private HashMap<VarTuple, ConsTable> constraints;
	
	public Agent(int id,int sumOfAgent,int domainSize, Mailer mailer, HashMap<VarTuple, ConsTable> constraints) {
		this.id = id;
		this.sumOfAgent = sumOfAgent;
		this.mailer = mailer;
		this.constraints = constraints;
		this.domainSize = domainSize;
	}

	@Override
	public void run() {
		
		// Counters for the massages
		int counterAssignmentMSG = 0;
		int counterFinishMSG = 0;
		
		// Counters for the success
		int counterSucsses = 0;
		int sumCounterSucsses = 0;
		
		// Get the assignment
		int assignment  = (int)(Math.random()*domainSize);
		
		// Send a massages to the neighbors
		Message sentItems = new Message(id,assignment);
		for(VarTuple i : constraints.keySet()) {
			if(i.getI() != id) {
				mailer.send(i.getI(), sentItems);
			}else {
				mailer.send(i.getJ(), sentItems);
			}
		}

		while(true) {
			// Read a massage
			Message inbox = mailer.readOne(id);
			if(inbox != null) {
				// Check the type of the massage (assignment or finish)
				if(inbox.getCounterSucsses() < 0) {
					// Find the right tuple and check the constraints via location of the id (i is left) also update the counterSucsses accordingly
					for(VarTuple i : constraints.keySet()) {
						if(i.getI() == inbox.getSenderId()) { 
							if(constraints.get(i).getTable()[inbox.getSenderAssignment()][assignment]) {
								counterSucsses++;
							}
						} else if(i.getJ() == inbox.getSenderId()) {
							if(constraints.get(i).getTable()[assignment][inbox.getSenderAssignment()]) {
								counterSucsses++;
							}
						} 		
					}
					// Update the counter for new assignment massage coming in
					counterAssignmentMSG++;
				} else {
					// update the counter for another agent is finish
					sumCounterSucsses += inbox.getCounterSucsses();
					counterFinishMSG++;
				}
			}
			// if you have all the assignments, print and send the finish massage
			if(counterAssignmentMSG == constraints.size()) {
				// counterSucsses also use here as "flag" for the last agent to send the finish massage only once
				if(counterSucsses > -1) {
					inbox = new Message(id,assignment,counterSucsses);
					System.out.println(inbox);
					mailer.send(sumOfAgent-1, inbox);
					counterSucsses = -1;
				}
				if (id != sumOfAgent-1) {
					break;
				}			
			}
			// if all agent are done sending print the sum of successes
			if(counterFinishMSG == sumOfAgent){
				System.out.println("total number of constraint checks: " + sumCounterSucsses);
				break;
			}
		}
	}
}

