//Scott Onestak and Lindsey Curtis
//CSC 240 Project: Outlier Detection
//Time Series Outlier Detection Using OutlierMAD

public class Instance {
	
	//variables
	private double value,mean,residual;
	private int timestamp,isAnomaly,classification;
	private boolean use;
	
	//constructor
	public Instance(int timestamp,double value, int isAnomaly,int classification){
		this.timestamp = timestamp;
		this.value = value;
		this.isAnomaly = isAnomaly;
		this.classification = classification;
		this.mean = 0;
		this.residual = 0;
		this.use = true;
	}
	
	//set residual
	public void setMean(double med){
		mean = med;
	}
	
	//set the classification
	public void setClassify(int classify){
		classification = classify;
	}
	
	//set the residual
	public void setRes(double res){
		residual = res;
	}
	
	//set use
	public void setUse(boolean use){
		this.use = use;
	}
	
	//return timestamp
	public int getTimestamp(){
		return timestamp;
	}
	
	//return the value
	public double getValue(){
		return value;
	}
	
	//return anomaly value
	public int getIsAnomaly(){
		return isAnomaly;
	}
	
	//return classification
	public int getClassify(){
		return classification;
	}
	
	//return residual
	public double getMean(){
		return mean;
	}
	
	public double getRes(){
		return residual;
	}
	
	public boolean getUse(){
		return use;
	}
}
