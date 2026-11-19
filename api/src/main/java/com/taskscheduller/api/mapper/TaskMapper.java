package com.taskscheduller.api.mapper;

import com.taskscheduller.api.dto.TaskDto;
import com.taskscheduller.api.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
	TaskDto toDto(Task task);

	Task toEntity(TaskDto dto);
}
