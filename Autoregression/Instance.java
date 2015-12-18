//code by Scott Onestak

public class Instance {
	
	//variables
	private double value,residual,expected;
	private int timestamp,isAnomaly,classification;
	private boolean use;

	//constructor
	public Instance(int timestamp,double value, int isAnomaly,int classification){
		this.timestamp = timestamp;
		this.value = value;
		this.isAnomaly = isAnomaly;
		this.classification = classification;
		this.residual = 0;
		this.expected = 0;
		this.use = true;
	}
	
	//set residual
	public void setResidual(double res){
		residual = res;
	}
	
	//set the classification
	public void setClassify(int classify){
		classification = classify;
	}
	
	public void setUse(boolean using){
		use = using;
	}
	
	public void setExp(double exp){
		expected = exp;
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
	public double getRes(){
		return residual;
	}
	
	public boolean getUse(){
		return use;
	}
	
	public double getExp(){
		return expected;
	}
}
