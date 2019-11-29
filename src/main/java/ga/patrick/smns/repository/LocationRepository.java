package ga.patrick.smns.repository;

import ga.patrick.smns.domain.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

    /** Find location by comparing provided coordinates with boundaries. */
    @Query("from Location l \n" +
            " where l.bottom <= :lat and :lat <= l.top \n" +
            " and l.left <= :lon and :lon <= l.right \n" +
            " order by (l.top - l.bottom) * (l.right - l.left) asc")
    Location findTopBy(double lat, double lon);

}
