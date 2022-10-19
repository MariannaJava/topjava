package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);


    private CopyOnWriteArrayList<Meal> meals;

    @Override
    public void init() throws ServletException {

        final Object meals = getServletContext().getAttribute("meals");

        if (meals == null || !(meals instanceof CopyOnWriteArrayList)) {

            throw new IllegalStateException("You're repo does not initialize!");
        } else {

            this.meals = (CopyOnWriteArrayList<Meal>) meals;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        List<MealTo> mealsTo=MealsUtil.filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("mealsTo", mealsTo);

        request.getRequestDispatcher("meals.jsp").forward(request, response);
        //response.sendRedirect("meals.jsp");
        log.debug("redirect to meals");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
