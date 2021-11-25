import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class School {
	private String name;        // name
    private double alpha;       // GPA weight
    private int[] rankings = new int[2000];     // rankings of schools
    private int student;        // index of matched student
    private int regret;         // regret
    public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
    // constructors
    public School(){
    	String name;
    	double alpha;
    	int[] rankings;
    	int student;
    	int regret;
    }
    public School(String name, double alpha, int nStudents){
    	this.name = name;
    	this.alpha = alpha;
    	this.rankings = new int[nStudents];
    }

    // getters
    public String getName() throws IOException{
		return this.name;
    }
    public double getAlpha(){
        return this.alpha;
    }
    public int getRanking(int i){
        return this.rankings[i];
    }
    public int getStudent(){
        return this.student;
    }
    public int getRegret (){
        return this.regret;
    }

    // setters
    public void setName(String name){
    	this.name = name;
    }
    public void setAlpha(double alpha){
    	this.alpha = alpha;
    }
    public void setRanking(int i, int r){
    	this.rankings[i] = r;
    }
    public void setStudent(int i){
    	this.student = i;
    }
    public void setRegret(int r){
    	this.regret = r;
    }
    public void setNStudents(int n){ // set rankings array size
    	this.rankings = new int[n];
    }

    // find student ranking based on school ID
    public int findRankingByID(int ind){
        return ind;
    }

    // get new info from the user
    public void editInfo(ArrayList<Student> S, boolean canEditRankings) throws IOException{
    	
    	System.out.print("\nName: ");
		this.setName(Pro4_nagranig.cin.readLine());
		this.setAlpha(Pro4_nagranig.getDouble("GPA weight: ",0.0, 1.0));
		if(canEditRankings){
			//if rankings can be edited, calculate them
			this.setNStudents(S.size());
			for (int i = 0; i < S.size(); i++) {
				rankings[i] = 0;
			}
			this.calcRankings(S);
			
		}
		else {
			return;
		}
		//System.out.println();
    }

	// calculate rankings based on weight alpha
    public void calcRankings(ArrayList<Student> S){
    	//reset rankings before calculating them
    	for(int s = 0; s<S.size();s++) {
    		this.setNStudents(S.size());
    		rankings[s] = 0;
    	}
    	double[] scores = new double[S.size()]; //initialize array for scores
	    	for (int i = 0; i < S.size(); i++){
	    		//compositeScore variable
	    		double compositeScore = (S.get(i).getGPA()*this.getAlpha()) + ((1 - this.getAlpha())*S.get(i).getES());
				scores[i] = compositeScore; //add score to list
		}
		
		Double[] sorted = new Double[scores.length]; //initialize a sorted array
		for (int i = 0; i < scores.length; i++){
			sorted[i] = scores[i]; //add all elements of scores array to this
		}
		
		Arrays.sort(sorted, Collections.reverseOrder());//sort in descending order
		
		for (int i = 0; i <scores.length; i++){
				for (int j = 0; j < sorted.length; j++){
					if ((scores[j] == sorted[i])&&this.getRanking(j) == 0){ //check of score at index matches sorted score at its index and if rankings have not already been assigned 
						this.rankings[j] = i+1;//set ranking
						break;//break once assigned
					}
				}
		}
    }

    // print school info and assigned student in tabular format
    public void print(ArrayList<Student> S, boolean rankingsSet) throws IOException{
    	//print school table
    	System.out.format("%-27s", this.getName());
		System.out.format("     %.2f", this.getAlpha());
		
		if (this.student == -1) {
			System.out.print("  -                          ");
		}
		if (this.student != -1) {
			//int l = S.get(this.student-1).getName().length(); //get name length of assigned student to align the table
			System.out.format("  %-27s", S.get(this.getStudent()-1).getName());
			///System.out.print("  "+S.get(this.student-1).getName()+String.format("%1$"+(27-l)+"s", ""));
		}

		if (rankingsSet) {
			this.printRankings(S); //print rankings if they have been assigned
		 }
		else System.out.print("-\n"); //dash otherwise
    }

    public void printRankings(ArrayList<Student> S){
    	for (int rank = 1; rank <= S.size(); rank++){ //rank starts at 1
			for (int i = 0; i < S.size(); i++){ 
				if (rank == rankings[i]){//if rank at index = rank, the required school has been found
					System.out.print(S.get(i).getName());//print student name
				if (rank != S.size())
					System.out.print(", "); //separate by comma if iteration isnt over
				else System.out.println();
				}
			}
		}
    }
    public boolean isValid() {
    	boolean validSchool = false;
    	if(this.alpha>=0 && this.alpha <= 1) {
    		validSchool = true;
    	}
    	return validSchool;
    }
	
    
}
