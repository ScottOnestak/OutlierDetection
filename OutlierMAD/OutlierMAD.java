//Scott Onestak and Lindsey Curtis
//CSC 240 Project: Outlier Detection
//Time Series Outlier Detection Using OutlierMAD

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;


public class OutlierMAD {
	
	public static ArrayList<Instance> theDataset = new ArrayList<Instance>();
	public static ArrayList<Results> results = new ArrayList<Results>();
	public static double graphYMax = Double.NEGATIVE_INFINITY;
	public static double graphYMin = Double.POSITIVE_INFINITY;
	public static double graphXMax = Double.NEGATIVE_INFINITY;
	public static double graphXMin = Double.POSITIVE_INFINITY;
	public static double tLevel = 3;
	public static int tp = 0,tn = 0,fp = 0,fn = 0;

	public static void main(String[] args) {
		
		//start the timer
		long startTime = System.currentTimeMillis();
		
		//necessary variables
		String container;
		String[] holder;
		double value;
		int timestamp,isAnomaly;
				
		//set equal to 67 for real files, 100 for everything else
		for(int i = 1; i <= 67; i++){
			//String fileName = "synthetic_" + Integer.toString(i) + ".csv";
			String fileName = "real_" + Integer.toString(i) + ".csv";
			//String fileName = "A3Benchmark-TS" + Integer.toString(i) + ".txt";
			//String fileName = "A4Benchmark-TS" + Integer.toString(i) + ".txt";
			
			//create try...catch
			try{
				//create buffered reader
				BufferedReader br = new BufferedReader(new FileReader(fileName));
					
				//read in first line...just labels so unimportant
				container = br.readLine();
				//begin to read in data
				container = br.readLine();
					
				//while there is another line
				while(container != null){
						
					//split on the comma... tab for A4, comma everything else
					holder = container.split(",");
					//holder = container.split("\t");
				
					//create appropriate variables from the split values
					timestamp = Integer.parseInt(holder[0]);
					value = Double.parseDouble(holder[1]);
					isAnomaly = Integer.parseInt(holder[2]);
						
					//get max and mins for graphics later
					if(timestamp > graphXMax){
						graphXMax = timestamp;
					} 
					if (timestamp < graphXMin){
						graphXMin = timestamp;
					}
					if(value > graphYMax){
						graphYMax = value;
					}
					if(value < graphYMin){
						graphYMin = value;
					}
						
					//create a new instances from the line
					Instance newInst = new Instance(timestamp,value,isAnomaly,0);
						
					//add instance to the dataset
					theDataset.add(newInst);
					
					//read in next line
					container = br.readLine();
				}
					
				//close buffered reader
				br.close();
					
			}catch(FileNotFoundException e){
				System.out.println("File not found: ");
			}catch(IOException e){
				e.printStackTrace();
			}
			
			outlierMAD(40);
			
			//add to results
			results.add(new Results(i,tp,fp,fn,tn));
			
			//Create GUI
			JFrame frame = new JFrame();
			frame.setTitle("Scatter Plot for " + Integer.toString(i));
			frame.setPreferredSize(new Dimension(700,700));
			DrawGraph draw = new DrawGraph();
			frame.add(draw);
			frame.pack();
			frame.setVisible(true);	
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//add the edges to the GUI
			draw.addEdges(theDataset, graphXMin, graphXMax, graphYMin, graphYMax);
		
			theDataset.clear();
			tp = fp = fn = tn = 0;
			graphYMax = Double.NEGATIVE_INFINITY;
			graphYMin = Double.POSITIVE_INFINITY;
			graphXMax = Double.NEGATIVE_INFINITY;
			graphXMin = Double.POSITIVE_INFINITY;
			
			theDataset.clear();
			tp = fp = fn = tn = 0;
			graphYMax = Double.NEGATIVE_INFINITY;
			graphYMin = Double.POSITIVE_INFINITY;
			graphXMax = Double.NEGATIVE_INFINITY;
			graphXMin = Double.POSITIVE_INFINITY;
		}
		
		//write results to file
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("Results.csv"));
			
			for(Results r: results){
				bw.write(r.getTheString());
			}

			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//print out the time it took to run the algorithm
		long finishTime = System.currentTimeMillis();
		long elapsedTime = finishTime - startTime;
		System.out.println("\nThe algorithm took " + elapsedTime + " milliseconds");
	}
	
	public static void outlierMAD(int size){
		int length = theDataset.size()-1;
		for(int i = size+1; i <= length-size; i++){
			double theMedian = median(i-size,i+size);
			double signVal = 1.4826 * newMedian(i-size,i+size,theMedian);
			if(Math.abs(theDataset.get(i).getValue()-theMedian) > tLevel * signVal){
				if(theDataset.get(i).getIsAnomaly() == 1){
					//true positive
					theDataset.get(i).setClassify(4);
					tp++;
				} else {
					//false positive
					theDataset.get(i).setClassify(3);
					fp++;
				}
			} else {
				if(theDataset.get(i).getIsAnomaly() == 0){
					//true negative
					theDataset.get(i).setClassify(1);
					tn++;
				} else {
					//false negative
					theDataset.get(i).setClassify(2);
					fn++;
				}
			}
			theDataset.get(i).setMedian(theMedian);
		}
	}
	
	public static double median(int lower, int upper){
		//place those values from the database into their own subset...they are now the window
		int counter = 0;
		double[] theWindow = new double[upper-lower+1];
		for(int i = lower; i <= upper; i++){
			theWindow[counter] = theDataset.get(i).getValue();
			counter++;
		}
		//sort the array to get the median
		Arrays.sort(theWindow);
		//System.out.println("Lower: " + lower + "   Upper: " + upper + "\n" + Arrays.toString(theWindow));
		int index = ((upper-lower+1) / 2);
		//System.out.println("Returning: " + theWindow[index]);
		return theWindow[index];
	}
	
	public static double newMedian(int lower, int upper, double med){
		//place the spread of the points about the median into a subset and find the median again
		int counter = 0;
		double[] spread = new double[upper-lower+1];
		for(int i = lower; i <= upper; i++){
			spread[counter] = Math.abs(theDataset.get(i).getValue()-med);
			counter++;
		}
		Arrays.sort(spread);
		//System.out.println("Lower: " + lower + "   Upper: " + upper + "\n" + Arrays.toString(spread));
		int index = ((upper-lower+1) / 2);
		//System.out.println("Returning: " + spread[index]);
		return spread[index];
	}
}
