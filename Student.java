import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Student {
	private String name;        // name
    private double GPA;         // GPA
    private int ES;             // extracurricular score
    private int[] rankings;     // rankings of schools
    private int school;         // index of matched school
    private int regret;         // regret
    public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
   
    // constructors
    public Student(){
    	String name;
        double GPA;
        int ES;
        int nSchools;
        int[] rankings;
        int school=-1;
        int regret;
      
    }
    public Student(String name, double GPA, int ES, int nSchools){
    	this.name = name;
    	this.GPA = GPA;
    	this.ES = ES;
    	this.rankings = new int[nSchools];
    	
    }

    // getters
    public String getName(){
		return this.name;
    }
    public double getGPA(){
        return this.GPA;
    }
    public int getES(){
        return this.ES;
    }
    public int getRanking(int i){
        return this.rankings[i];
    }
    public int getSchool(){
        return this.school;
    }
    public int getRegret(){
        return this.regret;
    }
    public int getRankingsLength(){
        return this.rankings.length;
    }

    // setters
    public void setName(String name){
    	this.name = name;
    }
    public void setGPA(double GPA){
    	this.GPA= GPA;
    }
    public void setES(int ES){
    	this.ES = ES;
    }
    public void setRanking(int i, int r){
    	this.rankings[i] = r;
    }
    public void setSchool(int i){
    	this.school = i;
    }
    public void setRegret(int r){
    	this.regret = r;
    }
    public void setNSchools(int n){ // set rankings array size
    	this.rankings = new int[n];
    }

    // find school ranking based on school ID
    public int findRankingByID(int ind){
        return ind;
    }

    // get new info from the user
    public void editInfo(ArrayList<School> H, boolean canEditRankings) throws IOException{
    	System.out.print("\nName: ");
		this.setName(Pro4_nagranig.cin.readLine());
		this.setGPA(Pro4_nagranig.getDouble("GPA: ", 0.0, 4.0));
		this.setES(Pro4_nagranig.getInteger("Extracurricular score: ", 0, 5)); 
		//if rankings can be edited, ask user if they want to edit rankings
		if (canEditRankings) {
			boolean bool = true;
			String choice = "";
			while(bool) {
				choice = Pro4_nagranig.getString("Edit rankings (y/n): \n");
				if (choice.toUpperCase().equals("Y")){
					editRankings(H); //edit rankings if user says yes
					break; //break loop once rankings have been changed
				}
				if (choice.toUpperCase().equals("N")) {
					canEditRankings = false;//cant edit rankings if user said no
					System.out.println();
					break; //break loop once user said no
				}
				else {
					System.out.println("ERROR: Choice must be 'y' or 'n'!"); //error if invalid choice
				}
			}
		}
		else {
			System.out.println();
		}
			
    }

    // edit rankings
    public void editRankings(ArrayList<School> H) throws IOException{
    	//reset rankings
    	this.setNSchools(H.size());
    	for (int i = 0; i < this.rankings.length; i++) {
			this.rankings[i] = 0;
    	}

		System.out.println("Student "+this.getName()+"'s rankings:");
		//Below code creates an arraylist for entered rankings and checks for duplicates
		//repeatedly asks user for input until valid is entered
		//see AssignRankings
		ArrayList<Integer> rank = new ArrayList<Integer>();
		for(int j=0; j<H.size(); j++) {
			int r=Pro4_nagranig.getInteger("School "+H.get(j).getName()+": ",1,H.size());
			if(rank.contains(r)) {
				while(rank.contains(r)) {
					System.out.print("ERROR: Rank " + r + " already used!\n\n");
					r=Pro4_nagranig.getInteger("School "+H.get(j).getName()+": ",1,H.size());
					}
					rank.add(r);
					this.setRanking(j, r);
				}
			else {
					rank.add(r);
					this.setRanking(j, r);
			}
		}
		System.out.println();
		
	}
   
    // print student info and assigned school in tabular format
    public void print(ArrayList<School> H, boolean rankingsSet) throws IOException{
    	//format table based on solution program's output
    	System.out.format("%-27s", this.getName());
		System.out.format("    %.2f", this.getGPA());
		System.out.format("%4d",this.getES());
		if (this.school == -1) {
			System.out.print("  -                          ");
		}
		if (this.school != -1) {
			//int l = H.get(this.school-1).getName().length(); //get length of school name
			System.out.format("  %-27s", H.get(this.getSchool()-1).getName());
			//System.out.print("  "+H.get(this.school-1).getName()+String.format("%1$"+(27-l)+"s", ""));
		}
		
		if (rankingsSet) {
			this.printRankings(H); //print rankings if rankings have previously been set
		 }
		else System.out.print("-\n"); //print dash if rankings have not been set
    }


    public void printRankings(ArrayList<School> H) throws IOException{
    	for (int rank = 1; rank <= H.size(); rank++){ //rank starts at 1
			for (int i = 0; i < H.size(); i++){ 
				if (rank == this.getRanking(i)){//if rank at index = rank, the required school has been found
					System.out.print(H.get(i).getName());//print school name
				if (rank != H.size())
					System.out.print(", ");//separate by comma if iteration isnt over
				else System.out.println();
				}
			}
		}
    }
    public boolean isValid() {
    	boolean validStudent = true;
    	if(this.GPA >= 0.0 && this.GPA <= 4.0) {
    		if(this.ES>=0 && this.ES<=5) {
    			validStudent = true;
    		}
    	}
    	return validStudent;
    }
    
}
