package dyn.utils;

/**
 * Created by OM on 27.12.2017.
 */
public class MailCounter {
    public int read;
    public int unread;

    public MailCounter() {
        read = 0;
        unread = 0;
    }

    public void incRead() {
        read++;
    }

    public void incUnread() {
        unread++;
    }
}
