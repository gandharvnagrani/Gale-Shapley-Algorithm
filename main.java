import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Pro4_nagranig {
	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in)); //object used to get user input
	//BufferedReader fin = new BufferedReader(new FileReader(filename));

	public static void main(String[] args) throws IOException {
		int nStudents = 0, nSchools = 0; //initialize variables for the number of schools and students
		ArrayList<Student> S = new ArrayList<Student>(); //create ArrayList of Student type
		ArrayList<School> H = new ArrayList<School>();//create ArrayList of School type
		SMPSolver match  = new SMPSolver(S,H);//create new SMPSolver object
		boolean matched = false; //assume matching has not taken place
		boolean bool = true; //boolean for while loop
		
		while (bool) {
			displayMenu(); //print display menu
			String choice = getString("\nEnter choice: "); //ask user for choice
			if (choice.toUpperCase().equals("L")) { 
				nSchools = loadSchools(H); //load schools first
				//if(nSchools>nStudents) {
					//S.clear();
			//	}
				nStudents = loadStudents(S,H); //then load students
				for(int i = 0; i<H.size(); i++) {
					H.get(i).calcRankings(S); //calculate student's rankings of students
				}
				//assigned schools and students take value -1
				for(int i = 0; i<S.size(); i++) {
		    		S.get(i).setSchool(-1);
		    	}
		    	for(int i = 0; i<H.size(); i++) {
		    		H.get(i).setStudent(-1);
		    	}
		    	matched = false;
			}
			
			else if (choice.toUpperCase().equals("E")) {
				editData(S,H);
				if(matched) {
					match.match();
					match.calcRegrets();
				}
			}
			else if (choice.toUpperCase().equals("P")) {
				//int nStudents = S.size();
				//int nSchools = H.size();
				if (nStudents == 0 || nSchools == 0) {
					//errors if there are no schools or students
					if(nStudents == 0 && nSchools == 0) {
						System.out.println("\nERROR: No students are loaded!\n\n" + "ERROR: No schools are loaded!\n" );
					} 
					else if(nStudents == 0) {
						System.out.print("\nERROR: No students are loaded!\n");
					}
					
					else if(nSchools==0) {
						if(nSchools == 0 && nStudents !=0) {
							System.out.println("\nERROR: No schools are loaded!\n");
						}
						else {
						System.out.print("ERROR: No schools are loaded!\n");
						}
					}
				}
				if(nStudents>0) {
					System.out.println("\nSTUDENTS:\n");
					printStudents(S,H); //print student table if there is more than one student
				}
				if(nSchools>0) {
					System.out.println("\nSCHOOLS:\n");
					//if(nStudents != nSchools) rankingsSet = false;
					printSchools(S,H); //print school table if there is more than one school
					System.out.println();
				}
				
			}
			else if (choice.toUpperCase().equals("M")) {
				match  = new SMPSolver(S,H); //match schools and students
	        	long elapsedTime=0;
	        	long start = System . currentTimeMillis (); //get start time
				matched=match.match();
				elapsedTime = System . currentTimeMillis () - start ; // Get elapsed time in ms
				if(matched) {
		    		match.calcRegrets(); //calculate all regrets if matchesExist
		    		match.printStats(); //print stats
		    		System.out.format("%d matches made in %dms!\n\n", S.size(), elapsedTime); //print no. of matches and how much time it took
			    	
				}
			
			}
			else if (choice.toUpperCase().equals("D")) {
				if(matched)
					match.print(); //only print stats if matching previously took place
				else {
					System.out.print("\nERROR: No matches exist!\n\n");
				}
			}
			else if(choice.toUpperCase().equals("R")) {
				// reset everything so far
				S.clear();
				H.clear();
			//	nSchools = 0;
			//	nStudents = 0;
				//rankingsSet = false;
				matched = false;
				System.out.println("\nDatabase cleared!\n");
			}
			else if(choice.toUpperCase().equals("Q")) {
				bool = false; //break loop if the user chooses to quit
			}
			else{
				System.out.print("\nERROR: Invalid menu choice!\n\n"); //error if anything other than the menu choices are entered
			}
			
		}
		System.out.println("\nArrivederci!"); //quit message
			
	}
			   
	public static void displayMenu() {
		//print display menu
		System.out.println("JAVA STABLE MARRIAGE PROBLEM v2\n");
	    System.out.println("L - Load students and schools from file");
	    System.out.println("E - Edit students and schools");
	    System.out.println("P - Print students and schools");
	    System.out.println("M - Match students and schools using Gale-Shapley algorithm");
	    System.out.println("D - Display matches and statistics");
	    System.out.println("R - Reset database");
	    System.out.println("Q - Quit");
	}
	public static int loadStudents(ArrayList<Student> S, ArrayList<School> H){
		Boolean isValid;
		Double gpa=0.0;
		Double es=0.0;
		String split[] = null; // array to add all lines
		int nStudents=0;
		int existing = S.size();
		do {
			isValid = true;
			//if user inputs 0
			String filename = getString("\nEnter student file name (0 to cancel): ");
			if(filename.equals("0")){
				System.out.println("\nFile loading process canceled.\n");
				if(H.size()>S.size()) {
					S.clear();
				}
				break;
			}
			try {
				BufferedReader student = new BufferedReader(new FileReader(filename)); //buffered reader object for reading student file
				Boolean bool=true; //boolean for while loop
				boolean clear = false;
				while(bool) {
					if(S.size()>0) {
						if(S.get(0).getRankingsLength()!=H.size()) {
							S.clear();
							clear = true;
						}
					}
					Boolean validStudent = false; //boolean to check if student is valid
					if((filename = student.readLine()) != null) { //check if line has data
						split = filename.split(","); //split at comma
						nStudents += 1; //add to total no of students
						
						if(split.length == 3 + H.size()) {
							gpa = Double.parseDouble(split[1]);
							es = Double.parseDouble(split[2]);
							if((gpa>=0.0&&gpa<=4.0)&&((es>=0 && es <=5) && es%1==0)) { //check for valid es
								validStudent = true;
							}
							else {validStudent = false;}
						}
						if(validStudent && split.length == 3 + H.size()) { 
							Student stu = new Student(split[0],gpa,es.intValue(),H.size()); //create student object
						
							Boolean validRank=true; //boolean to check if ranking is valid
							
							for(int i = 3; i<split.length; i++){ //check if ranking is valid
								Double schoolRank = Double.parseDouble(split[i]);
								if((schoolRank>=1 && schoolRank <= H.size()) && schoolRank%1==0) {
									validRank = true;
								}
								else {validRank = false;}
							}
							//check for repeated ranks
							if(validRank) {
								int [] ranks = new int[H.size()];
								for(int j = 3; j<split.length; j++) {
									ranks[j-3] = Integer.parseInt(split[j]);
								}
							
								for(int i = 0; i <ranks.length; i++) {
									for(int j = i+1; j<ranks.length; j++) {
										if(ranks[i]==ranks[j]) {
											validRank = false;
										}
									}
								}
							}
							//set student rankings if rankings were valid
							if(validRank) {
								stu.setNSchools(H.size());
								for(int i = 3,j=0;i<split.length; i++) {
									stu.setRanking(Integer.parseInt(split[i])-1,j+1);
									j++;
								}
								S.add(stu);
							}
						}
						
					}
					else {
						bool = false;
					}
					
				}
				student.close(); //close buffered reader
				if(clear) {
					System.out.format("\n%s of %s students loaded!\n\n", (S.size()), nStudents);
				}
				else {
					System.out.format("\n%s of %s students loaded!\n\n", (S.size()-existing), nStudents);
				}
				
			}
			//catch exceptions
			catch (FileNotFoundException e) { 
				System.out.println("\nERROR: File not found!");
				isValid = false;
			}
			catch (IOException e) {
				System.out.println("\nERROR: File not found!");
				isValid = false;
			}
				
		}while(!isValid);
		return S.size();
	}
	public static int loadSchools(ArrayList<School> H) throws IOException{
		Boolean isValid;
		String split[] = null; //array to store all lines
		int nSchools = 0;
		int existing = H.size(); //if schools already exist
		Boolean validSchool = true;
		do {
			//if user inputs 0
			isValid = true;
			String filename = getString("\nEnter school file name (0 to cancel): ");
			if(filename.equals("0")){
				System.out.println("\nFile loading process canceled.");
				break;
			}
			//File file = new File(filename);
			try {
				BufferedReader school = new BufferedReader(new FileReader(filename)); //buffered reader for reading school
				Boolean bool=true;
				while(bool) {
						if((filename = school.readLine()) != null) { //check if line has data
							split = filename.split(","); //split at comma
							School sch = new School(); //create school object
							sch.setName(split[0]); //name is first object in line
							Double alpha = Double.parseDouble(split[1]);
							if((alpha >= 0 && alpha <= 1)) { //check if gpa weight is valid
								validSchool = true;
								sch.setAlpha(alpha);// set alpha if valid
							}
							else {
								validSchool = false;
							}
							if(validSchool) {
								H.add(new School(sch.getName(),sch.getAlpha(),0)); // add only if school is valid
							}
							nSchools += 1; //increment number of schools
						}
						else {
							bool = false; //break out of loop if line has no data
						}
					
				}
				school.close(); //close school reader
				System.out.format("\n%s of %s schools loaded!\n", H.size()-existing, nSchools);
			}
			//check exceptions
			catch (FileNotFoundException e) {
				System.out.println("\nERROR: File not found!");
				isValid = false;
			}
			catch (IOException e) {
				System.out.println("\nERROR: File not found!");
				isValid = false;
			}
			
		}while(!isValid);
		return H.size();
	}
	

	public static void editData(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		int nStudents = S.size();
		int nSchools = H.size();
		String choice = ""; //initialize user's choice 
		boolean bool = true;
		while(bool){
			//display menu
			System.out.println("\nEdit data");
			System.out.println("---------");
			System.out.println("S - Edit students\nH - Edit high schools\nQ - Quit");
			choice = getString("\nEnter choice: ");
			//end display menu
			if (choice.toUpperCase().equals("S")){
				
				editStudents(S,H); //edit students
			}
			else if (choice.toUpperCase().equals("H")){
				
				editSchools(S,H); //edit schools
			}
			else if (choice.toUpperCase().equals("Q")){
				System.out.println(); //break loop if user enters q
				break;
			}
			else {
				System.out.println("ERROR: Invalid menu choice!"); //error
			}
		}
	}
			

	public static void editStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		int nStudents = S.size();
		int nSchools = H.size();
		if (nStudents == 0)
			System.out.println("\nERROR: No students are loaded!"); //cant edit if no students
	
		else {
			boolean bool = true; //while loop boolean
			while(bool) {
				System.out.println();
				printStudents(S, H); //print student table
				int stuEdit = getInteger("Enter student (0 to quit): ", 0, nStudents); //get which student is to be entered
				if (stuEdit == 0)
					break; //break if user decides to quit
					else {
						S.get(stuEdit - 1).editInfo(H, true); //edit rankings and details if rankings have been set
						for(int j = 0; j<nSchools; j++) {
							H.get(j).calcRankings(S); //recalculate schools rankings of students
					}
				}
			}
			
	    }
	}

	public static void editSchools(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		int nSchools = H.size();
		if (nSchools == 0)
			System.out.println("\nERROR: No schools are loaded!"); //cant edit if no schools
		else{
			int schoolEdit; //initialize variable of which school is to be edited
			do {
				System.out.println();
				printSchools(S, H);//print school table
				schoolEdit = getInteger("Enter school (0 to quit): ", 0, H.size());
				
				if (schoolEdit == 0)
					break; //break loop if user quits
				else { 
					boolean edit = true;
					if(H.size() == 0) {
						edit = false; //cant edit anything if no schools
					}
					H.get(schoolEdit - 1).editInfo(S, edit); // edit info if schools exist and rankings have been set
				}
				
			}while(schoolEdit != 0);
		}
	    }

	public static void printStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		//print student table
		int nStudents = S.size();
		int nSchools = H.size();
		if(nStudents != 0) {
			System.out.println(" #  Name                            GPA  ES  Assigned school            Preferred school order");
			System.out.println("----------------------------------------------------------------------------------------------");
			for (int counter = 0; counter < nStudents; counter++){
				System.out.format("%2d. ", counter+1);
				if(nSchools>0) {
					S.get(counter).print(H,true);
				}
				else {
					S.get(counter).print(H,false);
				}
			}
			System.out.print("----------------------------------------------------------------------------------------------\n");
		}
	
	}

	public static void printSchools(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		//print school table
		int nSchools = H.size();
		int nStudents = S.size();
		if(nSchools != 0) {
			System.out.println(" #  Name                          Weight  Assigned student           Preferred student order");
			System.out.println("--------------------------------------------------------------------------------------------");
			for (int counter = 0; counter < nSchools; counter++){
				System.out.format("%2d. ", counter+1);
				if(nStudents>0) {
					H.get(counter).print(S,true);
				}
				else {
					H.get(counter).print(S,false);
				}
			}
			System.out.println("--------------------------------------------------------------------------------------------");
		}
	}

	public static int getInteger(String prompt, int LB, int UB){
		int choice=0; //initialize placeholder value for choice
		int i; 
		//String error = "\nERROR: Input must be an integer in [%d, %d]!\n";
		while(true) { //create a while loop so the user is prompted till a valid value is entered
			i=1; //set placeholder value for i
			System.out.print(prompt); //The prompt can be assigned to a specific one in this case because it's the same for all iterations
			//use a try-catch system to gracefully resolve all possible errors
			// if an exception occurs, the value of i is set to 0
			try {
				choice = Integer.parseInt(cin.readLine()); //set choice equal to the value the user will select
			} catch(IOException e){
				i=0; //catch IOException and set i=0 if exception occurs
			} catch(NumberFormatException e) {
				i=0; //catch NumberFormatException and sert i = 0 if exception occurs
			}
			if (i!=0 && (choice<LB || choice>UB)) // in case i is not equal to 0, check for the possibility of other invalid inputs
				i=0; // again set i equal to 0
			if(i==0) 
				if(UB == Integer.MAX_VALUE) {
					System.out.format("\nERROR: Input must be an integer in [%d, infinity]!\n\n",Integer.valueOf(LB));
				}
				else {
					System.out.format("\nERROR: Input must be an integer in [%d, %d]!\n\n",Integer.valueOf(LB), Integer.valueOf(UB));
				}
				
			//give the user an error statement if i equals 0 and repeatedly ask for input until a valid value is entered
			if (i!=0) 
				return choice; //accept user input if input is valid
			}
	    }

	public static double getDouble(String prompt, double LB, double UB) {
		double input = 0; //set placeholder value for user input
		//LB=0.00;
		int i; //initialize variable i
		while(true) {
			i=1; //set value of i to 1. We use the value of i in our try-catch system
			System.out.print(prompt);
			try {
				input = Double.parseDouble(cin.readLine()); //set input equal to the value the user will input
			}catch(IOException e) { 
				i = 0; //catch IOException and set i=0 if the exception occurs
			} catch(NumberFormatException e) { 
				i = 0; //catch NumberFormatException and set i=0 if exception occurs
			}
			if (i!=0 && (input<LB || input>UB)) //in case i is not 0, check if input is negative or greater than max value of double
				i=0; //set it to 0 if input is invalid
			if (i==0) 
				System.out.format("\nERROR: Input must be a real number in [%.2f, %.2f]!\n\n", Double.valueOf(LB), Double.valueOf(UB)); 
			//print out error to the user if input was invalid and keep asking for input till a valid one is entered
			if (i!=0) 
				return input; //accept user input if input is valid
			}
	    }
	public static String getString(String prompt) {
		//similar to getString and getDouble. Asks user to input string based on a prompt.
		System.out.print(prompt);
		String input="";
		try {
			input = cin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
		
	}
	public static double compositeScore(double alpha, double GPA, int ES) {
		double score = (GPA*alpha + (1-alpha)*ES);
		return score;
	}
	

}

