package model;

public class GetProfileResponse {
    private String status;
    private String message;
    private String name;
    private String gender;
    private String text;
    private String school;
    private String identity;
    private String phone;

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getText() {
        return text;
    }

    public String getSchool() {
        return school;
    }

    public String getIdentity() {
        return identity;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
