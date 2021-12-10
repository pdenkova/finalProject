package uni.fin.data.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.fin.data.entity.CommentEntity;

public interface CommentEntityRepo extends JpaRepository<CommentEntity, Integer>{

	
}
