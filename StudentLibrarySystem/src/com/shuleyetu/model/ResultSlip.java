public class ResultSlip {
    private int resultSlipId;
    private int studentId;
    private int semesterId;
    private double gpa;
    private int totalCredits;
    private String status;  // "DRAFT", "PUBLISHED"
    private Timestamp generatedDate;
    private Timestamp publishedDate;
    private Integer publishedBy;
    
    // Relationships
    private Student student;
    private Semester semester;
    private Admin publisher;
    private List<ResultDetail> details;
    
    // getters/setters
}