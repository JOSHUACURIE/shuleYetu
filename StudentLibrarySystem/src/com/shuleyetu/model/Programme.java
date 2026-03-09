package com.shuleyetu.model;
import java.sql.Timestamp;
public class Programme {
    private int programmeId;
    private String programmeCode;
    private String programmeName;
    private int durationYears;
    private String description;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Relationships
    private List<Student> students;
    private List<Course> courses;  // via programme_course
    
  public Programme(){

  }

  public int getProgrammeId(){ return programmeId;}
  public void setProgrammeId(int programmeId){
    this.programmeId=programmeId;
  }

  public String getProgrammeName(){ return programmeName;}
  public void setProgrammeName(String programmeName){
    this.programmeName=programmeName;
  }

 public String getProgrammeCode(){ return programmeCode;}
 public void setProgrammeCode(String programmeCode){
    this.programmeCode=programmeCode;
 }

 public int getDurationYears(){ return durationYears;}
 public void setDurationYears(int durationYears){
    this.durationYears=durationYears;
 }

public String getDescription(){
    return description;
}
public void setDescription(String description){
    this.description=description;
}

  public boolean isActive(){ return isActive; }
  public void setActive(boolean active){
    this.isActive=active;
  }

  public Timestamp getCreatedAt(){ return createdAt;}
  public void setCreatedAt(Timestamp createdAt){
    this.createdAt=createdAt;
  }

   @Override
    public String toString() {
        return "Programme{" +
               "programmeId=" + programmeId +
               ", programmeName='" + programmeName + '\'' +
               ", programmeCode='" + programmeCode + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}