package autoria.mapper;

import autoria.dto.UserDTO;
import autoria.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {


    UserDTO convertToDto(User user);

    User convertToUser(UserDTO userDTO);
}
