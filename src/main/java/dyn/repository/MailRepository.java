package dyn.repository;

import dyn.model.Mail;
import dyn.model.MailStatus;
import dyn.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends CrudRepository<Mail, Long> {
    @Query("select m from Mail m where ((m.from=?1 AND m.to=?2) OR (m.from=?2 AND m.to=?1)) AND m.status=?3 order by m.date desc")
    List<Mail> findAllByFromOrToAndStatusOrderByDateDesc(User user1, User user2, MailStatus mailStatus);

    @Query("select m from Mail m where m.from=?1 OR m.to=?1 order by m.date desc")
    List<Mail> findAllMailOfUser(User user);

    @Query("select m from Mail m where m.to=?1 and m.status='unread'")
    List<Mail> hasNewMail(User user);
}
