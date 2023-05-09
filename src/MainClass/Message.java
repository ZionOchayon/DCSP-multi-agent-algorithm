package MainClass;

 // Messages communicate sending messages to each other
public class Message {
	
	private int senderId;
	private int senderAssignment;
	private int counterSuccess = -1;
	
	// Assignment messages
	public Message(int senderId,int senderAssignment) {
		this.senderId = senderId;
		this.senderAssignment = senderAssignment; 
	}
	
	// Finish messages
	public Message(int senderId,int senderAssignment,int counterSuccess) {
		this.senderId = senderId;
		this.senderAssignment = senderAssignment; 
		this.counterSuccess = counterSuccess;
	}

	public int getSenderId() {
		return senderId;
	}
	
	public int getSenderAssignment() {
		return senderAssignment;
	}

	public int getCounterSucsses() {
		return counterSuccess;
	}

	@Override
	public String toString() {
		return "ID: " + senderId + ", assignment: " + senderAssignment + ", successful constraint checks: " + counterSuccess;
	}
}
