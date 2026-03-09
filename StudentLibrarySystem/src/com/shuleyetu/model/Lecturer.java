package com.shuleyetu.model;
import java.sql.Date;
import java.util.List;
public class Lecturer extends Person{

private int lecturerId;
private String staffNumber;
private Integer departmentId;
private String qualification;
private String specialization;
private Date hireDate;
private boolean isActive;

private String departName;
private List<CourseOffering> courseOfferings


public Lecturer(){
    super();
    this.role="LECTURER";
}

// public Lecturer(int lecturerId,String staffNumber,Integer departmentId,String qualification,String specializaion,Date hireDate){
//     this.lecturerId=lecturerId;
//     this.staffNumber=staffNumber;
//     this.departmentId=departmentId;
// }

public int getLecturerId(){ return lecturerId; }
public void setLecturerId(int lecturerId){
    this.lecturerId=lecturerId;
    super.setPersonId(lecturerId);
}

public String getStaffNumber(){ return staffNumber; }
public void setStaffNumber(String staffNumber){ 
    this.staffNumber=staffNumber;
}

public Integer getDepartmentId(){ return departmentId; }
public void setDepartmentId(Integer departmentId){
    this.departmentId=departmentId;
}

public String getQualification(){ return qualification; }
public void setQualification(String qualification){
    this.qualification=qualification;
}

public String getSpecialization(){ return specialization;}
public void setSpecialization(String specialization){ 
    this.specialization=specialization;
}

public Date getHireDate(){ return hireDate; }
public void setHireDate(Date hireDate){
    this.hireDate=hireDate;
}

public boolean isActive(){ return isActive;}
public void setActive(boolean active ){
    this.isActive=active;
}

public String getDepartmentName(){ return departName;}
public void setDepartmentName(String departName){
    this.departName=departName;
}

  @Override
    public String toString() {
        return "Lecturer{" +
               "lecturerId=" + lecturerId +
               ", name='" + getName() + '\'' +
               ", staffNumber='" + staffNumber + '\'' +
               ", departmentId=" + departmentId +
               '}';
    }

}