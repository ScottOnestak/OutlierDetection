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


public class MovingAverage {
	
	public static ArrayList<Instance> theDataset = new ArrayList<Instance>();
	public static ArrayList<Results> results = new ArrayList<Results>();
	public static double graphYMax = Double.NEGATIVE_INFINITY;
	public static double graphYMin = Double.POSITIVE_INFINITY;
	public static double graphXMax = Double.NEGATIVE_INFINITY;
	public static double graphXMin = Double.POSITIVE_INFINITY;
	public static double tLevel = 3;
	public static int tp = 0,tn = 0,fp = 0,fn = 0;
	public static double sdRes;
	public static int mValue = 4;

	public static void main(String[] args) {
		
		//start the timer
		long startTime = System.currentTimeMillis();
		
		//necessary variables
				String container;
				String[] holder;
				double value;
				int timestamp,isAnomaly;
					
				//set equal to 67 for real data files, 100 for every other set
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
					
					movingAverage(mValue);
					
					for(int j = mValue + 1; j < theDataset.size(); j++){
						double compare = Math.abs(theDataset.get(j).getRes() / sdRes);
						if(compare < 3 & theDataset.get(j).getIsAnomaly() == 0){
							//true negative
							theDataset.get(j).setClassify(1);
							tn++;
						} else if(compare < 3 & theDataset.get(j).getIsAnomaly() == 1) {
							//false negative
							theDataset.get(j).setClassify(2);
							fn++;
						} else if(compare >= 3 & theDataset.get(j).getIsAnomaly() == 0){
							//false positive
							theDataset.get(j).setClassify(3);
							fp++;
						} else if (compare >= 3 & theDataset.get(j).getIsAnomaly() == 1){
							//true positive
							theDataset.get(j).setClassify(4);
							theDataset.get(j).setUse(false);
							recalculate(j,mValue);
							tp++;
						} else {
							theDataset.get(j).setClassify(5);
						}
					}
					
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
	
	public static void movingAverage(int size){
		for(int i = size + 1; i < theDataset.size() - 1; i++){
			theDataset.get(i).setMean(mean(i - size, i - 1));
		}
		
		calcRes(size);
	}
	
	public static double mean(int lower, int upper){
		double sum = 0;
		double count = 0;
		
		for(int i = upper; i >= lower; i--){
			if(theDataset.get(i).getUse()){
				sum += theDataset.get(i).getValue() * (i+1-lower);
				count += i+1-lower;
			}
		}
		
		if(count == 0){
			theDataset.get(upper+1).setUse(false);
			return Integer.MAX_VALUE;
		}
		
		return sum / count;
	}
	
	public static void calcRes(int size){
		double sum = 0;
		for(int i = size; i < theDataset.size(); i++){
			double res = theDataset.get(i).getValue() - theDataset.get(i).getMean();
			theDataset.get(i).setRes(res);
			sum += Math.pow(res,2);
		}
		sdRes = Math.pow(sum / theDataset.size(), .5);
	}
	
	public static void recalculate(int outlier, int interval){
		int range;
		if(theDataset.size()-1 > outlier + interval){
			range = outlier + interval;
		} else {
			range = theDataset.size()-1;
		}
		for(int i = outlier + 1; i <= range; i++){
			theDataset.get(i).setMean(mean(i-interval, i - 1));
			double res = theDataset.get(i).getValue() - theDataset.get(i).getMean();
			theDataset.get(i).setRes(res);
		}
	}
}
