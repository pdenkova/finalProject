package uni.fin.data.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.github.jknack.handlebars.internal.lang3.StringUtils;

import uni.fin.data.entity.UserEntity;
import uni.fin.data.service.repo.UserEntityRepo;

@Service
public class UserEnService extends CrudService<UserEntity, Integer>{
	private UserEntityRepo repository;

    public UserEnService(@Autowired UserEntityRepo repository) {
        this.repository = repository;
    }

    @Override
    protected UserEntityRepo getRepository() {
        return repository;
    }

	public Collection<UserEntity> findAll() {

		return repository.findAll();
	}
	public void saveAll(List<UserEntity> users) {
		repository.saveAll(users);		
	}
	
	public Stream<UserEntity> fetchItems(String filter, int offset, int limit) {
		return repository.findAll().stream().filter(u -> StringUtils.containsIgnoreCase(u.getUsername(), filter));

	}
	public int count (String filter){
		return (int) repository.findAll().stream().filter(u -> StringUtils.containsIgnoreCase(u.getUsername(), filter)).count();
	}

}
