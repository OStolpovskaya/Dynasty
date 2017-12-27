package dyn.service;

import dyn.model.Mail;
import dyn.model.MailStatus;
import dyn.model.User;
import dyn.repository.MailRepository;
import dyn.utils.MailCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OM on 26.12.2017.
 */
@Service
public class MailService {
    @Autowired
    MailRepository mailRepository;

    public List<Mail> showUnreadChat(User user, User player) {
        List<Mail> chat = mailRepository.findAllByFromOrToAndStatusOrderByDateDesc(user, player, MailStatus.unread);
        for (Mail mail : chat) {
            if (mail.getTo() == user) {
                mail.setStatus(MailStatus.read);
            }
        }
        mailRepository.save(chat);
        return chat;
    }

    public List<Mail> showReadChat(User user, User player) {
        List<Mail> chat = mailRepository.findAllByFromOrToAndStatusOrderByDateDesc(user, player, MailStatus.read);
        return chat;
    }

    public void save(Mail newMail) {
        mailRepository.save(newMail);
    }


    public Map<User, MailCounter> countMailOfUser(User user) {
        Map<User, MailCounter> map = new LinkedHashMap<>();
        List<Mail> mailOfUser = mailRepository.findAllMailOfUser(user);
        for (Mail mail : mailOfUser) {
            User player;
            if (mail.getFrom() == user) {
                player = mail.getTo();
            } else {
                player = mail.getFrom();
            }
            if (!map.containsKey(player)) {
                map.put(player, new MailCounter());
            }
            MailCounter mailCounter = map.get(player);
            MailStatus status = mail.getStatus();
            if (status.equals(MailStatus.unread) && mail.getTo() == user) {
                mailCounter.incUnread();
            } else {
                mailCounter.incRead();
            }
        }
        return map;
    }

    public boolean hasNewMail(User user) {
        List<Mail> mailOfUser = mailRepository.hasNewMail(user);
        if (mailOfUser.size() > 0) {
            return true;
        }
        return false;
    }
}
