//code by Scott Onestak

import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

public class DrawGraph extends JPanel {

	 private ArrayList<Instance> theDataset = new ArrayList<Instance>();
	 double latDistance, lonDistance, xMax, xMin, yMax, yMin, distX, distY,first,last;
	
	public void addEdges(ArrayList<Instance> theDataset, double graphXMin, double graphXMax, double graphYMin, double graphYMax,
			double first, double last) {
		
		xMin = graphXMin;
		distX = graphXMax - graphXMin;
		yMin = graphYMin;
		distY = graphYMax - graphYMin;
		
		this.first = first;
		this.last = last;
		
		this.theDataset.addAll(theDataset);
		
		repaint();
	}
	
	public void paintComponent(Graphics graphics) {
	 	Graphics2D g2 = (Graphics2D) graphics;
	 	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
	 
        super.paintComponent(graphics);

        //get width and height of screen
         int width = getWidth();
         int height = getHeight();
       
        
        //set background to black
        this.setBackground(Color.WHITE);
        
        //set stroke size
        g2.setStroke(new BasicStroke(4));
        
        for(int i = 0; i < theDataset.size(); i++){
        	
        	if(theDataset.get(i).getClassify() == 1){
        		//true negative = black
        		g2.setColor(Color.BLACK);
        		g2.setStroke(new BasicStroke(3));
        	} else if(theDataset.get(i).getClassify() == 2){
        		//false negative = red
        		g2.setColor(Color.RED);
        		g2.setStroke(new BasicStroke(5));
        	} else if(theDataset.get(i).getClassify() == 3){
        		//false positive = orange
        		g2.setColor(Color.ORANGE);
        		g2.setStroke(new BasicStroke(5));
        	} else if (theDataset.get(i).getClassify() == 4){
        		//true positive = magenta
        		g2.setColor(Color.MAGENTA);
        		g2.setStroke(new BasicStroke(5));
        	} else {
        		//just in case some value was not changed, exclude
        		System.out.println("Skipping:" + i);
        		g2.setColor(Color.WHITE);
        	} 
        	/*
        	if(theDataset.get(i).getIsAnomaly() == 0){
        		g2.setStroke(new BasicStroke(3));
        		g2.setColor(Color.BLACK);
        	} else {
        		g2.setStroke(new BasicStroke(3));
        		g2.setColor(Color.RED);
        	}
        	*/
        	//draw the points
        	g2.draw(new Line2D.Double(((double) theDataset.get(i).getTimestamp() - xMin) * width / distX,
        			height - ((theDataset.get(i).getValue() - yMin)*height/distY),
        			((double) theDataset.get(i).getTimestamp() - xMin) * width / distX,
        			height- ((theDataset.get(i).getValue() - yMin)*height/distY)));
        }
        
        //set stroke size and color
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.GREEN);
        
        //draw regression line
        g2.draw(new Line2D.Double(((double) theDataset.get(0).getTimestamp() - xMin) * width / distX,
        		height- ((first - yMin)*height/distY),
        		((double) theDataset.get(theDataset.size()-1).getTimestamp() - xMin) * width / distX,
        		height - ((last - yMin)*height/distY)));
        
	}

}
