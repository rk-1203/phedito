import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import javax.swing.plaf.basic.BasicToolBarUI;

class ImageEditor extends Component implements ActionListener , ChangeListener , MouseMotionListener , MouseListener , WindowListener
{
	static final int MAX = 1000;
	JFrame f,f1;
	JToolBar tb;
	JPanel p1,p2,p3,p4,p5;
	JButton b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15;
	JTextField t1,t2;
	JSlider js,js1;
	JLabel l1,l2,picLabel;
	MenuBar mb;
	Menu m1,m2;
	MenuItem opn,sv,svas,ext,contact,manual;
	FileDialog fd;
	String fname,path,extn;
	BufferedImage img,temp,blur1,blur2,blur3,tempImage,blur_size_icon,blur_size_background,history[]		 ;
	AffineTransform tx = null;
	File src = null,dest = null;
	int t = 0,token = 0,crop_clicked = 0,blur_size,x1=-1,x2=-1,y1=-1,y2=-1,font_size,size,startx=-1,starty=-1,miny,maxy,lasso_flag=1,endx=-1,endy=-1,textX,textY,pencil_flag=0,pencil_size=1,eraser_size=5;
	String font_name,text;
	LoadImageApp lia=null;
	int bval = 0,index=0,cur_index=0;
	double theta=0.0;
	int[][] full_img,new_img;
	int[][] boundary;
	int[] outputImagePixelData;
	Graphics2D gg;
	//JScrollPane sp1;
	JTextField txt;
	JComboBox cbFontNames;
	JComboBox cbFontSizes;
	JButton btOK;
	JButton btSetColor;
	String seFontName;
	Color colorText,colorline = Color.BLACK;
	JColorChooser jc ;
	int seFontSize;
	
	
	
	public ImageEditor()
	{
		f = new JFrame();
		f.setLayout(new BorderLayout());
		f.setLocation(-7,0);
		f.setSize(1380,733);
		f.setTitle("Phedito");
		
		//toolBar = new JToolBar();
		history = new BufferedImage[MAX];
		tb = new JToolBar(JToolBar.VERTICAL);
		tb.setUI(new BasicToolBarUI());
		tb.setLayout(new GridLayout(14,1));
		f.add(tb , BorderLayout.WEST);
		b14 = new JButton("     Undo    ");
        tb.add(b14);
		b15 = new JButton("      Redo     ");
		tb.add(b15);
		b0 = new JButton("    Pencil    ");
        tb.add(b0);
		b12 = new JButton("     Eraser    ");
		tb.add(b12);
		b1 = new JButton("   Brightness  ");
        tb.add(b1);
		b2 = new JButton("  Left Rotate  ");
      	tb.add(b2);
		b3 = new JButton("  Right Rotate ");
        tb.add(b3);
		b4 = new JButton("      Crop     ");
        tb.add(b4);
		b5 = new JButton("Flip Horizontal");
        tb.add(b5);
		b6 = new JButton(" Flip Vertical ");
        tb.add(b6);
		b7 = new JButton("      Blur     ");
        tb.add(b7);
		b8 = new JButton("    Add Text   ");
		tb.add(b8);
		b9 = new JButton("     Lasso     ");
		tb.add(b9);
		b11 = new JButton("     Resize    ");
		tb.add(b11);
		
		mb = new MenuBar();
		m1 = new Menu("File");
		m2 = new Menu("Help");
		
		opn = new MenuItem("Open");
		sv = new MenuItem("Save");
		svas = new MenuItem("Save As");
		ext = new MenuItem("Exit");
		contact = new MenuItem("About");
		manual = new MenuItem("Manual");
		
		b0.addActionListener(this);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b6.addActionListener(this);
		b7.addActionListener(this);
		b8.addActionListener(this);
		b9.addActionListener(this);
		b11.addActionListener(this);
		b12.addActionListener(this);
		b14.addActionListener(this);
		b15.addActionListener(this);
		f.addMouseListener(this);
		f.addMouseMotionListener(this);
		f.addWindowListener(this);
		opn.addActionListener(this);
		sv.addActionListener(this);
		svas.addActionListener(this);
		ext.addActionListener(this);
		manual.addActionListener(this);
		contact.addActionListener(this);
		
		m1.add(opn);
		m1.add(sv);
		m1.add(svas);
		m1.addSeparator();
		m1.add(ext);
		
		m2.add(contact);
		m2.add(manual);
		
		mb.add(m1);
		mb.add(m2);
		
		f.setMenuBar(mb);
		f.setVisible(true);		
		f.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void stateChanged(ChangeEvent e)
	{
		if(token == 1)
		{
			//bval=1.0f;
			bval= js.getValue();
			//bval=bval+(temp*1.0f)/10;
			//System.out.println(bval);
			tempImage=deepCopy(img);
			lia.repaint();
			f.setSize(1380,733);
			f.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
			f1.setAlwaysOnTop(true); 
			t1.setText(50+bval+"");
			//img = tempImage;
			//System.out.println("!");
		}
		else if(token == 3)
		{
			blur_size = js.getValue();
			t1.setText(""+blur_size);
			try
			{
				blur_size_icon = ImageIO.read(new File("blur_label.jpg"));
			}
			catch(Exception e1)
			{
				//do nothing
			}
			System.out.println("now");
			//BufferedImage bi=new BufferedImage(10+5*blur_size,10+5*blur_size, BufferedImage.TYPE_INT_ARGB);
			
			//gg.dispose();
			
			//f1.repaint();
			gg=(Graphics2D)f1.getGraphics();
			gg.drawImage(blur_size_background,220,60,60,60,null);
			gg.drawImage(blur_size_icon,250-(10+5*blur_size)/2,90-(10+5*blur_size)/2,10+5*blur_size,10+5*blur_size,null);
			//f1.repaint();
			//blur_size_icon = bi;
			//picLabel = new JLabel(new ImageIcon(blur_size_icon));
			//p2.add(picLabel,BorderLayout.EAST);
			//f1.repaint(); 
			
			//gg.dispose();
		}
		else if(token == 7)
		{
			pencil_size = js.getValue();
			t1.setText(""+pencil_size);
			try
			{
				blur_size_icon = ImageIO.read(new File("blur_label.jpg"));
			}
			catch(Exception e1)
			{
				//do nothing
			}
			System.out.println("now");
			gg=(Graphics2D)f1.getGraphics();
			gg.drawImage(blur_size_background,215,80,60,40,null);
			gg.drawImage(blur_size_icon,245-(pencil_size+1)/2,100-(pencil_size+1)/2,pencil_size+1,pencil_size+1,null);
			
		}
		else if(token == 8)
		{
			eraser_size = js.getValue();
			t1.setText(""+eraser_size);
			
			try{
			blur_size_icon = ImageIO.read(new File("blur_label1.jpg"));
			blur_size_background = ImageIO.read(new File("blur_label.jpg"));
			}
			catch(Exception ee)
			{}
			System.out.println("now");
			gg=(Graphics2D)f1.getGraphics();
			gg.drawImage(blur_size_background,153,76,60,44,null);
			gg.drawImage(blur_size_icon,183-(eraser_size+1)/2,98-(eraser_size+1)/2,eraser_size+1,eraser_size+1,null);
			
		}
	}
	
	static BufferedImage deepCopy(BufferedImage bi) 
	{
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	BufferedImage alterImage(BufferedImage imageOriginal,int brightness) throws IOException 
	{
		BufferedImage imageAltered = new BufferedImage(imageOriginal.getWidth(), imageOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
		//brightness = rand.nextInt(150 + 200 + 1) - 200; //values from 150 to 200
		//double contrast = 1.5 + (5.0 - 1.5) * rand.nextDouble(); //values from 1.5 to 5.0
		double contrast=1.5;
		//brightness =-200;
	
		Color d = new Color(imageOriginal.getRGB(5, 5));
		System.out.println(" "+d.getRed()+" "+d.getRed()+" "+d.getRed()+" "+d.getAlpha());
	
		for(int i = 0; i < imageOriginal.getWidth(); i++) 
		{
			for(int j = 0; j < imageOriginal.getHeight(); j++) 
			{
				Color c = new Color(imageOriginal.getRGB(i, j));
				if(c.getRed()!=0 && c.getBlue()!=0 && c.getGreen()!=0 )
				{
					int red = (int) contrast * c.getRed() + brightness;
					int green = (int) contrast * c.getGreen() + brightness;
					int blue = (int) contrast * c.getBlue() + brightness;
					if(red > 255) // the values of the color components must be between 0-255
					{ 
					red = 255;
					}
					else if(red < 0) 
					{
						red = 0;
					}
					if(green > 255) 
					{
						green = 255;
					}
					else if(green < 0) 
					{
						green = 0;
					}
					if(blue > 255) 
					{
						blue = 255;
					}
					else if(blue < 0) 
					{
						blue = 0;
					}
					imageOriginal.setRGB(i, j, new Color(red, green, blue).getRGB());
				}
				else if(c.getRed()==0 && c.getBlue()==0 && c.getGreen()==0 && c.getAlpha()==255)
				{
					//System.out.println("Hello");
					//imageAltered.setRGB(i, j, new Color(c.getRed(), c.getGreen(), c.getBlue(),c.getAlpha()).getRGB());
				}
				
			}
		}
		return imageOriginal;
	}
	
	public void windowClosing(WindowEvent e)
	{
		Window w = e.getWindow();
		System.out.println("Windpw closing");
		if(w == f)
		{
			if(!isSaved())
			{
				System.out.println("Windpw closing1");
				int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes to "+fname+" ?");
				if(input == 0)	//Yes
				{
					try
					{
						if(dest == null)
							save();
						else
						{
							extn = fname.substring(fname.lastIndexOf('.')+1);
							//System.out.println(extn);
							ImageIO.write(img,extn,dest);
						}
					}
					catch(Exception e1)
					{
						System.out.println(e1.getMessage());
					}
					w.setVisible(false);
					w.dispose();
					System.exit(1);
					System.out.println("Windpw closing2");
				}
				else if(input == 1) //No
				{
					w.setVisible(false);
					w.dispose();
					System.exit(1);
					System.out.println("Windpw closing3");
				}
			}
			else
			{
				w.setVisible(false);
				w.dispose();
				System.exit(1);
			}
		}
		else if(w == f1)
		{
			if(token!=6)
			{
				int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes to "+fname+" ?");
				if(input == 1)
				{
					token = 0;
					tempImage = null;
					System.out.println("brightdel");
					w.setVisible(false);
					w.dispose();
					f1 = null;
					lia.repaint();
				}
				else if(input == 0)
				{
					img = tempImage;
					t = 1;
					token = 0;
					tempImage = null;
					System.out.println("brightdel");
					w.setVisible(false);
					w.dispose();
					f1 = null;
					lia.repaint();
				}
			}
			else
			{
				token = 0;
				tempImage = null;
				System.out.println("brightdel");
				w.setVisible(false);
				w.dispose();
				f1 = null;
				lia.repaint();
			}
		}
		
	}
	
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	
	public void actionPerformed(ActionEvent e)
	{
		String str = e.getActionCommand();
		//System.out.println(str + " was clicked");
		if(e.getSource() == opn)
		{
			try
			{
				openFile();	
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == sv)
		{
			try
			{
				save();
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == svas)
		{
			try
			{
				saveAs();
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == ext)
		{
			try
			{
				exit(f);
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == b0)
		{
			try
			{
				if(src!=null)
				{
					if(f1==null)
					{	
						colorline = Color.BLACK;
						pencil_size = 1;
						tempImage=deepCopy(img);
						pencil();
					}
					else
					{
						f1.setVisible(true);
					}
				}
				else
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
			}
			catch(Exception exc)
			{
				//do nothing
			}
			token = 7;
		}
		else if(e.getSource() == b12)
		{
			try
			{
				if(src!=null)
				{
					if(f1==null)
					{
						eraser_size = 5;
						tempImage=deepCopy(img);
						colorline = Color.WHITE;
						Eraser();
					}
					else
						f1.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
			}
			catch(Exception exc)
			{
				//do nothing
			}
			token = 8;
		}
		else if(e.getSource() == b1)
		{
			try
			{
				//tempImage=deepCopy(img);
				if(src!=null)
				{
					System.out.println(f1);
					if(f1 != null)
					{
						f1.setVisible( true );
						//System.out.println("Yes");
					}
					else
					{
						bval = 0;
						brightness();
						//System.out.println("NO");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b2)
		{
			try
			{
				if(src!=null)
				{
					leftRotate();
					t = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b3)
		{
			try
			{
				if(src!=null)
				{
					rightRotate();
					t = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b4)
		{
			try
			{
				if(src!=null)
				{
					token = 2;			//crop
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b5)
		{
			try
			{
				if(src!=null)
				{
					flipHorizontal();
					t = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b6)
		{
			try
			{
				if(src!=null)
				{
					flipVertical();
					t = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b7)
		{
			try
			{
				if(src!=null)
				{
					if(f1!=null)
					{
						f1.setVisible( true );
					}
					else
					{
						f1 = new JFrame();
						f1.setLayout(new GridLayout(2,1));
						f1.setTitle("Blur Size");
						f1.setLocation(15,280);
						f1.setSize(300,130);
						f1.setVisible(true);
						f1.setResizable(false);
						f1.addWindowListener(this);
						f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						
						p1 = new JPanel();
						p1.setLayout(new FlowLayout());
						js = new JSlider(JSlider.HORIZONTAL,0, 10, 5);
						js.addChangeListener(this);
						t1 = new JTextField("10",4);
						t1.setEditable(false);
						p1.add(js);
						p1.add(t1);
						
						p2 = new JPanel();
						p2.setLayout(new FlowLayout(FlowLayout.RIGHT));
						b10 = new JButton("OK");
						p2.add(b10);
						b10.addActionListener(this);
						
						p3 = new JPanel();
						p3.setLayout(new FlowLayout(FlowLayout.RIGHT,17,-13));
						
						p4 = new JPanel();
						p4.setLayout(new GridLayout(1,2));
						
						blur_size_icon = ImageIO.read(new File("blur_label.jpg"));
						blur_size_background = ImageIO.read(new File("blur_label1.jpg"));
						BufferedImage bi=new BufferedImage(60,60, BufferedImage.TYPE_INT_ARGB);
						//gg.dispose();
						gg=(Graphics2D)bi.getGraphics();
						gg.drawImage(blur_size_background,0,0,60,60,null);
						gg.drawImage(blur_size_icon,12,12,35,35,null);
						//blur_size_icon = bi;
						picLabel = new JLabel(new ImageIcon(bi));
						p3.add(picLabel);
						f1.repaint(); 
						//js.setEnabled(false);
						//f1.add(js);
						p4.add(p2);
						p4.add(p3);
						f1.add(p1);
						f1.add(p4);
						blur_size = 5;
						
						token = 3;
						System.out.println("^");
						tempImage = deepCopy(img);
						
						
						//f1.repaint();
						
					}
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b8 && token!=4)
		{
			try
			{
				if(src!=null)
				{
					if(f1 != null)
					{
						f1.setVisible( true );
						//System.out.println("Yes");
					}
					else
					{
						addText();
						colorText = Color.BLACK;
						//System.out.println("NO");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == btOK)
		{
			try
			{
				text = txt.getText();
				font_name = cbFontNames.getSelectedItem().toString();
				font_size = (int)cbFontSizes.getSelectedItem();
				System.out.println("*"+text+"*");
				if(!text.equals(""))
				{
					f1.dispose();
					f1 = null;
					jc = null;
					/*
					f1 = new JFrame();
					f1.setLayout(new GridLayout(2,1));
					f1.setTitle("Add Text");
					f1.setLocation(15,280);
					f1.setSize(300,120);
					f1.setVisible(true);
					f1.setResizable(false);
					f1.addWindowListener(this);
					f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					
					p1 = new JPanel();
					l1 = new JLabel("Apply Text on Image?");
					p1.add(l1);
					p2 = new JPanel();
					p2.setLayout(new GridLayout(1,2));
					p3 = new JPanel();
					p4 = new JPanel();
					b10 = new JButton("Yes");
					b13 = new JButton("No");
					b10.addActionListener(this);
					b13.addActionListener(this);
					p3.add(b10);
					p4.add(b13);
					p2.add(p3);
					p2.add(p4);
					
					f1.add(p1);
					f1.add(p2);
					*/
				}
				else
					JOptionPane.showMessageDialog(f,"Please enter Text!!");
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == btSetColor)
		{
			if(token == 4)
			{
				try
				{
					jc = new JColorChooser();
					colorText = jc.showDialog(this,"Color Chooser",Color.BLACK);
				}
				catch(Exception exc)
				{
					//do nothing
				}
			}
			else if(token==7)
			{
				try
				{
					jc = new JColorChooser();
					colorline = jc.showDialog(this,"Color Chooser",Color.BLACK);
				}
				catch(Exception exc)
				{
					//do nothing
				}
			}
		}
		else if(e.getSource() == b9)
		{
			try
			{
				if(src!=null)
				{
					token = 5;
					lia.repaint();
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(b10 != null && e.getSource() == b10)
		{
			try
			{
				if(token==1 || token == 3)
				{
					token = 0;
					history[index++] = img;
					cur_index = index;
					if(tempImage!=null)
						img = tempImage;
					tempImage = null;
					t = 1;
					//exit(f1);
					f1.dispose();
					f1 = null;
					lia.repaint();	
				}
				else if(token==6)
				{
					resize(Integer.parseInt(t1.getText()),Integer.parseInt(t2.getText()));
					f1.dispose();
					f1=null;
				}
				else if(token ==4)
				{
					f1.dispose();
					f1 = null;
					history[index++] = img;
					cur_index = index;
					img = tempImage;
					t = 1;
					token = 0;
					lia.repaint();
				}
				else if(token == 7 || token ==8)
				{
					history[index++] = img;
					cur_index = index;
					img = tempImage;
					extn = fname.substring(fname.lastIndexOf('.')+1);
					ImageIO.write(img,extn,new File("_9"+fname));
					img = ImageIO.read(new File("_9"+fname));
					(new File("_9"+fname)).delete();
					f1.dispose();
					f1 = null;
					t=1;
					token = 0;
					lia.repaint();	
					pencil_size = 1;
					eraser_size = 5;
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b11)
		{
			try
			{
				if(src!=null)
				{
					if(f1 == null)
					{
						f1 = new JFrame();
						f1.setLayout(new GridLayout(2,1));
						f1.setTitle("Resize");
						f1.setLocation(15,280);
						f1.setSize(300,120);
						f1.setVisible(true);
						f1.setResizable(false);
						//f1.addWindowListener(this);

						p1 = new JPanel();
						p1.setLayout(new FlowLayout());
						t1 = new JTextField(""+img.getWidth(),10);
						t2 = new JTextField(""+img.getHeight(),10);
						p1.add(t1);
						p1.add(t2);

						p2 = new JPanel();
						p2.setLayout(new FlowLayout());
						b10 = new JButton("OK");
						p2.add(b10);
						token = 6;
						b10.addActionListener(this);
						f1.add(p1);
						f1.add(p2);
					}
					else
					{
						f1.setVisible( true );
					}
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}				
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b13)
		{
			try
			{
				if(token == 4)
				{
					f1.dispose();
					f1 = null;
					//img = tempImage;
					token = 0;
					lia.repaint();
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b14)			//undo
		{
			try
			{
				if(src!=null)
				{
					if(index>0)
					{	
						if(index == cur_index)
						{
							System.out.println("yo");
							history[index++]=img;
							index--;
							cur_index++;
						}
						index--;
						img = history[index];
						token = 0;
						lia.repaint();
						t = 1;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
		else if(e.getSource() == b15)			//redo
		{
			try
			{
				if(src!=null)
				{
					if(index<cur_index-1)
					{	
						System.out.println(index);
						index++;
						img = history[index];
						
						token = 0;
						lia.repaint();
						t = 1;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(f,"Please open an Image!!");
				}
			}
			catch(Exception exc)
			{
				//do nothing
			}
		}
	}

	public void mouseClicked(MouseEvent e)
	{
		Graphics2D g = (Graphics2D)f.getGraphics();
		int height=img.getHeight(),width=img.getWidth();
		int xc = (f.getWidth())/2+tb.getWidth()+9;
		int yc = f.getHeight()/2+50;
		int xc1 = f.getWidth()/2;
		int yc1 = f.getHeight()/2;
		int w = img.getWidth();
		int h = img.getHeight();

		int x = e.getX();
		int y = e.getY();
		if(token == 4)
		{
			if(f1==null)
			{
				f1 = new JFrame();
				f1.setLayout(new GridLayout(2,1));
				f1.setTitle("Add Text");
				f1.setLocation(15,280);
				f1.setSize(300,120);
				f1.setVisible(true);
				f1.setResizable(false);
				f1.addWindowListener(this);
				f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				p1 = new JPanel();
				l1 = new JLabel("Apply Text on Image?");
				p1.add(l1);
				p2 = new JPanel();
				p2.setLayout(new GridLayout(1,2));
				p3 = new JPanel();
				p4 = new JPanel();
				b10 = new JButton("Yes");
				b13 = new JButton("No");
				b10.addActionListener(this);
				b13.addActionListener(this);
				p3.add(b10);
				p4.add(b13);
				p2.add(p3);
				p2.add(p4);
				
				f1.add(p1);
				f1.add(p2);
			}
			else
				f1.setVisible(true);
			
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2+(font_size*3)/4 && y<=yc+height/2)
			{
				System.out.println("here");
				textX = x-tb.getWidth()-9-xc1+w/2;
				textY = y-50-yc1+h/2;
				//lia.repaint();
				tempImage = deepCopy(img);
				Graphics osg = tempImage.createGraphics();
				osg.setColor(colorText);
				osg.drawImage(tempImage,0,0,null);
				osg.setFont(new Font(font_name,Font.PLAIN,font_size));
				osg.drawString(text, textX, textY);
				try
				{
					extn = fname.substring(fname.lastIndexOf('.')+1);
					ImageIO.write(tempImage,extn,new File("_2"+fname));
					osg.dispose();
					tempImage = ImageIO.read(new File("_2"+fname));
					(new File("_2"+fname)).delete();
				}
				catch(Exception e1){}
				lia.repaint();
			}
		}
		else if(token ==8)
		{
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2 && y<=yc+height/2&&pencil_flag==1)
			{
				Graphics2D gg = (Graphics2D)tempImage.getGraphics();
				gg.setColor(colorline);
				gg.setStroke(new BasicStroke(eraser_size));
				gg.drawLine(x-tb.getWidth()-9-f.getWidth()/2+tempImage.getWidth()/2,y-50-f.getHeight()/2+tempImage.getHeight()/2,x-tb.getWidth()-9-f.getWidth()/2+img.getWidth()/2,y-50-f.getHeight()/2+img.getHeight()/2);
				g.setStroke(new BasicStroke(eraser_size));
                //g.draw(new Line2D.Float(30, 20, 80, 90));
				g.setColor(colorline);
				g = (Graphics2D)g;
				g.drawLine(x,y,x,y);
				x1=x2;
				y1=y2;
			}
		}
		//g.drawLine(x,y,x,y);
	}
	
	public void mousePressed(MouseEvent e)
	{
		Graphics g = f.getGraphics();
		int height=img.getHeight(),width=img.getWidth();
		int xc = (f.getWidth())/2+tb.getWidth()+9;
		int yc = f.getHeight()/2+50;
		int xc1 = f.getWidth()/2;
		int yc1 = f.getHeight()/2;
		int x = e.getX();
		int y = e.getY();
		int w = img.getWidth();
		int h = img.getHeight();
		if(token==5)
		{
			
			boundary = new int[img.getHeight()][2];
			x1=e.getX();
			y1=e.getY();
			startx=x1;
			starty=y1;
			int x11 = xc -w/2;
			int y11 = yc -h/2;
			g.drawLine(x1,y1,x1,y1);
			if(miny>y1)
				miny = y1;
			if(maxy < y1)
				maxy = y1;
			
			if(boundary[y1-y11][0]==0)
				boundary[y1-y11][0]=x1-x11;
			else
				boundary[y-y11][1]=x1-x11;
			
			if(boundary[y1-y11+1][0]==0)
				boundary[y1-y11+1][0]=x1-x11;
			else
				boundary[y-y11+1][1]=x1-x11;
			t = 1;
			/*
			if(boundary[y1-y11+2][0]==0)
				boundary[y1-y11+2][0]=x1-x11;
			else
				boundary[y-y11+2][1]=x1-x11;
			*/
		}
		else if(token == 2)
		{
			System.out.println(img.getWidth()+" "+img.getHeight());
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2 && y<=yc+height/2)
			{
				x1=x-tb.getWidth()-9;
				y1=y-50;
				//System.out.println("#e");
			}
		}
		else if(token==7 || token == 8)
		{
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2 && y<=yc+height/2)
			{
				Graphics gg = tempImage.getGraphics();
				gg.drawImage(tempImage,0,0,null);
				x1=e.getX();
				y1=e.getY();
				pencil_flag=1;
			}
			
		}
		else if(token == 4)
		{
			if(f1==null)
			{
				f1 = new JFrame();
				f1.setLayout(new GridLayout(2,1));
				f1.setTitle("Add Text");
				f1.setLocation(15,280);
				f1.setSize(300,120);
				f1.setVisible(true);
				f1.setResizable(false);
				f1.addWindowListener(this);
				f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				p1 = new JPanel();
				l1 = new JLabel("Apply Text on Image?");
				p1.add(l1);
				p2 = new JPanel();
				p2.setLayout(new GridLayout(1,2));
				p3 = new JPanel();
				p4 = new JPanel();
				b10 = new JButton("Yes");
				b13 = new JButton("No");
				b10.addActionListener(this);
				b13.addActionListener(this);
				p3.add(b10);
				p4.add(b13);
				p2.add(p3);
				p2.add(p4);
				
				f1.add(p1);
				f1.add(p2);
			}
			else
				f1.setVisible(true);
			
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2+(font_size*3)/4 && y<=yc+height/2)
			{
				System.out.println("here");
				textX = x-tb.getWidth()-9-xc1+w/2;
				textY = y-50-yc1+h/2;
				//lia.repaint();
				tempImage = deepCopy(img);
				Graphics osg = tempImage.createGraphics();
				osg.setColor(colorText);
				osg.drawImage(tempImage,0,0,null);
				osg.setFont(new Font(font_name,Font.PLAIN,font_size));
				osg.drawString(text, textX, textY);
				try
				{
					extn = fname.substring(fname.lastIndexOf('.')+1);
					ImageIO.write(tempImage,extn,new File("_2"+fname));
					osg.dispose();
					tempImage = ImageIO.read(new File("_2"+fname));
					(new File("_2"+fname)).delete();
				}
				catch(Exception e1){}
				lia.repaint();
			}
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		Graphics2D g = (Graphics2D)f.getGraphics();
		int height=img.getHeight(),width=img.getWidth();
		int xc = (f.getWidth())/2+tb.getWidth()+9;
		int yc = f.getHeight()/2+50;
		int xc1 = f.getWidth()/2;
		int yc1 = f.getHeight()/2;
		int x = e.getX();
		int y = e.getY();
		int w = img.getWidth();
		int h = img.getHeight();
		if(token==3)
		{
			try
			{
				blur(x-tb.getWidth()-9,y-50);
				t = 1;
				System.out.println("Hello"+x+" "+y);
			}
			catch(Exception ee){}
		}
		if(token == 2)
		{
			//System.out.println(img.getWidth()+" "+img.getHeight());
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2 && y<=yc+height/2)
			{
				x2=x-tb.getWidth()-9;
				y2=y-50;
				//System.out.println("#e");

			}
			lia.repaint();
		}
		else if(token == 4)
		{
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2+(font_size*3)/4 && y<=yc+height/2)
			{
				System.out.println("here");
				textX = x-tb.getWidth()-9-xc1+w/2;
				textY = y-50-yc1+h/2;
				//lia.repaint();
				tempImage = deepCopy(img);
				Graphics osg = tempImage.createGraphics();
				osg.setColor(colorText);
				osg.drawImage(tempImage,0,0,null);
				osg.setFont(new Font(font_name,Font.PLAIN,font_size));
				osg.drawString(text, textX, textY);
				try
				{
					extn = fname.substring(fname.lastIndexOf('.')+1);
					ImageIO.write(tempImage,extn,new File("_2"+fname));
					osg.dispose();
					tempImage = ImageIO.read(new File("_2"+fname));
					(new File("_2"+fname)).delete();
				}
				catch(Exception e1){}
				lia.repaint();
			}
		}
		else if(token==5)
		{
			int tempy;
			x2=e.getX();
			y2=e.getY();
			int x11 = xc -w/2;
			int y11 = yc -h/2;
			g.drawLine(x1,y1,x2,y2);
			double m = (y2-y1)*1.0/(x2-x1);
			//System.out.println("x1 "+x1+" x2 "+x2);
			if(miny>y2)
				miny = y2;
			if(maxy < y2)
				maxy = y2;
			/*
			for(int i = y1-y11;i<y2-y11;i++)
			{
				
			}*/
			int xh = x2;
			int yh = y2;
			if(y1>y2)
			{
				int t = x1;
				x1 = x2;
				x2 = t;
				
				t = y1;
				y1 = y2;
				y2 = t;
			}
			for(int i = y1-y11;i<=y2-y11;i++)
			{
				tempy = (int)((i-y1+y11)/m + x1-x11);
				if(boundary[i][0]==0)
					boundary[i][0]=tempy;
				else
					boundary[i][1]=tempy;
			}
			x1=xh;
			y1=yh;
			if(boundary[yh-y11][0]==0)
				boundary[yh-y11][0]=xh-x11;
			else
				boundary[yh-y11][1]=xh-x11;
		}
		else if(token == 7 || token == 8)
		{
			t = 1;
			x2=e.getX();
			y2=e.getY();
			if(token == 8)
			{
				System.out.println("This should'nt be happening");
				colorline = Color.WHITE;
				pencil_size = eraser_size;				
			}
			else
			{
				//colorline = Color.BLACK;
				g.setColor(colorline);
				//g.setColor(colorline);
			}
			if(x>=xc-width/2 && x<=xc+width/2 && y>=yc-height/2 && y<=yc+height/2&&pencil_flag==1)
			{
				Graphics2D gg = (Graphics2D)tempImage.getGraphics();
				gg.setColor(colorline);
				gg.setStroke(new BasicStroke(pencil_size));
				gg.drawLine(x1-tb.getWidth()-9-f.getWidth()/2+tempImage.getWidth()/2,y1-50-f.getHeight()/2+tempImage.getHeight()/2,x2-tb.getWidth()-9-f.getWidth()/2+img.getWidth()/2,y2-50-f.getHeight()/2+img.getHeight()/2);
				g.setStroke(new BasicStroke(pencil_size));
                //g.draw(new Line2D.Float(30, 20, 80, 90));
				g.setColor(colorline);
				g = (Graphics2D)g;
				g.drawLine(x1,y1,x2,y2);
				x1=x2;
				y1=y2;
			}
			else
			{
				pencil_flag=0;
			}
			
		}
		
		/*
		Graphics g = f.getGraphics();
		g.setColor(Color.red);
		g.drawLine(x,y,e.getX(),e.getY());		
		x = e.getX();
		y = e.getY();
		*/
	}
	
	public void mouseMoved(MouseEvent e)
	{
		//Do Nothing Method
	}
	public void mouseExited(MouseEvent e)
	{
		//Do Nothing Method
	}
	public void mouseEntered(MouseEvent e)
	{
		//Do Nothing Method
	}
	public void mouseReleased(MouseEvent e)
	{
		if(token==5)
		{
			int tempy,tx,ty;
			int xc = (f.getWidth())/2+tb.getWidth()+9;
			int yc = f.getHeight()/2+50;
			int w = img.getWidth();
			int h = img.getHeight();
			int x11 = xc -w/2;
			int y11 = yc -h/2;
			endx = e.getX();
			endy = e.getY();
			double m = (endy-starty)*1.0/(endx-startx);

				//System.out.println("Start");
			/*if(startx>endx)
				boundary[endy-y11][0]=endx-x11;
			else
				boundary[endy-y11][1]=endx-x11;*/
			
			/*if(boundary[endy-y11][0]==0)
				boundary[endy-y11][0]=endx-x11;
			else
				boundary[endy-y11][1]=endx-x11;	*/
			
			if(starty>endy)
			{
				//System.out.println("Yes");
				int t = startx;
				startx = endx;
				endx = startx;
				
				t = starty;
				starty = endy;
				endy = t;
			}
			System.out.println(" starty "+starty+" endy "+endy);
			for(int i = starty-y11;i<endy-y11;i++)
			{
				tempy = (int)((i-starty+y11)/m + startx-x11);
				System.out.println(" x "+tempy+" y "+i);
				if(boundary[i][0]==0)
					boundary[i][0]=tempy;
				else
					boundary[i][1]=tempy;
			}
			

			try
			{
				System.out.println("Devil May Care");
				lasso();
				System.out.println("Devil May Care");
			}
			catch(Exception e1)
			{
				
			}
			startx=-1;
			starty=-1;
			lia.repaint();
		}
		else if(token == 4)
		{
			if(f1==null)
			{
				f1 = new JFrame();
				f1.setLayout(new GridLayout(2,1));
				f1.setTitle("Add Text");
				f1.setLocation(15,280);
				f1.setSize(300,120);
				f1.setVisible(true);
				f1.setResizable(false);
				f1.addWindowListener(this);
				f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				p1 = new JPanel();
				l1 = new JLabel("Apply Text on Image?");
				p1.add(l1);
				p2 = new JPanel();
				p2.setLayout(new GridLayout(1,2));
				p3 = new JPanel();
				p4 = new JPanel();
				b10 = new JButton("Yes");
				b13 = new JButton("No");
				b10.addActionListener(this);
				b13.addActionListener(this);
				p3.add(b10);
				p4.add(b13);
				p2.add(p3);
				p2.add(p4);
				
				f1.add(p1);
				f1.add(p2);
			}
			else
				f1.setVisible(true);
		}
		else if(token == 2)
		{
			int xc = f.getWidth()/2;
			int yc = f.getHeight()/2;
			int w = img.getWidth();
			int h = img.getHeight();
			int x = xc -w/2;
			int y = yc -h/2;
			System.out.println("HELLO Ditesh  "+x1+" "+y1+" "+x2+" "+y2);
			if(x1<x2 && y1<y2)
			{
				System.out.println("HELLO Ditesh11  "+x1+" "+y1+" "+x2+" "+y2);
				temp=img.getSubimage(x1-x,y1-y,x2-x1,y2-y1);
			}
			else if(x1<x2 && y1>y2)
			{
				System.out.println("HELLO Ditesh22");
				temp=img.getSubimage(x1-x,y2-y,x2-x1,y1-y2);
			}
			else if(x1>x2 && y1>y2)
			{
				System.out.println("HELLO Ditesh33");
				temp=img.getSubimage(x2-x,y2-y,x1-x2,y1-y2);
			}	
			else if(x1>x2 && y1<y2)
			{
				System.out.println("HELLO Ditesh44");
				temp=img.getSubimage(x2-x,y1-y,x1-x2,y2-y1);
			}
			System.out.println("Height "+img.getHeight()+" width "+img.getWidth());
					
			//System.out.println(img.getHeight()+" "+img.getWidth());
			
			
			extn = fname.substring(fname.lastIndexOf('.')+1);
			try
			{
				ImageIO.write(temp,extn,new File("_1"+fname));
				history[index++] = img;
				cur_index = index;
				img = ImageIO.read(new File("_1"+fname));
				(new File("_1"+fname)).delete();
			}
			catch(Exception ee)
			{
				//do nothing
			}
			token=0;
			t = 1;
			lia.repaint();
		}
		
	}
	
	public class LoadImageApp extends JPanel
	{
		public LoadImageApp() 
		{
		   try 
		   {
			   //System.out.println("2");
			   img = ImageIO.read(src);
			   System.out.println("Height "+img.getHeight()+" width "+img.getWidth());

			   
			   //repaint();
		   } 
		   catch (IOException e) 
		   {
			   //do nothing
		   }

		}

		//public void paint(Graphics g) 
		public void paintComponent(Graphics g)
		{
			 super.paintComponent(g);
			//g.drawImage(img, 0, 0, null);
			//System.out.println("1"); 
			Graphics2D g2d=(Graphics2D)g;
			try
			{
				int xc = f.getWidth()/2;
				int yc = f.getHeight()/2;
				int w = img.getWidth();
				int h = img.getHeight();

				if(isRotated())
				{
					int t=w;
					w=h;
					h=t;
				}
				//drawing new image on panel
				if(token == 0 || token == 5 || token == 7)
				{
					//g2d.dispose();
					g2d.drawImage(img,xc-w/2,yc-h/2,this);
				}
				else if(token == 1)							//change Brightness
				{
					try
					{
						tempImage=alterImage(tempImage,bval*4);
					}
					catch(Exception e1)
					{
						e1.printStackTrace();
					}
					g2d.drawImage(tempImage,xc-w/2,yc-h/2,this);
					//tempImage=deepCopy(img);
				}
				else if(token == 2)						//image crop
				{
					//System.out.println("^");
					g2d.drawImage(img,xc-w/2,yc-h/2,this);
					g2d.setColor(Color.black);
					if(x1<x2 && y1<y2)
						g2d.drawRect(x1,y1,x2-x1,y2-y1);
					else if(x1<x2 && y1>y2)
						g2d.drawRect(x1,y2,x2-x1,y1-y2);
					else if(x1>x2 && y1>y2)
						g2d.drawRect(x2,y2,x1-x2,y1-y2);
					else if(x1>x2 && y1<y2)
						g2d.drawRect(x2,y1,x1-x2,y2-y1);
					
				}/*
				else if(token==4)
				{
					//System.out.println("flip  "+w+" "+h);
					temp = new BufferedImage(w, h, img.getColorModel().getTransparency());
					Graphics2D gg = temp.createGraphics();
					gg.drawImage(img, 0,0,w,h,w, 0, 0, h, null);
					g2d.drawImage(img, xc-w/2,yc-h/2,xc+w/2,yc+h/2,w, 0, 0, h, null);
					gg.dispose();
					img=temp;
					token = 0;
				}
				else if(token==5)
				{
					temp = new BufferedImage(w, h, img.getColorModel().getTransparency());
					Graphics2D gg = temp.createGraphics();
					gg.drawImage(img, 0,0,w,h,0, h, w, 0, null);
					g2d.drawImage(img, xc-w/2,yc-h/2,xc+w/2,yc+h/2,0, h, w, 0, null);
					gg.dispose();
					img=temp;
					token = 0;
				}*/
				else if(token==3 || token==4 || token==7 || token==8 )
				{
					g2d.drawImage(tempImage,xc-w/2,yc-h/2,this);
					//g2d.drawImage(blur3,xc-w/2,yc-h/2,this);
				}/*
				else if(token == 4)						//add text
				{
					
					g2d.drawImage(img,xc-w/2,yc-h/2,this);
					//osg.dispose();
				}
				else if(token==5)
				{
					//System.out.println("Hello");
					g2d.drawImage(img,xc-w/2,yc-h/2,this);
					//g2d.drawImage(blur3,xc-w/2,yc-h/2,this);
				}*/
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
					  
	  
		}
	}
	
	boolean isSaved()
	{
		if(t==0)
			return true;
		else
			return false;
	}

	boolean isRotated()
	{
		if(theta%(Math.PI)!=0)
			return true;
		else
			return false;
	}
	
	void openFile()throws Exception
	{
		if(!isSaved())
		{
			System.out.println("Windpw closing1");
			int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes to "+fname+" ?");
			if(input == 0)	//Yes
			{
				try
				{
					save();
				}
				catch(Exception e1)
				{
					System.out.println(e1.getMessage());
				}
			}
		}
		fd = new FileDialog(f,"Select File",FileDialog.LOAD);
		fd.setVisible(true);
		fname = fd.getFile();
		path = fd.getDirectory();
		src = new File(path,fname);
		System.out.println(src);
		System.out.println("File Name : "+fname+" Path : "+path);
		lia=new LoadImageApp();
		f.add(lia);
		lia.repaint();
		f.setSize(1380,733);
		f.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		dest = null;
		
	}
	
	void saveAs()throws Exception
	{
		fd = new FileDialog(f,"Save As",FileDialog.SAVE);
		fd.setVisible(true);
		fname = fd.getFile();
		path = fd.getDirectory();
		dest = new File(path,fname);
		extn = fname.substring(fname.lastIndexOf('.')+1);
		System.out.println(extn);
		ImageIO.write(img,extn,dest);
		t = 0;
	}
	
	void save()throws Exception
	{
		extn = fname.substring(fname.lastIndexOf('.')+1);
		//System.out.println(extn);
		ImageIO.write(img,extn,src);
		t = 0;
	}
	
	void exit(JFrame w)throws Exception
	{
		System.out.println("Windpw closing");
		if(w == f)
		{
			if(!isSaved())
			{
				System.out.println("Windpw closing1");
				int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes to "+fname+" ?");
				if(input == 0)	//Yes
				{
					try
					{
						save();
					}
					catch(Exception e1)
					{
						System.out.println(e1.getMessage());
					}
					w.setVisible(false);
					w.dispose();
					System.exit(1);
					System.out.println("Windpw closing2");
				}
				else if(input == 1) //No
				{
					w.setVisible(false);
					w.dispose();
					System.exit(1);
					System.out.println("Windpw closing3");
				}
			}
			else
			{
				w.setVisible(false);
				w.dispose();
				System.exit(1);
			}
		}
		else if(w == f1)
		{
			w.setVisible(false);
			w.dispose();
			w = null;
		}
		
	}
	
	public void pencil()
	{
		f1 = new JFrame();
		f1.setLayout(new GridLayout(2,1));
		f1.setTitle("Pencil");
		f1.setLocation(15,280);
		f1.setSize(300,130);
		f1.setVisible(true);
		f1.setResizable(false);
		f1.addWindowListener(this);
		f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		js = new JSlider(JSlider.HORIZONTAL,1, 10, 1);
		js.addChangeListener(this);
		t1 = new JTextField("1",4);
		t1.setEditable(false);
		p1.add(js);
		p1.add(t1);
		
		p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		b10 = new JButton("OK");
		p2.add(b10);
		b10.addActionListener(this);
		
		p5 = new JPanel();
		//p5.setLayout(new FlowLayout());
		btSetColor=new JButton("Set text color");
		p5.add(btSetColor);
		btSetColor.addActionListener(this);
		
		txt = new JTextField(30);
		
		p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.RIGHT,17,-10));
		
		p4 = new JPanel();
		p4.setLayout(new FlowLayout());
		
		try
		{
			blur_size_icon = ImageIO.read(new File("blur_label.jpg"));
			blur_size_background = ImageIO.read(new File("blur_label1.jpg"));
		}
		catch(Exception ee){}
		
		BufferedImage bi=new BufferedImage(60,60, BufferedImage.TYPE_INT_ARGB);
		//gg.dispose();
		gg=(Graphics2D)bi.getGraphics();
		gg.drawImage(blur_size_background,0,0,60,60,null);
		gg.drawImage(blur_size_icon,29,29,2,2,null);
		//blur_size_icon = bi;
		picLabel = new JLabel(new ImageIcon(bi));
		p3.add(picLabel);
		f1.repaint(); 
		//js.setEnabled(false);
		//f1.add(js);
		p4.add(p2);
		p4.add(p5);
		p4.add(p3);
		f1.add(p1);
		f1.add(p4);
		//f1.add(p4);
		
		blur_size = 5;
		
		token = 7;
		//System.out.println("^");
		//tempImage = deepCopy(img);
		
	}
	
	public void Eraser()
	{
		System.out.println("^~~~~~~~~~~~~~");
		f1 = new JFrame();
		f1.setLayout(new GridLayout(2,1));
		f1.setTitle("Eraser");
		f1.setLocation(15,280);
		f1.setSize(300,130);
		f1.setVisible(true);
		f1.setResizable(false);
		f1.addWindowListener(this);
		f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		js = new JSlider(JSlider.HORIZONTAL,5,40, 5);
		js.addChangeListener(this);
		t1 = new JTextField("5",4);
		t1.setEditable(false);
		p1.add(js);
		p1.add(t1);
		
		p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		b10 = new JButton("OK");
		p2.add(b10);
		b10.addActionListener(this);
		
		
		txt = new JTextField(30);
		
		p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.RIGHT,17,-13));
		
		p4 = new JPanel();
		p4.setLayout(new FlowLayout());
		
		try
		{
			blur_size_icon = ImageIO.read(new File("blur_label.jpg"));
			blur_size_background = ImageIO.read(new File("blur_label1.jpg"));
		}
		catch(Exception ee){}
		
		BufferedImage bi=new BufferedImage(60,60, BufferedImage.TYPE_INT_ARGB);
		//gg.dispose();
		gg=(Graphics2D)bi.getGraphics();
		gg.drawImage(blur_size_icon,0,0,60,60,null);
		gg.drawImage(blur_size_background,29,29,5,5,null);
		//blur_size_icon = bi;
		picLabel = new JLabel(new ImageIcon(bi));
		p3.add(picLabel);
		f1.repaint(); 
		//js.setEnabled(false);
		//f1.add(js);
		p4.add(p2);
		p4.add(p3);
		f1.add(p1);
		f1.add(p4);
		//f1.add(p4);
		
		blur_size = 5;
		
		token = 8;
		System.out.println("^");
		//tempImage = deepCopy(img);
		
	}
	
	void brightness()throws Exception
	{
		//temp=img;
		System.out.println("*");
		System.out.println(src);
		System.out.println("*");
		f1 = new JFrame();
		f1.setLayout(new GridLayout(3,1));
		f1.setTitle("Brightness Level");
		f1.setLocation(15,280);
		f1.setSize(300,120);
		f1.setVisible(true);
		f1.setResizable(false);
		f1.addWindowListener(this);
		f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		js = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		t1 = new JTextField("50",3);
		t1.setEditable(false);
		p1.add(t1);

		p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		b10 = new JButton("OK");
		p2.add(b10);
		b10.addActionListener(this);
		//js.setEnabled(false);
		f1.add(js);
		f1.add(p1);
		f1.add(p2);
		token = 1;
		js.addChangeListener(this);
		
	}

	void leftRotate()throws Exception
	{	
		System.out.println("1");
		System.out.println("Height "+img.getHeight()+" width "+img.getWidth());
		int i,j;
		//System.out.println("*");
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		int width = img.getWidth();
		int height = img.getHeight();
		//System.out.println(img.getWidth);
		final boolean hasAlphaChannel = img.getAlphaRaster() != null;
		outputImagePixelData= new int[height*width+1];

		full_img = new int[height][width];
		System.out.println("2");
		if (hasAlphaChannel) 
		{
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) 
				{
					col = 0;
					row++;
				}
			}
		}
		else
		{
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
        System.out.println("3");
		temp=  new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println(full_img[0][0]);
		
		new_img = new int[img.getWidth()][img.getHeight()];
		
		for(i=0 ;i<height;i++)					//transpose
		{
			for(j=0 ;j<width;j++)
			{
				new_img[j][i] = full_img[i][j];
			}
		}				
		
		System.out.println("7");
		for(i=0 ;i<height;i++)					//transpose
		{
			for(j=0 ;j<width/2;j++)
			{
				int t = new_img[j][i];
				new_img[j][i] = new_img[width-1-j][i];
				new_img[width-1-j][i] = t;
			}
		}
		
		System.out.println("4");
		for (int y=0, pos=0 ; y < width ; y++)
		{
			for (int x=0 ; x < height ; x++, pos++)
			{
				outputImagePixelData[pos] = new_img[y][x] ;
			}
            
		}
		System.out.println("5");
		
		temp.setRGB(0, 0, height, width, outputImagePixelData, 0, height);
		extn = fname.substring(fname.lastIndexOf('.')+1);
		ImageIO.write(temp,extn,new File("_1"+fname));
		//img = temp;
		history[index++] = img;
		cur_index = index;
		img = ImageIO.read(new File("_1"+fname));
		(new File("_1"+fname)).delete();
		//System.out.println(img.getWidth()+" "+img.getHeight());
		System.out.println("6");
		token = 0;
		lia.repaint();
	}

	void rightRotate()throws Exception
	{
		System.out.println("1");
		int i,j;
		//System.out.println("*");
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		System.out.println("&&&&&");
		int width = img.getWidth();
		int height = img.getHeight();
		//System.out.println(img.getWidth);
		final boolean hasAlphaChannel = img.getAlphaRaster() != null;
		outputImagePixelData= new int[height*width+1];

		full_img = new int[height][width];
		System.out.println("2");
		if (hasAlphaChannel) 
		{
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
		else
		{
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
        System.out.println("3");
		temp =  new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println(full_img[0][0]);
		System.out.println("*");
		new_img = new int[img.getWidth()][img.getHeight()];
		
		for(i=0 ;i<height;i++)					//transpose
		{
			for(j=0 ;j<width;j++)
			{
				new_img[j][height-1-i] = full_img[i][j];
			}
		}				
		
		
		System.out.println("4");
		for (int y=0, pos=0 ; y < width ; y++)
		{
			for (int x=0 ; x < height ; x++, pos++)
			{
				outputImagePixelData[pos] = new_img[y][x] ;
			}
            
		}
		System.out.println("5");
		
		temp.setRGB(0, 0, height, width, outputImagePixelData, 0, height);
		extn = fname.substring(fname.lastIndexOf('.')+1);
		ImageIO.write(temp,extn,new File("_1"+fname));
		//img = temp;
		history[index++] = img;
		cur_index = index;
		img = ImageIO.read(new File("_1"+fname));
		(new File("_1"+fname)).delete();
		//System.out.println(img.getWidth()+" "+img.getHeight());
		System.out.println("6");
		
		token = 0;
		lia.repaint();
		//img = temp;
	}
	
	void flipHorizontal()throws Exception
	{
		System.out.println("1");
		int i,j;
		//System.out.println("*");
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		int width = img.getWidth();
		int height = img.getHeight();
		//System.out.println(img.getWidth);
		final boolean hasAlphaChannel = img.getAlphaRaster() != null;
		outputImagePixelData= new int[height*width+1];

		full_img = new int[height][width];
		System.out.println("2");
		if (hasAlphaChannel) 
		{
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
		else
		{
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
        System.out.println("3");
		temp =  new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println(full_img[0][0]);
		System.out.println("*");
		new_img = new int[img.getHeight()][img.getWidth()];
		
		for(i=0 ;i<height;i++)					//transpose
		{
			for(j=0 ;j<width/2;j++)
			{
				int t = full_img[i][width-1-j];
				full_img[i][width-1-j] = full_img[i][j];
				full_img[i][j] = t;
			}
		}				
		
		
		System.out.println("4");
		for (int y=0, pos=0 ; y < height ; y++)
		{
			for (int x=0 ; x < width ; x++, pos++)
			{
				outputImagePixelData[pos] = full_img[y][x] ;
			}
            
		}
		System.out.println("5");
		
		temp.setRGB(0, 0, width, height, outputImagePixelData, 0, width);
		extn = fname.substring(fname.lastIndexOf('.')+1);
		ImageIO.write(temp,extn,new File("_1"+fname));
		//img = temp;
		history[index++] = img;
		cur_index = index;
		img = ImageIO.read(new File("_1"+fname));
		(new File("_1"+fname)).delete();
		//System.out.println(img.getWidth()+" "+img.getHeight());
		System.out.println("6");
		lia.repaint();
		//img = temp;
	}
	
	void flipVertical()throws Exception
	{
		System.out.println("1");
		int i,j;
		//System.out.println("*");
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		int width = img.getWidth();
		int height = img.getHeight();
		//System.out.println(img.getWidth);
		final boolean hasAlphaChannel = img.getAlphaRaster() != null;
		outputImagePixelData= new int[height*width+1];

		full_img = new int[height][width];
		System.out.println("2");
		if (hasAlphaChannel) 
		{
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
		else
		{
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
        System.out.println("3");
		temp =  new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println(full_img[0][0]);
		System.out.println("*");
		new_img = new int[img.getHeight()][img.getWidth()];
		
		for(i=0 ;i<height/2;i++)					//transpose
		{
			for(j=0 ;j<width;j++)
			{
				int t = full_img[height-1-i][j];
				full_img[height-1-i][j] = full_img[i][j];
				full_img[i][j] = t;
			}
		}				
		
		
		System.out.println("4");
		for (int y=0, pos=0 ; y < height ; y++)
		{
			for (int x=0 ; x < width ; x++, pos++)
			{
				outputImagePixelData[pos] = full_img[y][x] ;
			}
            
		}
		System.out.println("5");
		
		temp.setRGB(0, 0, width, height, outputImagePixelData, 0, width);
		extn = fname.substring(fname.lastIndexOf('.')+1);
		ImageIO.write(temp,extn,new File("_1"+fname));
		//img = temp;
		history[index++] = img;
		cur_index = index;
		img = ImageIO.read(new File("_1"+fname));
		(new File("_1"+fname)).delete();
		//System.out.println(img.getWidth()+" "+img.getHeight());
		System.out.println("6");
		token = 0;
		lia.repaint();
		//img = temp;
	}
	
	void blur(int x,int y) throws Exception
	{	
		int xc = f.getWidth()/2;
		int yc = f.getHeight()/2;
		int w = img.getWidth();
		int h = img.getHeight();
		int x11 = xc -w/2;
		int y11 = yc -h/2;
		x = x - x11-18;
		y = y- y11-20;
		size=3;
		float weight = 1.0f / (size * size);
		float[] data = new float[size * size];

		for (int i = 0; i < data.length; i++) 
		{
			data[i] = weight;
		}
		//int x=0;
		//int y=0;
		System.out.println("E1");
		blur1 = tempImage;
		blur2 = new BufferedImage(blur1.getWidth(this), blur1.getHeight(this), BufferedImage.TYPE_INT_RGB);
		Graphics2D tst = blur2.createGraphics(); 
		tst.drawImage(blur1, 0, 0, this);
		blur3 = new BufferedImage(blur1.getWidth(this), blur1.getHeight(this), BufferedImage.TYPE_INT_RGB);
		
		System.out.println("E2");
		//float data[] = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,0.0625f, 0.125f, 0.0625f };
		Kernel kernel = new Kernel(size,size, data);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,null);
		blur2 = tempImage.getSubimage(x, y, 10+5*(blur_size), 10+5*(blur_size)); 
		blur3 = tempImage.getSubimage(x, y, 10+5*(blur_size), 10+5*(blur_size));
		convolve.filter(blur2, blur3);    
		Graphics osg = tempImage.getGraphics();
		osg.drawImage(blur3,x,y,null);
		osg.dispose();
		//img=blur3;
		token = 3;
		lia.repaint();
	}
	
	void addText()throws Exception
	{
		f1 = new JFrame();
		f1.setLocation(15,280);
		f1.setSize(500,200);
		f1.setVisible(true);
		f1.setResizable(false);
		f1.setLayout(new BorderLayout());
		colorText=null;
		f1.setTitle("Add Text");
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		f1.setLayout(new GridLayout(4,1));
		f1.addWindowListener(this);
		
		
		cbFontNames=new JComboBox();
		cbFontSizes=new JComboBox();
		btOK=new JButton("OK");
		//btOK.setBackground(Color.BLACK);
		//btOK.setForeground(Color.BLUE);  
		btOK.addActionListener(this);

		btSetColor=new JButton("Set text color");
		//btSetColor.setBackground(Color.BLACK);
		//btSetColor.setForeground(Color.WHITE);  
		//btSetColor.addActionListener(this);
		btSetColor.addActionListener(this);
		
		txt = new JTextField(30);
		
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		//p1.setLayout(new FlowLayout());
		p1.add(new JLabel("Text:"));
		p1.add(txt);
		f1.add(p1);
		
		//p2.setLayout(new FlowLayout());
		p2.add(new JLabel("Font Name"));
		p2.add(cbFontNames);
		f1.add(p2);
		
		p3.setLayout(new FlowLayout());
		p3.add(new JLabel("Font Size"));
		p3.add(cbFontSizes);
		f1.add(p3);
		
		
		p4.setLayout(new FlowLayout());
		p4.add(btSetColor);
		p4.add(btOK);
		f1.add(p4);

		//p1.setBackground(Color.GRAY);
		//f1.add(p1, BorderLayout.CENTER);
		f1.add(p1);
		f1.add(p2);
		f1.add(p3);
		f1.add(p4);
		
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment(); 
		String[] fonts=ge.getAvailableFontFamilyNames();
		for(String f:fonts)
			cbFontNames.addItem(f);
		//Initialize font sizes
		for(int i=8;i<50;i++)
			cbFontSizes.addItem(i);
		tempImage = deepCopy(img);
		token = 4;
	}
	          
	void lasso()throws Exception
	{
		System.out.println("1");
		int i,j;
		//System.out.println("*");
		final byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		final int width = img.getWidth();
		final int height = img.getHeight();
		final boolean hasAlphaChannel = img.getAlphaRaster() != null;
		outputImagePixelData= new int[height*width+1];

		full_img = new int[height][width];
		System.out.println("2");
		if (hasAlphaChannel) 
		{
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
		else
		{
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				full_img[row][col] = argb;
				col++;
				if (col == width) {
				col = 0;
				row++;
				}
			}
		}
        System.out.println("3");
		temp=  new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println(full_img[0][0]);
		
		new_img = new int[img.getHeight()][img.getWidth()];
		
		for(i=0 ;i<img.getHeight();i++)
		{
			for(j=boundary[i][0];j<boundary[i][1];j++)
			{
				new_img[i][j] = full_img[i][j];
			}
			for(j=boundary[i][1];j<boundary[i][0];j++)
			{
				new_img[i][j] = full_img[i][j];
			}
		}				
		System.out.println("4");
		for (int y=0, pos=0 ; y < height ; y++)
		{
			for (int x=0 ; x < width ; x++, pos++)
			{
				outputImagePixelData[pos] = new_img[y][x] ;
			}
            
		}
		System.out.println("5");
		
		temp.setRGB(0, 0, width, height, outputImagePixelData, 0, width);
		extn = fname.substring(fname.lastIndexOf('.')+1);
		//System.out.println(extn);
		ImageIO.write(temp,extn,new File("_3"+fname));
		history[index++] = img;
		cur_index = index;
		img = ImageIO.read(new File("_3"+fname));
		(new File("_3"+fname)).delete();
		//img = temp;
		//System.out.println("6");
		lia.repaint();
	}
	
	public void resize(int w,int h)
	{
		history[index++] = img;
		cur_index = index;
		t=1;
		System.out.println("HHHHHHHHHHHHHHHHHHHHHHHH");
		BufferedImage bi=new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg=(Graphics2D)bi.getGraphics();
		gg.drawImage(img,0,0,w,h,null);
		img=bi;
		try{
			ImageIO.write(img,fname.substring(fname.lastIndexOf('.')+1),new File("_4"+fname));
			img = ImageIO.read(new File("_4"+fname));
			(new File("_4"+fname)).delete();
		}
		catch(Exception e)
		{
			
		}
		gg.dispose();
		token =0 ;
		System.out.println("fff");
		lia.repaint();
   
	}
	
	public static void main(String args[])
	{
		ImageEditor e = new ImageEditor();
	}
}