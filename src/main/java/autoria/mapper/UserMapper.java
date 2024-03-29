package autoria.mapper;

import autoria.dto.UserDTO;
import autoria.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface UserMapper {


    UserDTO convertToDto(User user);

    User convertToUser(UserDTO userDTO);

}
