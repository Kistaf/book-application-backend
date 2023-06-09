package dat3.security.service;

import dat3.book_app.entity.Member;
import dat3.security.dto.UserWithRolesRequest;
import dat3.security.dto.UserWithRolesResponse;
import dat3.security.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import dat3.security.repository.MemberRepository;

@Service
public class UserWithRolesService {

  private  final MemberRepository userWithRolesRepository;
  private PasswordEncoder passwordEncoder;

  public UserWithRolesService(MemberRepository userWithRolesRepository, PasswordEncoder passwordEncoder) {
    this.userWithRolesRepository = userWithRolesRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserWithRolesResponse getUserWithRoles(String id){
    var user = userWithRolesRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    return new UserWithRolesResponse(user);
  }

  //Make sure that this can ONLY be called by an admin
  public UserWithRolesResponse addRole(String username , Role role){
    var user = userWithRolesRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    user.addRole(role);
    return new UserWithRolesResponse(userWithRolesRepository.save(user));
  }

  //Make sure that this can ONLY be called by an admin
  public UserWithRolesResponse removeRole(String username , Role role){

    var user = userWithRolesRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    user.removeRole(role);
    return new UserWithRolesResponse(userWithRolesRepository.save(user));
  }

  //Only way to change roles is via the addRole method
  public UserWithRolesResponse editUserWithRoles(String username , UserWithRolesRequest body){
    var user = userWithRolesRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    user.setEmail(body.getEmail());
    user.setPassword(passwordEncoder.encode(body.getPassword()));
    return new UserWithRolesResponse(userWithRolesRepository.save(user));
  }

  /**
   *
   * @param body - the user to be added
   * @param role  - the role to be added to the user - pass null if no role should be added
   * @return the user added
   */
  public UserWithRolesResponse addUserWithRoles(UserWithRolesRequest body, Role role){
    if(userWithRolesRepository.existsById(body.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This user name is taken");
    }
    if(userWithRolesRepository.existsByEmail(body.getEmail())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This email is used by another user");
    }
    if(body.getPassword().isEmpty())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Empty password not allowed");
    String pw = passwordEncoder.encode(body.getPassword());
    var userWithRoles = new Member(body.getUsername(), pw, body.getEmail());
    if(role !=null  ) {
      userWithRoles.addRole(role);
    }
    return new UserWithRolesResponse(userWithRolesRepository.save(userWithRoles));
  }

}
