package GUIPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI {
	public static void main(String args[]) {
	       JFrame frame = new JFrame("Course Scheduler");
	       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       frame.setSize(300,300);
	       
	       JMenuBar mb = new JMenuBar();
	       JMenu fm = new JMenu("File");
	       mb.add(fm);
	       
	       JMenuItem open = new JMenuItem("Open...");
	       JFileChooser fc = new JFileChooser();
	       open.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                int rv = fc.showOpenDialog(frame);
	                
	                if (rv == JFileChooser.APPROVE_OPTION) {
	                	File file = fc.getSelectedFile();
	                	// send this file to the parser for validity checking
	                }
	            }
	        });
	       fm.add(open); // Adds Button to content pane of frame
	       
	       JMenuItem save = new JMenuItem("Save As...");
	       fm.add(save);
	       
	       JButton run = new JButton("Plan Schedule");
	       
	       frame.getContentPane().add(BorderLayout.NORTH, mb);
	       frame.getContentPane().add(run);
	       
	       frame.setVisible(true);
	    }
}
