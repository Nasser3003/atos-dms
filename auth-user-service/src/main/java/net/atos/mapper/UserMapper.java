package net.atos.mapper;

import net.atos.dto.UserReadDto;
import net.atos.model.UserEntity;

public class UserMapper {

    public static UserReadDto toUserReadDto(UserEntity user) {
        return new UserReadDto(user.getEmail(), user.getNationalId());
    }
}