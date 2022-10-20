package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;



public class MealController extends HttpServlet {

    private static final Logger log = getLogger(MealController.class);

    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private CopyOnWriteArrayList<Meal> meals;
    private AtomicInteger id;

    @Override
    public void init() throws ServletException {

        Object meals = getServletContext().getAttribute("meals");

        if (meals == null || !(meals instanceof CopyOnWriteArrayList)) {

            throw new IllegalStateException("You're repo does not initialize!");
        } else {

            this.meals = (CopyOnWriteArrayList<Meal>) meals;
        }
        Object id=getServletContext().getAttribute("id");
        if (id == null || !(id instanceof AtomicInteger)) {

            throw new IllegalStateException("You're repo does not initialize!");
        } else {

            this.id = (AtomicInteger) id;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward="";
        String action = request.getParameter("action");


        List<MealTo> mealsTo=MealsUtil
                .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        if(action==null || action.equalsIgnoreCase("listMeal")){
            forward = LIST_MEAL;

            request.setAttribute("mealsTo", mealsTo);
        } else if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            meals.remove(mealId-1);

            mealsTo=MealsUtil
                    .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", mealsTo);
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            MealTo mealTo = mealsTo.get(mealId);
            request.setAttribute("mealTo", mealTo);

        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
        /*
        List<MealTo> mealsTo=MealsUtil.filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("mealsTo", mealsTo);

        request.getRequestDispatcher("meals.jsp").forward(request, response);*/
        //response.sendRedirect("meals.jsp");
        log.debug("redirect to meals");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Meal meal=new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),//request.getParameter("dateTime),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                id.get()
                );

        /*User user = new User();
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        try {
            Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(request.getParameter("dob"));
            user.setDob(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setEmail(request.getParameter("email"));
        String userid = request.getParameter("userid");
        if(userid == null || userid.isEmpty())
        {
            dao.addUser(user);
        }
        else
        {
            user.setUserid(Integer.parseInt(userid));
            dao.updateUser(user);
        }*/


        meals.add(meal);
        List<MealTo> mealsTo=MealsUtil
                .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("mealsTo", mealsTo);
        view.forward(request, response);
    }

}

/*

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

}
*/

