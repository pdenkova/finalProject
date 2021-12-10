package uni.fin.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fin.data.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}