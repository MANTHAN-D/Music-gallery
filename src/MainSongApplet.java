/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newpackage;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sun.net.www.content.image.gif;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.applet.AudioClip;
import java.net.*;
import java.awt.event.*;
import java.awt.*;

/**
 *
 <applet code="MainSongApplet" width=1000 height=1000>
 * </applet>
 */
public class MainSongApplet extends Applet implements ItemListener,ActionListener{
    Choice movieName;
    String movieSelected="";
    Button findSongs;
    List songList;
    SongPlayFrame frame;
    Dimension size=new Dimension(200,300);

    String search;
    String tname;

    Connection  con = null;
    Statement   stmt = null;
    ResultSet  rs = null;

    AudioClip currsong,prevsong;
    int prevSongFlag=0;
    URL songsurl;

     
    @Override
     public void init()
    {
        movieName=new Choice();
        findSongs=new Button("GO");
        findSongs.setActionCommand("GO");

        resize(1300,1300);
        //setBackground(Color.CYAN);
        setLayout(new GridLayout(3,1));
        
        ImageIcon icon = new ImageIcon("src/newpackage/Images/smileymain.gif");
        //BufferedImage myimage= new BufferedImage(100,100,);

        JPanel p1=new JPanel();
        p1.setBackground(Color.ORANGE);
        JPanel p2=new JPanel();
        p2.setBackground(Color.ORANGE);
        JPanel p3=new JPanel(new FlowLayout(FlowLayout.LEFT,30,10));
        p3.setBackground(Color.ORANGE);
        
        JLabel l1=new JLabel( icon, JLabel.CENTER);
        p1.add(l1);

        JLabel l2=new JLabel("SHREE's MUSIC GALLERY",JLabel.CENTER );
        Font myFont = new Font(Font.SANS_SERIF,Font.BOLD+Font.ITALIC,75);
        l2.setFont(myFont);
        p2.add(l2);

        //JPanel p4=new JPanel(new GridLayout(1,4));
        //p4.setBackground(Color.ORANGE);
        //setLayout(new FlowLayout(FlowLayout.LEADING,30,50));
        ImageIcon miniIcon=new ImageIcon("src/newpackage/Images/smileyicon.gif");
        JLabel imageLabel = new JLabel(miniIcon);
        JLabel textLabel = new JLabel("Select Movie");
        Font textFont = new Font(Font.SERIF,Font.ITALIC,30);
        textLabel.setFont(textFont);

        p3.add(imageLabel);
        p3.add(textLabel);

        
        movieName.add("");
        movieName.add("Aaja Nachle");
        movieName.add("Agneepath");
        movieName.add("Jannat");
        movieName.add("Mere Bother ki Dulhan");
        movieName.add("Murder2");

        p3.add(movieName);
        p3.add(findSongs);

        //p3.add(p4);

        songList= new List(8,false) ;
        songList.add("");

        //songList.setCursor(Cursor.HAND_CURSOR);

        p3.add(songList);

        add(p1);
        add(p2);
        add(p3);
        
        
        movieName.addItemListener(this);
        findSongs.addActionListener(this);
        songList.addActionListener(this);

        try {
           
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicGallery","root","");
            con.setAutoCommit(false);

            stmt = con.createStatement();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        try{
                songsurl= new URL("file:C:/Documents and Settings/global infosys/My Documents/NetBeansProjects/SwingDemo/build/classes/song_base/");
           }catch(MalformedURLException e)
           {
                e.printStackTrace();
           }

        frame= new SongPlayFrame((this.getWidth())/3,(this.getHeight())/4);
        frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   }

    public void itemStateChanged(ItemEvent ie)
    {
        movieSelected=movieName.getSelectedItem();
        System.out.println(movieSelected);
    }

    public void actionPerformed(ActionEvent ae)
    {
        //System.out.println(ae.getSource());
        //Button find=(Button)ae.getSource();
        //if(find.getLabel()=)

        frame.dispose();
        //frame.setVisible(frame.isDisplayable());
        if(ae.getActionCommand().equals("GO"))
        {
            songList.setCursor(new Cursor(Cursor.HAND_CURSOR));
            songList.removeAll();
            if(movieSelected.equalsIgnoreCase(""))
            {
                songList.add("No movie selected");
            }
            else
            {
                try{
                search="Select * from mov_list where Name='"+movieSelected+"'";
                rs = stmt.executeQuery(search);
                rs.next();
                tname=rs.getString(1);
                search="Select * from "+tname;
                rs= stmt.executeQuery(search);
                rs.next();
                do{

                      String sr_no = rs.getString(1);
                      String name = rs.getString(2);
                      String record= sr_no+"."+name;
                      songList.add(record);
                  }while(rs.next());
                System.out.println(tname);
                
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            if(!ae.getActionCommand().equals("No movie selected"))
            {
                if(prevSongFlag==1)
                {
                    prevsong.stop();
                }
                String songname=ae.getActionCommand()+".wav";
                frame.setTitle(songname);
                currsong=getAudioClip(songsurl,songname);

                prevsong=currsong;
                prevSongFlag=1;

                frame.currsong=currsong;
                frame.setVisible(true);
                
                String pathName,pathSearch;
                pathSearch="Select Path from "+tname+" where Name='"+ae.getActionCommand()+"'";
                try{
                    //rs= stmt.executeQuery(pathSearch) ;
                    //rs.next();
                    //pathName=rs.getString("Path");
                    //System.out.println(pathName);
                    }catch(Exception e)
                     {
                        e.printStackTrace();
                     }
            }
        }
        
    }
        public void stop()
        {
            System.out.println("appletStop");
            try{
            con.commit();
            con.close();
            }catch(Exception e)
                {
                    e.printStackTrace();
                }
        }

}

class SongPlayFrame extends JFrame implements AudioClip
{
    public SongPlayFrame(int w, int h)
    {
        
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocation(w,h);
        setBackground(Color.white);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        label=new JLabel("What do you wish to do??",JLabel.CENTER);
        label.setFont(new Font("Serif",Font.BOLD,DEFAULT_SIZE)) ;

        //JPanel p=new JPanel();
        //p.add(label, 30);

        add(label,BorderLayout.CENTER);

        buttonPanel = new JPanel();
        group=new ButtonGroup();

        addOption("Play Once");
        addOption("Play in Loop");
        addOption("Stop");

        add(buttonPanel,BorderLayout.SOUTH);

       /* MyWindowHandler windowHandler = new MyWindowHandler(this);

	addWindowListener(windowHandler);*/
    }

    public void addOption(String name)
    {
        boolean selected= name.equalsIgnoreCase("Play Once");
        JRadioButton button = new JRadioButton(name, selected);
        group.add(button);
        buttonPanel.add(button);

        ActionListener listener=new ActionListener()
        {

            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equalsIgnoreCase("Play Once"))
                {
                    play();
                    System.out.println("play clicked songNo:"+songcounter);
                    //JFrame playFrame= new newpackage.playFrame.getInstance();
                    //playFrame.setVisible(true);
                }
                else if(e.getActionCommand().equalsIgnoreCase("Play in Loop"))
                {
                    loop();
                    System.out.println("Play in Loop");
                    //JFrame downloadFrame= new newpackage.downloadFrame.getInstance();
                    //downloadFrame.setVisible(true);
                }
                else
                {

                    stop();
                    System.out.println("Stop");
                }
            }
        };
        button.addActionListener(listener);
    }

    public void play()
    {
        currsong.play();
        songcounter++;
    }
    public void loop()
    {
        currsong.loop();
        songcounter++;
    }
    public void stop()
    {
        currsong.stop();
    }

    /*public void setDefaultCloseOperation(int operation)
    {
        //this.setDefaultCloseOperation(operation);
        currsong.stop();

    }*/

   /* public static void close()
    {
        currsong.stop();
    }*/

    public static final int DEFAULT_WIDTH=300;
    public static final int DEFAULT_HEIGHT=200;
    public static AudioClip currsong;
    public int songcounter=0;

    private JPanel buttonPanel;
    private ButtonGroup group;
    private JLabel label;

    private static final int DEFAULT_SIZE=20;
}
/*class MyWindowHandler extends WindowAdapter
{
	JFrame songPlayFrame;
	public MyWindowHandler(JFrame songPlayFrame)
	{
		this.songPlayFrame=songPlayFrame;
	}
	public void windowClosing(WindowEvent e)
	{
		SongPlayFrame.close();
                songPlayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
}*/