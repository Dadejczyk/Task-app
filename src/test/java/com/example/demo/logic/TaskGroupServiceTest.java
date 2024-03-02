package com.example.demo.logic;

import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import com.example.demo.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    @Test
    @DisplayName("should throw when undone tasks")
    void toogleGroup_throws_IllegalStateException() {
        //given
        TaskRepository mockTaskRepository = taksRepositoryRetunrning(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(-1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }
    @Test
    @DisplayName("should throw when no group")
    void toogleGroup_wrongId_throwsIllegalArgumentException() {
        //given
        TaskRepository mockTaskRepository = taksRepositoryRetunrning(false);
        //and
       var mockRepository = mock(TaskGroupRepository.class);
       when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
       //system under test
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(-1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }
    @Test
    @DisplayName("should toogle group")
    void toogleGroup_worksAsExpected() {
        //given
        TaskRepository mockTaskRepository = taksRepositoryRetunrning(false);
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        //and
        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //system under test
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        //when
        toTest.toggleGroup(0);
        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taksRepositoryRetunrning(boolean result) {
        var  mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}


