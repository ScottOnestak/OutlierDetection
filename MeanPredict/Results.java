//Scott Onestak and Lindsey Curtis
//CSC 240 Project: Outlier Detection
//Time Series Outlier Detection Using OutlierMAD

public class Results {

	private int fileNum;
	private double tp,fp,fn,tn;
	private String theString;
	
	public Results(int fileNum, double tp, double fp, double fn, double tn){
		this.fileNum = fileNum;
		this.tp = tp;
		this.fp = fp;
		this.fn = fn;
		this.tn = tn;

		theString = Integer.toString(fileNum) + "," + Double.toString(tp) + "," + Double.toString(fp) + "," + Double.toString(fn) + "," +
						Double.toString(tn) + "\n";
		
	}
	
	//return fileNum
	public int getFileNum(){
		return fileNum;
	}
	
	//return tp
	public double getTP(){
		return tp;
	}
	
	//return fp
	public double getFP(){
		return fp;
	}
	
	public String getTheString(){
		return theString;
	}
}
