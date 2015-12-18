//code by Scott Onestak

public class Results {

	private int fileNum;
	private double tp,fp,fn,tn,r2,a,b,se;
	private String theString;
	
	public Results(int fileNum, double tp, double fp, double fn, double tn, double r2, double a, double b, double se){
		this.fileNum = fileNum;
		this.tp = tp;
		this.fp = fp;
		this.fn = fn;
		this.tn = tn;
		this.r2 = r2;
		this.a = a;
		this.b = b;
		this.se = se;
		theString = Integer.toString(fileNum) + "," + Double.toString(tp) + "," + Double.toString(fp) + "," + Double.toString(fn) + "," +
						Double.toString(tn) + "," + Double.toString(r2) + "," + Double.toString(a) + "," + Double.toString(b) + "," +
						Double.toString(se) + "\n";
		
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
