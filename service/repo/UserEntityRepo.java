package uni.fin.data.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.fin.data.entity.UserEntity;

public interface UserEntityRepo extends JpaRepository<UserEntity, Integer> {

	UserEntity findByUsername(String name);

}
