package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.dto.UserReadDto;
import net.atos.mapper.UserMapper;
import net.atos.repository.UserRepository;
import net.atos.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {

    private final UserRepository userRepository;
    private final Util util;

    public List<UserReadDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserReadDto).collect(Collectors.toList());
    }
    public UserReadDto getUserByEmail(String email) {
        return UserMapper.toUserReadDto(util.findUserByEmail(email));
    }
}
