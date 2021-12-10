package uni.fin.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fin.data.entity.SamplePerson;


//crud operations; entity and primary key< , >; predefined methods; 
public interface SamplePersonRepository extends JpaRepository<SamplePerson, Integer> {

}