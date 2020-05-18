package scheduler;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Room {

  private String building;
  private String number;
  private int capacity;

  public Room() {
	  this.capacity = 0;
  }
  
  public Room(String building, String number, int capacity) {
    this.building = building;
    this.number = number;
    this.capacity = capacity;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
  
  public void ErrorMsg() {
      String message = "Error: Invalid building name in room info file. " + getBuilding() + " is invalid or doesn't host computer science classes";
      JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
          JOptionPane.ERROR_MESSAGE);
    }
  
  public int getPriority() {
	  int E = 2;
	  int IT = 0;
	  int IL = 10;
	  int JWS = 4;
	  int MC = 7;
	  int SH = 1;
	  int BS = 9;
	  int MP = 6;
	  int PP = 12;
	  int FA = 5;
	  int PAH = 8;
	  int P = 11;
	  int UC = 3;
	  
	  int priority = 13; //initialized here so compiler doesn't yell at me
	  
	    
	  if(getBuilding().compareTo("Engineering") == 0) {
	   	priority =  E; 
	   }
	  else if(getBuilding().compareTo("Public Policy") == 0) {
		   	priority =  PP; 
	  }
	  else if(getBuilding().compareTo("Information Technology") == 0) {
		  priority =  IT;
	  }
	  else if(getBuilding().compareTo("Interdisciplinary Life S") == 0) {
		  priority =  IL;
	    }
	  else if(getBuilding().compareTo("Janet & Walter Sondheim") == 0) {
		  priority =  JWS;
	  }
	  else if(getBuilding().compareTo("Meyerhoff Chemistry") == 0) {
		  priority =  MC;
	  }
	  else if(getBuilding().compareTo("Sherman Hall") == 0) {
		  priority =  SH;
	  }
	  else if(getBuilding().compareTo("Biological Sciences") == 0) {
		  priority =  BS;
	  }
	  else if (getBuilding().compareTo("Math & Psychology") == 0) {
		  priority =  MP;
	  }
	  else if (getBuilding().compareTo("Fine Arts") == 0) {
		  priority =  FA;
	  }
	  else if (getBuilding().compareTo("Performing Arts & Humanities") == 0) {
		  priority =  PAH;
	  }
	  else if (getBuilding().compareTo("Physics") == 0) {
		  priority =  P;
	  }
	  else if (getBuilding().compareTo("University Center") == 0) {
		  priority =  UC;
	  }
	  else {
		  //there is no building
		  System.out.print("Error: Invalid building name in room info file: " + getBuilding() + " is invalid or doesn't host computer science classes");
		  ErrorMsg();
	  }
	  
	  return priority;
  }
}
