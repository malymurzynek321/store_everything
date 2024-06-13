package org.example.store_everything.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Document(collection = "information")
public class Information {

    @Id
    private String id;

    private String ownerId;

    private String categoryId;

    @NotBlank
    @Size(min = 3, max = 20)
    private String title;

    @NotBlank
    @Size(min = 5, max = 500)
    private String content;

    private String url;

    @PastOrPresent
    private LocalDate creationDate;

    @PastOrPresent
    private LocalDate reminderDate;

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }
    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id='" + id + "'" +
                ", ownerId='" + ownerId + "'" +
                ", categoryId='" + categoryId + "'" +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                ", url='" + url + "'" +
                ", creationDate='" + creationDate + "'" +
                ", reminderDate='" + reminderDate + "'" +
                "}";
    }
}
