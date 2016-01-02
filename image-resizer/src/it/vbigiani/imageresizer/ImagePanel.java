package it.vbigiani.imageresizer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class ImagePanel extends JComponent implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private Rectangle2D.Float shape = null;
	private Point startDrag;
	private Point endDrag;
	private ImageEditor main;

	@Override
	public Dimension getPreferredSize() {
		if (image != null) {
			return new Dimension(image.getWidth(), image.getHeight());
		}
		return super.getPreferredSize();
	}

	public ImagePanel(ImageEditor main) {
		this.main = main;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, null);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(2));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

		if (shape != null) {
			g2.setPaint(Color.BLACK);
			g2.draw(shape);
			g2.setPaint(Color.YELLOW);
			g2.fill(shape);
		}

		if (startDrag != null && endDrag != null) {
			g2.setPaint(Color.LIGHT_GRAY);
			Shape r = makeRectangle(startDrag, endDrag);
			g2.draw(r);
		}

	}

	private Rectangle2D.Float makeRectangle(Point c1, Point c2) {
		return new Rectangle2D.Float(Math.min(c1.x, c2.x), Math.min(c1.y, c2.y), Math.abs(c1.x - c2.x), Math.abs(c1.y - c2.y));
	}

	public BufferedImage getImage() {
		return image;
	}

	public void loadImage(File file) {
		try {
			image = ImageIO.read(file);
			sizeChanged();
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void sizeChanged() {
		resetSelected();
		revalidate();
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		startDrag = new Point(e.getX(), e.getY());
		endDrag = startDrag;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		if (endDrag != null && startDrag != null) {
			try {
				shape = makeRectangle(startDrag, calculateEnd(e));
				startDrag = null;
				endDrag = null;
				repaint();
			} catch (Exception ex) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}

	private Point calculateEnd(MouseEvent e) {
		double ratio = main.getRatio();
		if (ratio == 0) {
			return startDrag;
		}
		double xStart = startDrag.getX();
		double yStart = startDrag.getY();
		double xEnd = e.getX();
		double yEnd = e.getY();
		double dX = xEnd - xStart;
		double dY = yEnd - yStart;
		double cDX = Math.abs(dY * ratio) * Math.signum(dX);
		xEnd = xStart + cDX;
		yEnd = yStart + dY;
		return new Point((int) xEnd, (int) yEnd);
	}

	public void mouseDragged(MouseEvent e) {
		endDrag = calculateEnd(e);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	public int getImgX() {
		if (shape != null) {
			return shape.getBounds().x;
		}
		return 0;
	}
	
	public int getImgY() {
		if (shape != null) {
			return shape.getBounds().y;
		}
		return 0;
	}
	
	public int getImgW() {
		if (shape != null) {
			return shape.getBounds().width;
		}
		return 0;
	}
	
	public int getImgH() {
		if (shape != null) {
			return shape.getBounds().height;
		}
		return 0;
	}

	public void resetSelected() {
		shape = null;
		startDrag = null;
		endDrag = null;
	}

}