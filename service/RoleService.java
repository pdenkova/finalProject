package uni.fin.data.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import uni.fin.data.entity.RoleEntity;
import uni.fin.data.service.repo.RoleRepo;

@Service
public class RoleService extends CrudService<RoleEntity, Integer> {

	 private RoleRepo repository;

	    public RoleService(@Autowired RoleRepo repository) {
	        this.repository = repository;
	    }

	    @Override
	    protected RoleRepo getRepository() {
	        return repository;
	    }
	    
	    public Collection<RoleEntity>findAll(){
	    	return repository.findAll();
	    }

	}

