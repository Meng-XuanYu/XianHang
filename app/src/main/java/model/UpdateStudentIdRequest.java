package model;

public class UpdateStudentIdRequest {
    private String userId;
    private String studentId;

    public UpdateStudentIdRequest(String userId, String studentId) {
        this.userId = userId;
        this.studentId = studentId;
    }
}
