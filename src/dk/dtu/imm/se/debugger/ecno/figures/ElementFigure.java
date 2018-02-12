package dk.dtu.imm.se.debugger.ecno.figures;

import java.awt.GraphicsEnvironment;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class ElementFigure extends EFigure {

	private String text;
	protected Dimension corner = new Dimension(0, 0);  	
	private int expandWidth = 20;
	private int expandHeight = 10;

	public ElementFigure(String text) {
		// TODO Auto-generated constructor stub
		super();
		this.text = text;
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Font font = new Font(null, new FontData(fonts[0], 15, java.awt.Font.BOLD));
				//			int width = FigureUtilities.getTextWidth(text, font);
				Dimension dim = FigureUtilities.getTextExtents(text, font);
		Dimension size = dim.expand(expandWidth, expandHeight);
		this.setSize(size);
		this.setBounds(new Rectangle(0, 0, size.width, size.height));
		
		font.dispose();
	}

	@Override
	protected void fillShape(Graphics graphics) {
		// TODO Auto-generated method stub
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
		
		Point p = getBounds().getLocation();
		int height = getBounds().height-expandHeight;
		graphics.setForegroundColor(getForegroundColor());
		graphics.drawString(text, p.x+ expandWidth/2, p.y+height/8);
		
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub
		Rectangle f = Rectangle.SINGLETON;
		Rectangle r = getBounds();

		f.x = r.x + getLineWidth() / 2;
		f.y = r.y + getLineWidth() / 2;
		f.width = r.width - getLineWidth();
		f.height = r.height - getLineWidth();
		//	getLineWidth()
		//	System.out.println(f.x + "," + f.y + ";" + f.width + "," + f.height + ";" + corner.width + "," + corner.height);
		graphics.drawRoundRectangle(f, corner.width, corner.height);
		
	}
	
	public void setCornerDimensions(Dimension d) {
		corner.width = d.width;
		corner.height = d.height;
	}
	

	

}
