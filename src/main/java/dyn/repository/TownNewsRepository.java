package dyn.repository;

import dyn.model.Family;
import dyn.model.TownNews;
import dyn.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownNewsRepository extends PagingAndSortingRepository<TownNews, Long> {
    List<TownNews> findAllByFamilyUser(User userId);

    List<TownNews> findAllByFamily(Family family);

    List<TownNews> findAllByOrderByDateDesc();

    Page<TownNews> findAll(Pageable pageable);
}