import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.lang.String.*;
import java.lang.Math.*;
import java.io.FileWriter;


/*CS575-01_FALL2014 Programming Assignment 3
* Created by - Neelabh Agrawal
*
* Program to implement CNF_SAT problem using Backtrack Algorithm with Pure literal identification
*
* Input arguments - <input file name> <output file name>
*       Input file should be in following structure -
*       Line 1: n m sizeC// where n is the number of variables, and m the number of clauses
*       and sizeC the number of literals in the clauses
*       Line 2 to m+1: a list of sizeC positive and negative numbers in the range 1 to n.
*       If the number is negative the literal is negated.
*
* Ouput format - Output file with one possible solution if any
*/

class Submission {
	int n, m, sizeC, countTrue;
	int[][] clauses;
	int[] x, clauseValue, res, a;
	boolean resultFound = false, noMore = false;
	String outFile;
	FileWriter writer;
	
	Submission(String in, String out) {
		
		try {
			Scanner scan = new Scanner(new File(in));
			outFile = out;
			n = scan.nextInt();
			m = scan.nextInt();
			sizeC = scan.nextInt();

			/* create a two dimensional array clauses and read all the values from input file*/
			x = new int[n];
			a = new int[n];
			for(int i=0;i<n;i++) {
				x[i]=(-1);
				a[i]=(-1);
			}
			clauses = new int[m][sizeC];

			for(int i = 0;i < m;i++) {
				for(int j=0;j<sizeC;j++) {
					clauses[i][j] = scan.nextInt();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		res = new int[m];

		/*get maximum and minimum index of x variables for each clause*/
		countTrue = 0;
		/*create new file with output file name entered by user*/
		try {
			writer = new FileWriter(outFile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}


	void backTrackComputeH1(int depth) {
		int result = checkClauses(depth);
		if (result==0) {// depth!=0){//not promising
			restore(depth);
			return;
		}
		if (result==1 && !noMore){//solution
			printResult(depth);
			noMore = true; //only one solution to be printed
			resultFound = true; //satisfying result is found
			restore(depth);
			return;
		}
		//result is not 0 or 1
		while (assignAllPure(depth) && !noMore){
			int newResult = checkClauses(depth);
			if (newResult == 1){//solution
				printResult(depth);
				noMore = true;
				resultFound = true;
				restore(depth);
				return;
	    		}
			if (newResult == 0){//pure variables cannot cause a clause to fail
				System.out.println("Check Clauses gives an incorrect result");
				System.exit(0);
	     		}
		}//while	

		//Not all clauses have a value yet, so continue the search
		if(depth<=n) {
		int var =findUnAssigned();//a[var] = -1 for all unassigned variables
		x[var]=1; a[var]=depth;
		if(!noMore)
			backTrackComputeH1(depth+1);
		x[var]=0; a[var]=depth;
		if(!noMore);
			backTrackComputeH1(depth +1);
		restore(depth);
		}
	}//backTrackComputeH1


	//method will restore value of array a to -1 for particular depth
	void restore(int depth) {
		for(int i=0;i<n;i++) {
			if(a[i]==depth) 
				a[i] = (-1);
		}
	}


	//Method will return newxt unassigned value in x set
	int findUnAssigned() {
		for(int i=0;i<n;i++) {
			if(a[i]==-1) {
				return i;
			}
		}
		return (n-1);
	}

	boolean assignAllPure(int depth) {

		//valueType will be 1 if pure literal is positive and -1 if it is negative
		//valueType will be set to 2 if literal is not pure
		int valueType, count=0; 
		for(int k=1;k<=n;k++) { //loop for value of x
			valueType = 0;

			//if x is already assigned then skip
			if(a[k-1]<=depth && a[k-1]>=0) { 
				continue;
			}
			for(int i=0;i<m;i++) {

				//if clause is already true then skip
				if(res[i]!=1) { 
					for(int j=0;j<sizeC;j++) {
						int clauseElement = clauses[i][j];

						//x found first time and is positive
						if(clauseElement==k && valueType==0) {
							valueType=1;
							break;
						} else if(clauseElement==(0-k) && valueType==0) {
							valueType=(-1);
							break;
						}
						if((clauseElement== k && valueType==(-1)) || (clauseElement == (0-k) && valueType==1)) {
							valueType=2;
							break;
						}
					}
					//x literal is not pure
					if(valueType==2) {
						break;
					}
				}
			}

			//set value of x if it is pure
			if(valueType==1) {
				x[k-1] = 1;
				a[k-1] = depth;
				count++;
			} else if(valueType==(-1)) {
				x[k-1] = 0;
				a[k-1] = depth;
				count++;
			}

		}
		if(count==0)
			return false;
		return true;
	}


	int checkClauses(int depth) {
		int atLeastOne=0;
		
		for(int i=0;i<m;i++)
			res[i] = (-1);

		for(int i=0;i<m;i++) {
			int count=0;atLeastOne=0;
			for(int j=0;j<sizeC;j++) {
				int c = clauses[i][j];
				if(c<0 && a[(0-c)-1] <=depth && a[(0-c)-1]>=0) {
					if(x[(0-c)-1]==0) {
						res[i] = 1;
						atLeastOne = 1;
						break;
					} else {
						count++;
					}
				} else if(c>0 && a[c-1]<=depth && a[c-1]>=0) {
					if(x[c-1]==1) {
						res[i] = 1;
						atLeastOne=1;
						break;
					} else {
						count++;
					}
				}
			}
			if(count==sizeC)
				return 0;//a clause is evaluatted 0 in the expression
				
		}
		countTrue = 0;
		for(int i=0;i<m;i++)
			if(res[i]==1)
				countTrue++;

		if(countTrue==m)
			return 1;//this means expression staisfied
		
		return -1;//expression is not yet evaluated completely
	}


	
	void printResult(int depth){
		try {
			writer = new FileWriter(outFile, true);
			writer.write(" The solution is\n");

			//print result in form of variable values
			for(int i=0;i<n;i++) {
				if(x[i] != -1)
				writer.write("x[" + (i+1) + "] = " + x[i] + "\n");
			}
			writer.close();
		}catch(IOException exc) {
			System.out.println(exc.getMessage());
		}
	}

	public static void main(String[] args) {
		String output = "";

		long time1 = System.currentTimeMillis();

		Submission sub = new Submission(args[0],args[1]);

		sub.backTrackComputeH1(0);

		//check if result is not found
		if(!sub.resultFound)
			output = "No satisfying assignment\n";

		//calculate running time
		long time2 = System.currentTimeMillis();
		output = output + "Run time is " + (time2 - time1) + " milliseconds";

		//Append the output string to output file
		try {
			FileWriter wr = new FileWriter(sub.outFile, true);
			wr.write(output);
			wr.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
