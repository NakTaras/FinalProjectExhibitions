package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.entity.Exhibition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;

public class AddExhibitionCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        Exhibition exhibition = new Exhibition();

        String[] locationsId = req.getParameterValues("chosenLocations");

        exhibition.setTopic(req.getParameter("topic"));
        exhibition.setDescription(req.getParameter("description"));
        exhibition.setStartTimestamp(Timestamp.valueOf(req.getParameter("startDate") + " " + req.getParameter("startTime") + ":00"));
        exhibition.setEndTimestamp(Timestamp.valueOf(req.getParameter("endDate") + " " + req.getParameter("endTime") + ":00"));
        exhibition.setPrice(Double.parseDouble(req.getParameter("price")));

        try {
            Part filePart = req.getPart("posterImg");
            InputStream fileContent = filePart.getInputStream();
            byte[] imgByteArray = new byte[fileContent.available()];
            fileContent.read(imgByteArray);
            exhibition.setPosterImg(imgByteArray);
        } catch (IOException | ServletException e) {
            System.out.println(e.getMessage());
            return "error.jsp";
        }

        System.out.println(exhibition);

        System.out.println(Arrays.toString(locationsId));

        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance();
        boolean isAdded = exhibitionDao.saveExhibition(exhibition, locationsId);

        Exhibition exhibition1 = exhibitionDao.getExhibitionById(exhibition.getId());

        if (isAdded){
            return "controller?command=getLocations";
        }

        return "error.jsp";
    }
}
