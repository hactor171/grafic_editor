/*
 *  Program EdytorGraficzny - aplikacja z graficznym interfejsem
 *   - obs�uga zdarze� od klawiatury, myszki i innych element�w GUI.
 *
 *  Autor: Roman Kovalchuk
 *   Data: 26.11.2015 r.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


abstract class Figure{

	static Random random = new Random();

	private boolean selected = false;
	
	public static final Comparator<Figure> compare_name = new Comparator<Figure>() {
		
		public int compare(Figure fig1, Figure fig2) {
			return fig1.getName().compareTo(fig2.getName());
		}
	};
	public static final Comparator<Figure> compare_x = new Comparator<Figure>() {
		public int compare(Figure fig1, Figure fig2) {
			return (int) Math.signum(fig1.getX() - fig2.getX());
		}
	};
	public static final Comparator<Figure> compare_y = new Comparator<Figure>() {
		public int compare(Figure fig1, Figure fig2) {
			return (int) Math.signum(fig1.getY() - fig2.getY());
		}
	};
	


	public boolean isSelected() { return selected; }
	public void select() {	selected = true; }
	public void select(boolean z) { selected = z; }
	public void unselect() { selected = false; }

	protected void setColor(Graphics g) {
		if (selected) g.setColor(Color.BLUE);
		           else g.setColor(Color.RED);
	}

	public abstract boolean isInside(float px, float py);
	public boolean isInside(int px, int py) {
		return isInside((float) px, (float) py);
	}

	protected String properties() {
		String s = String.format("  Pole: %.0f  Obwod: %.0f", computeArea(), computePerimeter());
		if (isSelected()) s = s + "   [SELECTED]";
		return s;
	}

	abstract String getName();
	abstract float  getX();
	abstract float  getY();

    abstract float computeArea();
    abstract float computePerimeter();

    abstract void move(float dx, float dy);
    abstract void scale(float s);

    abstract void draw(Graphics g);

    @Override
    public String toString(){
        return getName();
    }

}





class Elipse extends Point{
	float radx;
	float rady;
	
	Elipse(){
		radx= random.nextFloat()*100;
		rady= random.nextFloat()*100;
	
	}
	Elipse( float px, float py, float pradx, float prady   ){
		super(px, py);
		radx= pradx;
		rady= prady;
	}
	
	@Override
	public boolean isInside(float px, float py) {
		
		return (x - px)*(x-px)/(radx*radx)+(y-py)*(y-py)/(rady*rady) <=1;  
	}

	@Override
	String getName() {
		return "Elipse("+ x +","+ y +","+ radx +","+ rady +")";
	}

	
	@Override
	float computeArea(){ return (float)Math.PI*radx*rady; }
		
		
	@Override
	float computePerimeter() { return (float)(2*(Math.PI)*(Math.sqrt(0.5*(radx*radx)+(rady*rady)))); 
		
	}

	@Override
	void scale(float s) {
	 radx *= s;
	 rady *= s;
	}

	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawOval((int) (x-radx), (int) (y-rady), (int)(2*radx), (int) (2*rady));
		
	}
}

class Square extends Point{
float height;
	
	Square(){
		height = random.nextFloat()*100+20;
		
	}
	Square(float px, float py, float h){
		super(px, py);
		height = h;
		
	}

	@Override
	public boolean isInside(float px, float py) {
		
		return px>= x && px<=x+height/2 && py>= y && py<=y+height ;
	}

	@Override
	String getName() {
		return "Square("+ x + "," + y + ")";
	}

	
	@Override
	float computeArea() {
		
		return height*height;
	}

	@Override
	float computePerimeter() {
		return 4*height;
	}

	@Override
	void scale(float s) {
		height *=s;
		
	}

	@Override
	void draw(Graphics g) {
		setColor(g);
			g.drawRect((int) (x), (int)(y), (int) height, (int) height );
		
	}
}
class Rectangle extends Point{
	float height;
	float breadth; 
	Rectangle(){
		height = random.nextFloat()*40+20;
		breadth = random.nextFloat()*40+20;
	}
	Rectangle(float px, float py, float h, float b){
		super(px, py);
		height = h;
		breadth = b;
	}

	@Override
	public boolean isInside(float px, float py) {
		
		return px>= x-breadth/2 && px<=x+breadth/2 && py>= y-height/2 && py<=y+height/2 ;
	}

	@Override
	String getName() {
		return "Rectangle("+ x + "," + y + ")";
	}

	
	@Override
	float computeArea() {
		
		return breadth*height;
	}

	@Override
	float computePerimeter() {
		return 2*(breadth+height);
	}

	@Override
	void scale(float s) {
		breadth *=s;
		height *=s;
		
	}

	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawRect((int) (x-breadth/2), (int)(y-height/2), (int) breadth, (int) height );
		
	}
	
}
class Point extends Figure{

	protected float x, y;

	Point()
	{ this.x=random.nextFloat()*400;
	  this.y=random.nextFloat()*400;
	}

	Point(float x, float y)
	{ this.x=x;
	  this.y=y;
	}

	@Override
	public boolean isInside(float px, float py) {
		
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= 6);
	}


    @Override
	String getName() {
		return "Point(" + x + ", " + y + ")";
	}

	@Override
	float getX() {
		return x;
	}

	@Override
	float getY() {
		return y;
	}

	@Override
    float computeArea(){ return 0; }

	@Override
	float computePerimeter(){ return 0; }

	@Override
    void move(float dx, float dy){ x+=dx; y+=dy; }

	@Override
    void scale(float s){ }

	@Override
    void draw(Graphics g){
		setColor(g);
		g.fillOval((int)(x-3),(int)(y-3), 6,6);
	}

    String toStringXY(){ return "(" + x + " , " + y + ")"; }

}


class Circle extends Point{
    float r;

    Circle(){
        super();
        r=random.nextFloat()*100;
    }

    Circle(float px, float py, float pr){
        super(px,py);
        r=pr;
    }

    @Override
	public boolean isInside(float px, float py) {
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= r);
	}

    @Override
   	String getName() {
   		return "Circle(" + x + ", " + y + ")";
   	}

    @Override
    float computeArea(){ return (float)Math.PI*r*r; }

    @Override
    float computePerimeter(){ return (float)Math.PI*r*2; }

    @Override
    void scale(float s){ r*=s; }

    @Override
    void draw(Graphics g){
    	setColor(g);
        g.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
    }

}


class Triangle extends Figure{
    Point point1, point2, point3;

    Triangle(){
    	point1 = new Point();
    	point2 = new Point();
    	point3 = new Point();
    }

    Triangle(Point p1, Point p2, Point p3){
        point1=p1; point2=p2; point3=p3;
    }

    @Override
    public boolean isInside(float px, float py)
    { float d1, d2, d3;
      d1 = px*(point1.y-point2.y) + py*(point2.x-point1.x) +
           (point1.x*point2.y-point1.y*point2.x);
      d2 = px*(point2.y-point3.y) + py*(point3.x-point2.x) +
           (point2.x*point3.y-point2.y*point3.x);
      d3 = px*(point3.y-point1.y) + py*(point1.x-point3.x) +
           (point3.x*point1.y-point3.y*point1.x);
      return ((d1<=0)&&(d2<=0)&&(d3<=0)) || ((d1>=0)&&(d2>=0)&&(d3>=0));
    }

    @Override
	String getName() {
    	return "Triangle{"+point1.toStringXY()+
                point2.toStringXY()+
                point3.toStringXY()+"}";
	}

	@Override
	float getX() {
		return (point1.x+point2.x+point3.x)/3;
	}

	@Override
	float getY() {
		return (point1.y+point2.y+point3.y)/3;
	}

	@Override
	float computeArea(){
        float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                                    (point1.y-point2.y)*(point1.y-point2.y));
        float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                                    (point2.y-point3.y)*(point2.y-point3.y));
        float c = (float)Math.sqrt( (point1.x-point3.x)*(point1.x-point3.x)+
                                    (point1.y-point3.y)*(point1.y-point3.y));
        float p=(a+b+c)/2;
        return (float)Math.sqrt(p*(p-a)*(p-b)*(p-c));
    }

	@Override
    float computePerimeter(){
        float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                                    (point1.y-point2.y)*(point1.y-point2.y));
        float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                                    (point2.y-point3.y)*(point2.y-point3.y));
        float c = (float)Math.sqrt( (point1.x-point3.x)*(point1.x-point3.x)+
                                    (point1.y-point3.y)*(point1.y-point3.y));
        return a+b+c;
    }

	@Override
    void move(float dx, float dy){
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
    }

	@Override
    void scale(float s){
        Point sr1 = new Point((point1.x+point2.x+point3.x)/3,
                              (point1.y+point2.y+point3.y)/3);
        point1.x*=s; point1.y*=s;
        point2.x*=s; point2.y*=s;
        point3.x*=s; point3.y*=s;
        Point sr2 = new Point((point1.x+point2.x+point3.x)/3,
                              (point1.y+point2.y+point3.y)/3);
        float dx=sr1.x-sr2.x;
        float dy=sr1.y-sr2.y;
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
    }

	@Override
    void draw(Graphics g){
		setColor(g);
        g.drawLine((int)point1.x, (int)point1.y,
                   (int)point2.x, (int)point2.y);
        g.drawLine((int)point2.x, (int)point2.y,
                   (int)point3.x, (int)point3.y);
        g.drawLine((int)point3.x, (int)point3.y,
                   (int)point1.x, (int)point1.y);
    }

}



class Picture extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;


	Vector<Figure> figures = new Vector<Figure>();

    private int mouseX;
    private int mouseY;

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Figure f : figures)
			f.draw(g);
	}


    void addFigure(Figure fig)
    { for (Figure f : figures){ f.unselect(); }
      fig.select();
      figures.add(fig);
      repaint();
    }


    void moveAllFigures(float dx, float dy){
    	for (Figure f : figures){
    		if (f.isSelected()) f.move(dx,dy);
    	}
        repaint();
    }

    void scaleAllFigures(float s){
    	for (Figure f : figures)
        	{ if (f.isSelected()) f.scale(s);
        	}
          repaint();
    }

    public String toString(){
        String str = "Rysunek{ ";
        for(Figure f : figures)
            str+=f.toString() +"\n         ";
        str+="}";
        return str;
    }


    
    public void keyPressed (KeyEvent evt)
    //Virtual keys (arrow keys, function keys, etc) - handled with keyPressed() listener.
    {  int dist;
       if (evt.isShiftDown()) dist = 10;
                         else dist = 1;
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			moveAllFigures(0, -dist);
			break;
		case KeyEvent.VK_DOWN:
			moveAllFigures(0, dist);
			break;
		case KeyEvent.VK_LEFT:
			moveAllFigures( -dist , 0);
			break;
		case KeyEvent.VK_RIGHT:
			moveAllFigures(dist, 0);
			break;
		case KeyEvent.VK_DELETE:
			Iterator<Figure> i = figures.iterator();
			while (i.hasNext()) {
				Figure f = i.next();
				if (f.isSelected()) {
					i.remove();
				}
			}
			repaint();
			break;
		}
    }

   public void keyReleased (KeyEvent evt)
   {  }

   public void keyTyped (KeyEvent evt)
   //Characters (a, A, #, ...) - handled in the keyTyped() listener.
   {
     char znak=evt.getKeyChar(); 
		switch (znak) {
		case 'p':
			addFigure(new Point());
			break;
		case 'c':
			addFigure(new Circle());
			break;
		case 't':
			addFigure(new Triangle());
			break;
		case 'e':
			addFigure(new Elipse());
			break;
		case 'r':
			addFigure(new Rectangle());
			break;
		case 's':
			addFigure(new Square());
			break;

		case '+':
			scaleAllFigures(1.1f);
			break;
		case '-':
			scaleAllFigures(0.9f);
			break;
		}
   }


   
   public void mouseClicked(MouseEvent e)
   // Invoked when the mouse button has been clicked (pressed and released) on a component.
   { int px = e.getX();
     int py = e.getY();
     for (Figure f : figures)
       { if (e.isAltDown()==false) f.unselect();
         if (f.isInside(px,py)) f.select( !f.isSelected() );
       }
     repaint();
   }

   public void mouseEntered(MouseEvent e)
   //Invoked when the mouse enters a component.
   { }

   public void mouseExited(MouseEvent e)
   //Invoked when the mouse exits a component.
   { }


	public void mousePressed(MouseEvent e)
	// Invoked when a mouse button has been pressed on a component.
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

   public void mouseReleased(MouseEvent e)
   //Invoked when a mouse button has been released on a component.
   { }
   public void mouseDragged(MouseEvent e) {
		moveAllFigures( (e.getX() - mouseX),(e.getY() - mouseY));
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
	}
   
}



public class GraphicEditor extends JFrame implements ActionListener{


	private static final long serialVersionUID = 3727471814914970170L;


	private final String DESCRIPTION = "OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n"
			+ "   strzalki ==> przesuwanie figur\n"
			+ "   SHIFT + strzalki ==> szybkie przesuwanie figur\n"
			+ "   +,-  ==> powiekszanie, pomniejszanie\n"
			+ "   DEL  ==> kasowanie figur\n"
			+ "   p  ==> dodanie nowego punktu\n"
			+ "   e  ==> dodanie nowego elipsa\n"
			+ "   c  ==> dodanie nowego kola\n"
			+ "   r  ==> dodanie nowego prostokonta\n"
			+ "   t  ==> dodanie nowego trojkata\n"
			+ "   s  ==> dodanie nowego kwadratu\n"
			+ "\nOperacje myszka:\n" + "   klik ==> zaznaczanie figur\n"
			+ "   ALT + klik ==> zmiana zaznaczenia figur\n"
			+ "   przeciaganie ==> przesuwanie figur";


	protected Picture picture;

	private JMenu[] menu = { new JMenu("Figury"),
			                 new JMenu("Edytuj"),
			                 new JMenu("Pomoc")};

	private JMenuItem[] items = { new JMenuItem("Punkt"),
			                      new JMenuItem("Kolo"),
			                      new JMenuItem("Elipsa"),
			                      new JMenuItem("Prostokat"),
			                      new JMenuItem("Trojkat"),
			                      new JMenuItem("Kwadrat"),
			                      new JMenuItem("Wypisz wszystkie"),
			                      new JMenuItem("Przesun w gore"),
			                      new JMenuItem("Przesun w dol"),
			                      new JMenuItem("Przesun w lewo"),
			                      new JMenuItem("Przesun w prawo"),
			                      new JMenuItem("Powieksz"),
			                      new JMenuItem("Pomniejsz"),
			                      new JMenuItem("Autor"),
			                      new JMenuItem("Opis programu"),
			                      };

	private JButton buttonPoint = new JButton("Punkt");
	private JButton buttonCircle = new JButton("Kolo");
	private JButton buttonElipse = new JButton("Elipsa");
	private JButton buttonRectangle = new JButton("Prostokat");
	private JButton buttonTriangle = new JButton("Trojkat");
	private JButton buttonSquare = new JButton("Kwadrat");



    public GraphicEditor()
    { super ("Edytor graficzny");
      setSize(400,400);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      for (int i = 0; i < items.length; i++)
      	items[i].addActionListener(this);

      // dodanie opcji do menu "Figury"
      menu[0].add(items[0]);
      menu[0].add(items[1]);
      menu[0].add(items[2]);
      menu[0].add(items[3]);
      menu[0].add(items[4]);
      menu[0].add(items[5]);
      menu[0].addSeparator();
      menu[0].add(items[6]);

      // dodanie opcji do menu "Edytuj"
      menu[1].add(items[7]);
      menu[1].add(items[8]);
      menu[1].add(items[9]);
      menu[1].add(items[10]);
      menu[1].addSeparator();
      menu[1].add(items[11]);
      menu[1].add(items[12]);
     // dodanie opcji do menu "Pomoc"
      menu[2].add(items[13]);
      menu[2].add(items[14]);
      

      // dodanie do okna paska menu
      JMenuBar menubar = new JMenuBar();
      for (int i = 0; i < menu.length; i++)
      	menubar.add(menu[i]);
      setJMenuBar(menubar);

      picture=new Picture();
      picture.addKeyListener(picture);
      picture.setFocusable(true);
      picture.addMouseListener(picture);
      picture.setLayout(new FlowLayout());
      picture.addMouseMotionListener(picture); 

      buttonPoint.addActionListener(this);
      buttonCircle.addActionListener(this);
      buttonElipse.addActionListener(this);
      buttonTriangle.addActionListener(this);
      buttonRectangle.addActionListener(this);
      buttonSquare.addActionListener(this);
      
      picture.add(buttonPoint);
      picture.add(buttonCircle);
      picture.add(buttonElipse);
      picture.add(buttonTriangle);
      picture.add(buttonRectangle);
      picture.add(buttonSquare);
      setContentPane(picture);
      setVisible(true);
    }

	public void actionPerformed(ActionEvent evt) {
		Object zrodlo = evt.getSource();

		if (zrodlo == buttonPoint)
			picture.addFigure(new Point());
		if (zrodlo == buttonCircle)
			picture.addFigure(new Circle());
		if (zrodlo == buttonElipse)
			picture.addFigure(new Elipse());
		if (zrodlo == buttonTriangle)
			picture.addFigure(new Triangle());
		if (zrodlo == buttonRectangle)
			picture.addFigure(new Rectangle());
		if (zrodlo == buttonSquare)
			picture.addFigure(new Square());
		

		if (zrodlo == items[0])
			picture.addFigure(new Point());
		if (zrodlo == items[1])
			picture.addFigure(new Circle());
		if (zrodlo == items[2])
			picture.addFigure(new Elipse());
		if (zrodlo == items[3])
			picture.addFigure(new Rectangle());
		if (zrodlo == items[4])
			picture.addFigure(new Triangle());
		if (zrodlo == items[5])
			picture.addFigure(new Square());
		if (zrodlo == items[6])
			
			JOptionPane.showMessageDialog(null, picture.toString());

		if (zrodlo == items[7])
			picture.moveAllFigures(0, -10);
		if (zrodlo == items[8])
			picture.moveAllFigures(0, 10);
		if (zrodlo == items[9])
			picture.moveAllFigures(-10, 0);
		if (zrodlo == items[10])
			picture.moveAllFigures(10, 0);
		if (zrodlo == items[11])
			picture.scaleAllFigures(1.1f);
		if (zrodlo == items[12])
			picture.scaleAllFigures(0.9f);
		if (zrodlo == items[13])
			
	  	JOptionPane.showMessageDialog((Component)null, "Autor: Roman Kovalchuk");
	    if (zrodlo == items[14])
	    	JOptionPane.showMessageDialog((Component) null,
	    			"OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n"
	    					+ "   strzalki ==> przesuwanie figur\n"
	    					+ "   SHIFT + strzalki ==> szybkie przesuwanie figur\n"
	    					+ "   +,-  ==> powiekszanie, pomniejszanie\n"
	    					+ "   DEL  ==> kasowanie figur\n"
	    					+ "   p  ==> dodanie nowego punktu\n"
	    					+ "   e  ==> dodanie nowego elipsa\n"
	    					+ "   c  ==> dodanie nowego kola\n"
	    					+ "   r  ==> dodanie nowego prostokonta\n"
	    					+ "   t  ==> dodanie nowego trojkata\n"
	    					+ "   s  ==> dodanie nowego kwadratu\n"
	    					+ "\nOperacje myszka:\n" + "   klik ==> zaznaczanie figur\n"
	    					+ "   ALT + klik ==> zmiana zaznaczenia figur\n"
	    					+ "   przeciaganie ==> przesuwanie figur");
		picture.requestFocus(); 
		repaint();
	}

    public static void main(String[] args)
    { new GraphicEditor();
    }

}

