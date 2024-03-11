package com.example.demo.model.projection;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadline")
    void constructor_noDeadlines_createsNullDeadline() {
        //
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));
        //when
        var result = new GroupReadModel(source);

        // then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}