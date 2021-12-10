package uni.fin.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import uni.fin.data.entity.SamplePerson;

//wrapper of repository; need one more service layer; CrudService = ready service methods
@Service
public class SamplePersonService extends CrudService<SamplePerson, Integer> {

    private SamplePersonRepository repository;

    public SamplePersonService(@Autowired SamplePersonRepository repository) {
        this.repository = repository;
    }

    @Override
    protected SamplePersonRepository getRepository() {
        return repository;
    }

}
