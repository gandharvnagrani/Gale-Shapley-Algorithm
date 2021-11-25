import java.io.IOException;
import java.util.ArrayList;
public class SMPSolver {
	private ArrayList<Student> S = new ArrayList<Student>(); // suitors
    private ArrayList<School> R = new ArrayList<School>(); // receivers
    private double avgSuitorRegret; // average suitor regret
    private double avgReceiverRegret; // average receiver regret
    private double avgTotalRegret; // average total regret
    private boolean matchesExist; // whether matches exist

    // constructors
    public SMPSolver(){
    	//initialize values
    	this.avgReceiverRegret = 0;
    	this.avgSuitorRegret = 0;
    	this.avgTotalRegret = 0;
    	this.matchesExist = false;
    }
    public SMPSolver(ArrayList<Student> S, ArrayList<School> R){
    	//intitialize constructor and add students to the SMPSolver object
    	for(int i = 0; i<S.size(); i++ ) {
    		this.S.add(S.get(i));
    	}
    	for(int i = 0; i<R.size(); i++ ) {
    		this.R.add(R.get(i));
    	}
    }

    // getters
    public double getAvgSuitorRegret(){ //get average Suitor regret
        return this.avgSuitorRegret;
    }

    public double getAvgReceiverRegret(){ //get avg receiver regret
        return this.avgReceiverRegret;
    }

    public double getAvgTotalRegret(){ //get average total regret
        return this.avgTotalRegret;
    }

    public boolean matchesExist(){ //check if matches exist
        return this.matchesExist;
    }

    // reset everything with new suitors and receivers
    public void reset(ArrayList<Student> S, ArrayList<School> R){
    	//reset matches that were made and re-initialize values
    	this.S = S;
    	for(int i = 0; i<S.size(); i++) {
    		S.get(i).setSchool(-1);
    	}
    	this.R = R;
    	for(int i = 0; i<R.size(); i++) {
    		R.get(i).setStudent(-1);
    	}
    	this.avgReceiverRegret = 0;
    	this.avgSuitorRegret = 0;
    	this.avgTotalRegret = 0;
    	this.matchesExist = false;
    }

    // methods for matching
    public boolean match() throws IOException{ // Gale-Shapely algorithm to match; students are suitors
    	//Gale Shapley algorithm
    	matchesExist = false;
    	reset(S,R);
    	if(matchingCanProceed()) { //match only if conditions for matching are valid
	    	while(!allMatched()) { //run loop till all suitors have a match
	    		for(int suitor = 0; suitor<S.size(); suitor++) { //loop for all students
	    			if(S.get(suitor).getSchool()==-1) {
	    				boolean bool = true; //for while loop
	    				int rank = 1; //rank for getting the next highly ranked receiver
	    				while(bool==true) {
	    					for(int receiver = 0; receiver<R.size(); receiver++) { // loop for all schools
	    						if(S.get(suitor).getRanking(receiver) == rank) { //if the school is the student's highly ranked school
	    							if(R.get(receiver).getStudent()!=-1) { //and if the school isn't already engaged
	        							if(R.get(receiver).getRanking(suitor) < R.get(receiver).getRanking(R.get(receiver).getStudent()-1)){ //and if the suitor is ranked better than the current assigned student
	        								S.get(suitor).setSchool(receiver+1); //assign school to student
	        								S.get(R.get(receiver).getStudent()-1).setSchool(-1); //free the previously assigned student
	        								R.get(receiver).setStudent(suitor+1); //assign student to school
	        								bool = false; //break out of while loop
	        								break;
	        							}
	        							else {
	        								rank+=1; //if suitor is preferred less than the receiver's current assigned student then increase rank by 1
	        								bool = true; //and continue loop
	        							}
	        						}
	        						else {
	        							makeEngagement(suitor,receiver); //engage suitor and receiver if receiver is free
	        							bool = false; //break out of for loop
	        							break;
	        						}
	        					}
	        				}
	    					
	    		    			}	
	        				}
	    				}
		    		
	    			}
	    	matchesExist = true; // matches will exist after this loop ends
	    	
    	}

		//System.out.print(S);
    	//System.out.print(R);
    	

    	return matchesExist;
    }
    

    private boolean makeProposal(int suitor, int receiver){ // suitor proposes
    	//check if suitor can propose
    	boolean proposal = false; //placeholder
    	if(this.R.get(receiver).getStudent() == -1) {
    		proposal = true; //suitor can propose if receiver isnt assigned to a suitor
    	}
		else if(this.R.get(receiver).getRanking(suitor) < this.R.get(receiver).getRanking(this.R.get(receiver).getStudent()-1)) {
    		proposal = true; //suitor can also propose if it's preferred over the current assigned student
    	}
		
    	
    	return proposal;
    }

    private void makeEngagement(int suitor, int receiver){ // make engagement between suitor and receiver
    	S.get(suitor).setSchool(receiver+1);
		R.get(receiver).setStudent(suitor+1);
    	
    }

    public boolean matchingCanProceed(){ // check that matching rules are satisfied
    	boolean matchingCanProceed = true;
    	int nStudents = S.size();
    	int nSchools = R.size();
    	if(this.S==null) {
    		System.out.print("\nERROR: No suitors are loaded!\n\n"); //cant match if no students
			matchingCanProceed = false;
			return matchingCanProceed;
    	}
    	if(this.R==null) {
    		System.out.print("\nERROR: No receivers are loaded!\n\n"); //cant match if no schools
			matchingCanProceed = false;	
			return matchingCanProceed;
    	}
    	if(nStudents == 0 || nSchools == 0) {
			if(nStudents == 0) {
				System.out.print("\nERROR: No suitors are loaded!\n\n"); //cant match if no students
				matchingCanProceed = false;
				return matchingCanProceed;
			}
			if(nSchools == 0) {
				System.out.print("\nERROR: No receivers are loaded!\n\n"); //cant match if no schools
				matchingCanProceed = false;	
				return matchingCanProceed;
			}
		}
		
    	else if(nStudents != nSchools) {
				System.out.print("\nERROR: The number of suitors and receivers must be equal!\n\n"); //cant match if unequal number of students and schools
				matchingCanProceed = false;
				return matchingCanProceed;
			}
		
		return matchingCanProceed;
    }
    
    public void calcRegrets(){ // calculate regrets
    	double studentRegret=0;
		double schoolRegret=0;
		//calculate student regret
		for(int i = 0; i<S.size(); i++) {
			int index = S.get(i).getSchool()-1;
			S.get(i).setRegret(S.get(i).getRanking(index)-1);
			studentRegret+=S.get(i).getRegret();
		}
		
		//calculate school regret
		for(int i = 0; i<R.size(); i++) {
			int index = R.get(i).getStudent()-1;
			R.get(i).setRegret(R.get(i).getRanking(index)-1);
			schoolRegret+=R.get(i).getRegret();
			
		}
		avgSuitorRegret = studentRegret/S.size();
		avgReceiverRegret = schoolRegret/R.size();
		avgTotalRegret = (avgSuitorRegret + avgReceiverRegret)/2;
    }
    
    public boolean isStable(){ // check if a matching is stable
    	boolean stable = true;
    	int nStudents = S.size();
    	for(int i = 0; i<nStudents; i++) {
			if(S.get(i).getRegret() != 0) {
				int assignedSchool = S.get(i).getSchool();
				int rankOfAssignedSchool = S.get(i).getRanking(assignedSchool-1);
				for(int k = 1; k<rankOfAssignedSchool; k++ ) {
					for(int j = 0; j<S.size(); j++) {
						if( k == S.get(i).getRanking(j)) {
							int assignedStudent = R.get(j).getStudent();
							if(R.get(j).getRanking(assignedStudent-1) > R.get(j).getRanking(i)) {
								stable = false;
								break;
							}
							break;
						}
					}
				}
			
			}
		}
        return stable;
    }
    
    // print methods
    public void print() throws IOException{ // print the matching results and stats
        printMatches();
        printStats();
    }
    
    public void printMatches() throws IOException{ // print matches
    	System.out.print("\nMatches:\n" + "--------\n"); //header
    	//display matches
		for(int i = 0; i<S.size(); i++) {
			System.out.print(R.get(i).getName() + ": " + S.get(R.get(i).getStudent()-1).getName() + "\n");	
		}
    }
    
    public void printStats(){ // print matching stats
    	if(isStable()) {
    		System.out.format("\nStable matching? "+"Yes"+"\n"+ "Average student regret: %.2f"  + "\n" + "Average school regret: %.2f"+ 
       			 "\n" + "Average total regret: %.2f" + "\n", avgSuitorRegret, avgReceiverRegret,avgTotalRegret);
       			System.out.println();
    	}
    	else {
    		System.out.format("\nStable matching? "+"No"+"\n"+ "Average student regret: %.2f"  + "\n" + "Average school regret: %.2f"+ 
          			 "\n" + "Average total regret: %.2f" + "\n", avgSuitorRegret, avgReceiverRegret,avgTotalRegret);
          			System.out.println();
       	}
    	
    }
    public boolean allMatched() {
    	//check if all suitors are matched
    	boolean allMatched = true;
    	for(int suitor = 0; suitor<S.size(); suitor++) {
    		if(S.get(suitor).getSchool()==-1) {
    			allMatched=false;
    			break;
    		}
    	}
    	return allMatched;
    }
}
