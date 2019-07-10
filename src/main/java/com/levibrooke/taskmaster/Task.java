package com.levibrooke.taskmaster;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "TaskMaster")
public class Task {
    String id;
    String title;
    String description;
    String status;
    String assignee;
    String pic;

    public Task() {}
    public Task(String title, String description, String status, String assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
    }
    public Task(String title, String description, String status, String assignee, String pic) {
        this(title, description, status, assignee);
        this.pic = pic;
    }

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    @DynamoDBAttribute
    public String getTitle() {
        return title;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    @DynamoDBAttribute
    public String getStatus() {
        return status;
    }

    @DynamoDBAttribute
    public String getAssignee() {
        return assignee;
    }

    @DynamoDBAttribute
    public String getPic() {
        return pic;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean assigneeIsEmpty() {
        if (this.getAssignee() == "" || this.getAssignee() == null) {
            return true;
        }
        return false;
    }
}
