package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.UserDto;

public interface UserService {

    UserDto findByUsername(String username);

    Page<UserDto> getAllUsers(Pageable pageable);
}
