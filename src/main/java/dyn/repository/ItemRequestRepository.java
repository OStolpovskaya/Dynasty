package dyn.repository;

import dyn.model.Family;
import dyn.model.ItemRequest;
import dyn.model.ItemRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends CrudRepository<ItemRequest, Long> {
    List<ItemRequest> findAll();

    List<ItemRequest> findAllByFamilyOrderByDateDesc(Family family);

    Page<ItemRequest> findAllByStatus(Pageable pageable, ItemRequestStatus itemRequestStatus);
}