package osProcessorHW;

public class SimProcess {

	private int pid;
	private String pidName;
	private int totalInst; 
	
	
	//constructor
	public SimProcess(int pid, String pidName, int totalInst){
		this.pid =pid;
		this.pidName = pidName;
		this.totalInst = totalInst;
	}
	
	public int getpid() {
		return pid;
	}
	
	public ProcessState execute(int i) {
		System.out.println("Proccess num: " + pid  + 
					" Process Name: " + pidName + 
					" Instruction num: " + (i+1));
		if(i>= totalInst) {
			return ProcessState.FINISHED;
		}
		else if(Math.random() < .15){
			return ProcessState.BLOCKED;
		}
		else  {
			return ProcessState.READY;
		}
		
	}
	
	
}
