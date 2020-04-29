package bean;

import org.springframework.stereotype.Component;

import java.util.Date;

public class SampleBean {
    private int id;
    private Date createdAt;
    private String uploadedBy;

    public SampleBean() {
    }

    public SampleBean(int id, Date createdAt, String uploadedBy) {
        this.id = id;
        this.createdAt = createdAt;
        this.uploadedBy = uploadedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
