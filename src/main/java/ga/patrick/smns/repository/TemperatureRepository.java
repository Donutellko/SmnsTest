package ga.patrick.smns.repository;

import ga.patrick.smns.domain.Temperature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    /** Return list of temperature entries in reverse order, to
     * get N latest results using Pageable */
    List<Temperature> findByOrderByIdDesc(Pageable pageable);

    /** Return list of temperature entries in reverse order, to
     * get N latest results using Pageable */
    @Query("from Temperature t " +
            " join fetch t.location l " +
            " where lower(l.name) like :filter " +
            " order by t.id desc")
    List<Temperature> findFiltered(Pageable pageable, String filter);

}
