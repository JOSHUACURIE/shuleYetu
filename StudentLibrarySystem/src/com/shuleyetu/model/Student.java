package com.shuleyetu.model;
import java.sql.Date;
import java.util.List;


public class Student extends Person{
private int studentId;
private String registrationNumber;
private int programmeId;
private Date enrollmentDate;
private int currentYear;
private String status;



//the additional fields for joins

private String programmeName;
private List<Enrollment> enrollments;


public Student(){
super();
this.role="STUDENT";
}

//getters and the setters


public int getStudentId(){ return studentId;}
public void setStudentId(int studentId){
    this.studentId=studentId;
    super.setPersonId(studentId);
}

public String getRegistrationNumber(){  return registrationNumber; }
public void setRegistrationNumber(String registrationNumber){
    this.registrationNumber=registrationNumber;
}


public int getProgrammeId(){  return programmeId; }
public void setProgrammeId(int programmeId){
    this.programmeId=programmeId;
}


public Date getEnrollmentDate(){
     return enrollmentDate;
}
public void setEntollmentId(Date enrollmentDate){
this.enrollmentDate=enrollmentDate;
}


public int getCurrentYear(){  return currentYear;  }

public void setCurrentYear(int currentYear){
    this.currentYear=currentYear;
}


public String getStatus(){ return status; }
public void setStatus(String status){
    this.status=status;

}


public String getProgrammeName(){ return programmeName; }
public void setProgrammeName(String programmeName){
    this.programmeName=programmeName;
}

public List<Enrollment> getEnrollments(){
    return enrollments;
}
public void setEnrollments(List<Enrollment> enrollments){
    this.enrollments=enrollments;
}

  @Override
    public String toString() {
        return "Student{" +
               "studentId=" + studentId +
               ", name='" + getName() + '\'' +
               ", regNumber='" + registrationNumber + '\'' +
               ", programmeId=" + programmeId +
               '}';
    }
}