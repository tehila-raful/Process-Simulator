package osProcessorHW;
import java.util.ArrayList;

public class mainprocess {

	public static void main(String[] args) {
		
		SimProcessor simulator = new SimProcessor(); //processor object 
		
		//create ArrayLists 
		ArrayList<ProcessControlBlock>blockedArray = new ArrayList<ProcessControlBlock>();
		ArrayList<ProcessControlBlock>pcbArray = new ArrayList<ProcessControlBlock>();
		
		//create 10 different processes
		SimProcess process1 = new SimProcess(1,"proc1", 120);
		pcbArray.add(new ProcessControlBlock(process1));
		
		SimProcess process2 = new SimProcess(2,"proc2", 176);
		pcbArray.add(new ProcessControlBlock(process2));
		
		SimProcess process3 = new SimProcess(3,"proc3", 205);
		pcbArray.add(new ProcessControlBlock(process3));
		
		SimProcess process4 = new SimProcess(4,"proc4", 100);
		pcbArray.add(new ProcessControlBlock(process4));
		
		SimProcess process5 = new SimProcess(5,"proc5", 310);
		pcbArray.add(new ProcessControlBlock(process5));
		
		SimProcess process6 = new SimProcess(6,"proc6", 235);
		pcbArray.add(new ProcessControlBlock(process6));
		
		SimProcess process7 = new SimProcess(7,"proc7", 112);
		pcbArray.add(new ProcessControlBlock(process7));
		
		SimProcess process8 = new SimProcess(8,"proc8", 387);
		pcbArray.add(new ProcessControlBlock(process8));
		
		SimProcess process9 = new SimProcess(9,"proc9", 102);
		pcbArray.add(new ProcessControlBlock(process9));
		
		SimProcess process10 = new SimProcess(10,"proc10", 311);
		pcbArray.add(new ProcessControlBlock(process10));
		
		final int QUANTUM = 5;
	
		//set current process to 1 just to start
		simulator.setCurrentPro(pcbArray.get(0).getCurrentPro());

		int adder=0; //for quantum 
		
		for(int j=0;j<=3000; j++) {
			
			//get the instructions 
			System.out.print("Step " + (j +1) + " ");
			ProcessState result = simulator.executeNextInstruction();
			adder++; //increase increment for each instruction 
			
			if(adder % QUANTUM == 0) {
				System.out.println("***Quantum Completed***");
				
				updatePCB(pcbArray, simulator);
				
				//display context switch
				displaySwitch(pcbArray, "Saving Process: ");
				
				//add to end of ready list again
				pcbArray.add(pcbArray.get(0)); 

				//remove from beginning of the list 
				if(!pcbArray.isEmpty()) {
					   pcbArray.remove(0);
					}
				
				//if it does hit empty wake it up 
				while(pcbArray.isEmpty()) {
					wakeUpCall(pcbArray, blockedArray);
				}
				
				//setting a new pcb on the processor  
				simulator.setCurrentPro(pcbArray.get(0).getCurrentPro());  
				simulator.setCurrInstruction(pcbArray.get(0).getCurrInstruction());
				
				//display context switch
				displaySwitch(pcbArray, "Restoring Process: ");
			}
			
			else if(result == ProcessState.BLOCKED) {
				System.out.println("***Process Blocked***");
				
				updatePCB(pcbArray, simulator);
				
				//display context switch
				displaySwitch(pcbArray, "Saving Process: ");
				
				
				blockedArray.add(pcbArray.get(0)); //put on blocked list 
				
				//remove current pbc from pcb array 
				if(!pcbArray.isEmpty()) {
				   pcbArray.remove(0);
				}
				
				adder = 0; //increment back to zero 
				
				//if it does hit empty fill it 
				while(pcbArray.isEmpty()) {
					wakeUpCall(pcbArray, blockedArray);
				}

				//setting a new pcb on the processor  
				simulator.setCurrentPro(pcbArray.get(0).getCurrentPro());
				simulator.setCurrInstruction(pcbArray.get(0).getCurrInstruction());
				
				//display context switch
				displaySwitch(pcbArray, "Restoring Process: ");
			}
					
			else if(result == ProcessState.FINISHED) {
				System.out.println("***Process Finished***");
				
				updatePCB(pcbArray, simulator);
				
				//display context switch
				displaySwitch(pcbArray, "Saving Process: ");
				
				//remove from pcb list 
				if(!pcbArray.isEmpty()) {
					   pcbArray.remove(0);
					}
				adder = 0;
				
				//if it does hit empty fill it 
				while(pcbArray.isEmpty()) {
					wakeUpCall(pcbArray, blockedArray);
				}
				
				//setting a new pcb on the processor  
				simulator.setCurrentPro(pcbArray.get(0).getCurrentPro());
				simulator.setCurrInstruction(pcbArray.get(0).getCurrInstruction());
				
				//display context switch
				displaySwitch(pcbArray, "Restoring Process: ");
			}
			
			//loop through blocked processes and wake them up by 30% prob
			wakeUpCall(pcbArray, blockedArray);
		}
	}
	
	public static void displaySwitch(ArrayList<ProcessControlBlock> pcbArray, String str) {
		
		System.out.println("Context Switch: " +str  + pcbArray.get(0).getCurrentPro().getpid());
		System.out.println("\tInstruction:" + pcbArray.get(0).getCurrInstruction() 
							+"= R1: " + pcbArray.get(0).getRegister1()+ " R2: " + pcbArray.get(0).getRegister2()+ 
							" R3: " + pcbArray.get(0).getRegister3()+ " R4: " + pcbArray.get(0).getRegister1());
	}
	
	public static void wakeUpCall(ArrayList<ProcessControlBlock> pcbArray, ArrayList<ProcessControlBlock> blockedArray) {
		//loop through blocked processes and wake them up by 30% prob
		for(int i =0; i< blockedArray.size(); i++) {
			if(Math.random() > .70) {
				pcbArray.add(blockedArray.get(i));
				blockedArray.remove(i);
			}
		}

	}

	public static void updatePCB(ArrayList<ProcessControlBlock> pcbArray, SimProcessor simulator) {
		//update pcb registers 
		pcbArray.get(0).setRegister1(simulator.getRegister1());
		pcbArray.get(0).setRegister2(simulator.getRegister2());
		pcbArray.get(0).setRegister3(simulator.getRegister3());
		pcbArray.get(0).setRegister4(simulator.getRegister4());
		
		//save current inst
		pcbArray.get(0).setCurrInstruction(simulator.getCurrInstruction());
		
	}

}

