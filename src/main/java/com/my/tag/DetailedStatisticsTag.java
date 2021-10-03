package com.my.tag;

import com.my.db.dao.impl.LocationDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Map;

public class DetailedStatisticsTag extends BodyTagSupport {

    private static final Logger logger = LogManager.getLogger(DetailedStatisticsTag.class);

    private static final long serialVersionUID = -3967298211706329499L;

    private Map<String, Integer> detailedStatistics;

    public void setDetailedStatistics(Map<String, Integer> detailedStatistics) {
        this.detailedStatistics = detailedStatistics;
    }

    public int doStartTag() throws JspTagException {
        try {
            pageContext.getOut().write("<table class=\"detailed-statistic\">");
            pageContext.getOut().write("<tr>");
        } catch (IOException e) {
            throw new JspTagException(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doAfterBody() throws JspTagException {
        try {
            pageContext.getOut().write("</tr>");
            detailedStatistics.forEach((key, value) -> {
                try {
                    pageContext.getOut().write("<tr><td>" + key + "</td><td>" + value + "</td></tr>");
                } catch (IOException e) {
                    logger.error(e);
                }
            });
        } catch (IOException e) {
            logger.error(e);
            throw new JspTagException(e.getMessage());
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException {
        try {
            pageContext.getOut().write("</table>");
        } catch (IOException e) {
            logger.error(e);
            throw new JspTagException(e.getMessage());
        }
        return SKIP_BODY;
    }

}
