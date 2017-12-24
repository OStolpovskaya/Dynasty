package dyn.service;

import dyn.model.*;
import dyn.repository.TownNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static dyn.controllers.GameController.loc;

/**
 * Created by OM on 15.12.2017.
 */


@Service
public class TownNewsService {
    @Autowired
    MessageSource messageSource;
    @Autowired
    private TownNewsRepository townNewsRepository;

    public Page<TownNews> getNews(Pageable pageable) {
        Page<TownNews> news = townNewsRepository.findAll(pageable);
        return news;
    }

    public List<TownNews> getNews() {
        List<TownNews> news = townNewsRepository.findAllByOrderByDateDesc();
        return news;
    }

    public void addNewHouseNews(Family family) {
        String text = messageSource.getMessage("town.news.newHouse", new Object[]{family.link(), family.getHouse().getName()}, loc());
        saveNews(TownNewsType.newHouse, family, text);
    }

    public void addNewFamilyNews(Family family) {
        String text = messageSource.getMessage("town.news.newFamily", new Object[]{family.link()}, loc());
        saveNews(TownNewsType.newFamily, family, text);
    }

    public void addAchievementNews(Family family, Achievement achievement) {
        String text = messageSource.getMessage("town.news.achievement", new Object[]{family.link(), achievement.getName()}, loc());
        saveNews(TownNewsType.achievement, family, text);
    }

    public void addNewBuildingNews(Family family, House building) {
        String text = messageSource.getMessage("town.news.newBuilding", new Object[]{family.link(), building.getName()}, loc());
        saveNews(TownNewsType.newBuilding, family, text);
    }

    public void addCommonNews(Family family, String text) {
        saveNews(TownNewsType.common, family, text);
    }

    private void saveNews(TownNewsType newsType, Family family, String text) {
        if (family.getUser().getType().equals(UserType.player)) {
            TownNews townNews = new TownNews();
            townNews.setFamily(family);
            townNews.setType(newsType);
            townNews.setText(text);
            townNewsRepository.save(townNews);
        }
    }
}
