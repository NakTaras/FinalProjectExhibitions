package com.my.db.dao;

import com.my.db.entity.Exhibition;
import com.my.exception.DaoException;

import java.sql.Connection;
import java.sql.Date;

import java.util.List;
import java.util.Map;

/**
 * Interface ExhibitionDao is used to access information about exhibitions from the database.
 *
 * @author Taras Nakonechnyi
 */
public interface ExhibitionDao {

    /**
     * The method adds information about the exhibitions to the database.
     *
     * @param exhibition  - information about the exhibitions that needs to be saved in the database.
     * @param locationsId - locations id where the exhibition takes place.
     * @throws DaoException when the exhibitions could not be saved to the database.
     */
    void saveExhibition(Exhibition exhibition, String[] locationsId) throws DaoException;

    /**
     * The method gets information about the exhibition from the database by id.
     *
     * @param id - exhibition id.
     */
    Exhibition getExhibitionById(long id);

    /**
     * The method gets information about exhibitions from the database.
     * The method returns a certain number of exhibitions sorted by default that will be placed on the page.
     * Exhibitions have information about id, topic, description, start date, end date, tickets price, poster and status.
     *
     * @param pageNum - page number.
     * @throws DaoException when the exhibitions could not be gotten from the database.
     */
    List<Exhibition> getExhibitionsOnPageByDefault(int pageNum) throws DaoException;

    /**
     * The method gets information about exhibitions from the database.
     * The method returns a certain number of exhibitions sorted by price that will be placed on the page.
     * Exhibitions have information about id, topic, description, start date, end date, tickets price, poster and status.
     *
     * @param pageNum - page number.
     * @throws DaoException when the exhibitions could not be gotten from the database.
     */
    List<Exhibition> getExhibitionsOnPageByPrice(int pageNum) throws DaoException;

    /**
     * The method gets information about exhibitions from the database.
     * The method returns a certain number of exhibitions sorted by topic that will be placed on the page.
     * Exhibitions have information about id, topic, description, start date, end date, tickets price, poster and status.
     *
     * @param pageNum - page number.
     * @throws DaoException when the exhibitions could not be gotten from the database.
     */
    List<Exhibition> getExhibitionsOnPageByTopic(int pageNum) throws DaoException;

    /**
     * The method gets information about exhibitions from the database.
     * The method returns a certain number of exhibitions filtered by date that will be placed on the page.
     * Exhibitions have information about id, topic, description, start date, end date, tickets price, poster and status.
     *
     * @param pageNum    - page number.
     * @param chosenDate - date that user choose.
     * @throws DaoException when the exhibitions could not be gotten from the database.
     */
    List<Exhibition> getExhibitionsOnPageByDate(int pageNum, Date chosenDate) throws DaoException;

    /**
     * The method change exhibition status in the database.
     *
     * @param id - exhibitions id.
     * @throws DaoException when the exhibitions could not be canceled.
     */
    void cancelExhibitionById(long id) throws DaoException;

    /**
     * The method gets the number of all exhibitions from the database.
     *
     * @throws DaoException when the number of all exhibitions could not be gotten from the database.
     */
    int getAmountOfExhibitions() throws DaoException;

    /**
     * The method gets the number of exhibitions filtered by date from the database.
     *
     * @param chosenDate - date that user choose.
     * @throws DaoException when the number of exhibitions could not be gotten from the database.
     */
    int getAmountOfExhibitionsByDate(Date chosenDate) throws DaoException;

    /**
     * The method gets the number of tickets sold for the exhibition from the database.
     *
     * @param exhibitionId - exhibition id.
     */
    int getAmountOfSoldTicketsByExhibitionId(long exhibitionId);

    /**
     * The method gets information about exhibitions statistics from the database.
     * Exhibitions have information about id, topic, start date, end date and tickets price.
     *
     * @throws DaoException when the exhibitions` statistics could not be gotten from the database.
     */
    List<Exhibition> getExhibitionsStatistics() throws DaoException;

    /**
     * The method gets information about detailed exhibition statistics from the database.
     * Exhibitions have information about id, topic, start date, end date and tickets price.
     *
     * @throws DaoException when the exhibitions` statistics could not be gotten from the database.
     * @return map with user login and amount of bought tickets.
     */
    Map<String, Integer> getDetailedStatisticsByExhibitionId(long exhibitionId) throws DaoException;

    /**
     * The method gets information about all exhibitions from the database.
     * Exhibitions have information about id, topic, description, start date, end date, tickets price, poster and status.
     *
     * @throws DaoException when the exhibitions could not be gotten from the database.
     */
    List<Exhibition> getAllExhibitions() throws DaoException;
}
