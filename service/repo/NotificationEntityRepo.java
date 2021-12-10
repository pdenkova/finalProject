package uni.fin.data.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.fin.data.entity.NotificationEntity;

public interface NotificationEntityRepo extends JpaRepository<NotificationEntity, Integer> {

}
