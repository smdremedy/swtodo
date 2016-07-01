package com.byoutline.todoekspert.api;

public class TodoFromApi {

    public String content;
    public boolean done;
    public String createdAt;
    public String updatedAt;
    public String objectId;
    public String userId;


    @Override
    public String toString() {
        return "TodoFromApi{" +
                "content='" + content + '\'' +
                ", done=" + done +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
