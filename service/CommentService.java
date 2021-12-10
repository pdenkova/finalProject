package uni.fin.data.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import uni.fin.data.entity.CommentEntity;
import uni.fin.data.service.repo.CommentEntityRepo;

@Service
public class CommentService extends CrudService<CommentEntity, Integer> {

	
    private CommentEntityRepo repository;

    public CommentService(@Autowired CommentEntityRepo repository) {
        this.repository = repository;
    }

    @Override
    protected CommentEntityRepo getRepository() {
        return repository;
    }
    
    public Collection<CommentEntity>findAll(){
    	return repository.findAll();
    }

}
