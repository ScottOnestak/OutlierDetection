//code by Scott Onestak

public class Instance {
	
	//variables
	private double value,median,residual;
	private int timestamp,isAnomaly,classification;

	//constructor
	public Instance(int timestamp,double value, int isAnomaly,int classification){
		this.timestamp = timestamp;
		this.value = value;
		this.isAnomaly = isAnomaly;
		this.classification = classification;
		this.median = 0;
		this.residual = 0;
	}
	
	//set residual
	public void setMedian(double med){
		median=med;
	}
	
	//set the classification
	public void setClassify(int classify){
		classification = classify;
	}
	
	//set the residual
	public void setRes(double res){
		residual = res;
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
	public double getMed(){
		return median;
	}
	
	//return residaul
	public double getRes(){
		return residual;
	}
}
