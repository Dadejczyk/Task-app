package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.model.*;
import com.example.demo.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the undone group exist")// definiuje jak ten test będzie wyswietlany w raportach
    void createGroup_noMultipleGroupsConfig_And_openGroupsExists_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockGroupRepository = GroupRepositoryReturning(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        var toTest = new ProjectService(null, mockGroupRepository,null,mockConfig);

        //when
        var exception = catchThrowable(()-> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for given id")// definiuje jak ten test będzie wyswietlany w raportach
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system undone test
        var toTest = new ProjectService(mockRepository, null,null,mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects for a given id")// definiuje jak ten test będzie wyswietlany w raportach
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupsExists_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository mockGroupRepository =GroupRepositoryReturning(false);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system undone test
        var toTest = new ProjectService(mockRepository,mockGroupRepository, null,mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }
    @Test
    @DisplayName("sould create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();

        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        // system undone test
        var toTest = new ProjectService(mockRepository,inMemoryGroupRepo,serviceWithInMemRepo,mockConfig);

        //when
        GroupReadModel result = toTest.createGroup(today, 1);
        //then

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
       assertThat(countBeforeCall  + 1).isEqualTo(inMemoryGroupRepo.count());
    }
    private TaskGroupService dummyGroupService(final InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }
    private Project projectWith(String projectDescription, Set<Integer> daysToDeadLine) {
         Set<ProjectStep> steps = daysToDeadLine.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private static TaskGroupRepository GroupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private static TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        // and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }
    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository{
            private int index = 0;
            private Map<Integer,TaskGroup> map = new HashMap<>();

            public int count() {
               return map.values().size();
            }

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if (entity.getId() == 0) {
                    try {
                        var field = TaskGroup.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, ++index);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);

                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(group -> !group.isDone())
                        .anyMatch(group -> group.getProject() != null & group.getProject().getId() == projectId);
            }

        @Override
        public boolean existsByDescription(String description) {
            return  map.values().stream()
                    .anyMatch(group -> group.getDescription().equals(description));
        }
    };
}