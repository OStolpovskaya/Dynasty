package dyn.repository;

import dyn.model.Feedback;
import dyn.model.FeedbackType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
    List<Feedback> findByType(FeedbackType type);

    List<Feedback> findByTypeOrderByStatusAscDateDesc(FeedbackType type);
}