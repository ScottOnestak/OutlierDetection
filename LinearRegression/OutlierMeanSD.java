//code by Scott Onestak

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import javax.swing.JFrame;

public class OutliersMeanSD {
	
	public static ArrayList<Instance> theDataset = new ArrayList<Instance>();
	public static ArrayList<Results> results = new ArrayList<Results>();
	public static int tp = 0,tn = 0,fp = 0,fn = 0;
	public static double graphYMax = Double.NEGATIVE_INFINITY;
	public static double graphYMin = Double.POSITIVE_INFINITY;
	public static double graphXMax = Double.NEGATIVE_INFINITY;
	public static double graphXMin = Double.POSITIVE_INFINITY;
	public static double ResYMax = Double.NEGATIVE_INFINITY;
	public static double ResYMin = Double.POSITIVE_INFINITY;
	
	public static void main(String[] args) {
		
		//start the timer
		long startTime = System.currentTimeMillis();
		
		//necessary variables
		String container;
		String[] holder;
		double value;
		int timestamp,isAnomaly;
		
		//set equal to 67 for real data sets, 100 for other data sets
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
				
					//split on the comma...tab for A4, comma for the rest
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
	
			//calculate linear regression...
			//a is the y-intercept value
			//b is the slope coefficient
			double b = (r() * sdY()) / sdX();
			double a = meanY() - (r() * sdY() * meanX()) / sdX();
		
			//System.out.println("a: " + a);
			//System.out.println("b: " + b);
		
			//calculate endpoints to plot regression line
			double first = a + b * theDataset.get(0).getTimestamp();
			double last = a + b * theDataset.get(theDataset.size() - 1).getTimestamp();
		
			double r2 = r2(a,b);
			double se = standardError(a,b);
		
			double sdRes = sdResidual(a,b);
		
			for(int j = 0; j < theDataset.size(); j++){
				double compare = Math.abs(theDataset.get(j).getRes() / sdRes);
				if(compare < 3 & theDataset.get(j).getIsAnomaly() == 0){
					//true negative
					theDataset.get(j).setClassify(1);
					tn++;
				} else if(compare < 3 & theDataset.get(j).getIsAnomaly() == 1) {
					//false negative
					theDataset.get(j).setClassify(2);
					//System.out.println(i + ": " + j +": " + theDataset.get(j).getRes()/sdRes);
					fn++;
				} else if(compare >= 3 & theDataset.get(j).getIsAnomaly() == 0){
					//false positive
					theDataset.get(j).setClassify(3);
					fp++;
				} else {
					//true positive
					theDataset.get(j).setClassify(4);
					tp++;
				}
			}
		
			//add to results
			results.add(new Results(i,tp,fp,fn,tn,r2,a,b,se));
		
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
			draw.addEdges(theDataset, graphXMin, graphXMax, graphYMin, graphYMax,first,last);
		
			theDataset.clear();
			tp = fp = fn = tn = 0;
			graphYMax = Double.NEGATIVE_INFINITY;
			graphYMin = Double.POSITIVE_INFINITY;
			graphXMax = Double.NEGATIVE_INFINITY;
			graphXMin = Double.POSITIVE_INFINITY;
			ResYMax = Double.NEGATIVE_INFINITY;
			ResYMin = Double.POSITIVE_INFINITY;
		}
		
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
	
	//calculate the mean of X
	public static double meanX(){
		double sum = 0;
		for(int i = 0; i < theDataset.size(); i++){
			sum += theDataset.get(i).getTimestamp();
		}
		return sum / theDataset.size();
	}
	
	//calculate the mean of Y
	public static double meanY(){
		double sum = 0;
		for(int i = 0; i < theDataset.size(); i++){
			sum += theDataset.get(i).getValue();
		}
		return sum / theDataset.size();
	}
	
	//calculate standard deviation of X
	public static double sdX(){
		double sum = 0;
		double mean = meanX();
		for(int i = 0; i < theDataset.size(); i++){
			sum += Math.pow(theDataset.get(i).getTimestamp() - mean, 2);
		}
		return Math.pow(sum / theDataset.size(), .5);
	}
	
	//calculate standard deviation of Y
	public static double sdY(){
		double sum = 0;
		double mean = meanY();
		for(int i = 0; i < theDataset.size(); i++){
			sum += Math.pow(theDataset.get(i).getValue() - mean, 2);
		}
		return Math.pow(sum / theDataset.size(), .5);
	}
	
	//calculate covariance
	public static double covariance(){
		double sum = 0;
		double meanX = meanX();
		double meanY = meanY();
		for(int i = 0; i < theDataset.size(); i++){
			sum += (theDataset.get(i).getTimestamp() - meanX) * (theDataset.get(i).getValue() - meanY);
		}
		return sum / theDataset.size();
	}
	
	//calculate r value
	public static double r(){;
		return covariance()/(sdX() * sdY());
	}
	
	//calcualate r-squared value
	public static double r2(double a, double b){
		double explainedVariation = 0;
		double totalVariation = 0;
		double mean = meanY();
		for(int i = 0; i < theDataset.size(); i++){
			totalVariation += Math.pow(theDataset.get(i).getValue() - mean, 2);
			explainedVariation += Math.pow((a + b * theDataset.get(i).getTimestamp()) - mean, 2);
		}
		return explainedVariation/totalVariation;
	}

	//calculate theta squared value
	public static BigDecimal theta2(double a, double b){
		BigDecimal sum = new BigDecimal("0");
		for(int i = 0; i < theDataset.size(); i++){
			double temp = Math.pow(theDataset.get(i).getValue() - (a + b * theDataset.get(i).getTimestamp()),2);
			BigDecimal temp2 = new BigDecimal(String.valueOf(temp));
			sum = sum.add(temp2);
		}
		double front = 1 / (theDataset.size()-2);
		BigDecimal begin = new BigDecimal(String.valueOf(front));
		return sum;
	}
	
	//calculate variance of X
	public static double varX(){
		double sum = 0;
		double mean = meanX();
		for(int i = 0; i < theDataset.size(); i++){
			sum += Math.pow(theDataset.get(i).getTimestamp() - mean,2);
		}
		return sum;
	}
	
	//calculate standard error of b value
	public static double standardError(double a, double b){
		
		BigDecimal theta = new BigDecimal(String.valueOf(theta2(a,b)));
		BigDecimal size = new BigDecimal(String.valueOf(theDataset.size()));
		BigDecimal var = new BigDecimal(String.valueOf(varX()));
		BigDecimal col = new BigDecimal(String.valueOf(1-r2(a,b)));
		
		BigDecimal numerator = theta.divide(size,MathContext.DECIMAL128);
		BigDecimal denominator = var.multiply(col);
		BigDecimal variance = numerator.divide(denominator,MathContext.DECIMAL128);
		
		return Math.sqrt(variance.doubleValue());
	}
	
	public static double sdResidual(double a, double b){
		double sum = 0;
		for(int i = 0; i < theDataset.size(); i++){
			double res = theDataset.get(i).getValue() - (a+ b * theDataset.get(i).getTimestamp());
			theDataset.get(i).setResidual(res);
			if(res > ResYMax){
				ResYMax = res;
			}
			if(res < ResYMin){
				ResYMin = res;
			}
			
			sum += Math.pow(res,2);
		}
		return Math.pow(sum / theDataset.size(), .5);
	}
}
