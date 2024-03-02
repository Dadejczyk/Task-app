package com.example.demo.model.projection;


import com.example.demo.model.TaskGroup;


import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup() {
        TaskGroup result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        return result;
    }
}