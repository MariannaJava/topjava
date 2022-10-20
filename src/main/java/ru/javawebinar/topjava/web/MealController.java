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



public class MealController extends HttpServlet {

    private static final Logger log = getLogger(MealController.class);

    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
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
        String forward="";
        String action = request.getParameter("action");


        List<MealTo> mealsTo=MealsUtil
                .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.deleteUser(mealId);
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", mealsTo);
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealsTo.get(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listUser")){
            forward = LIST_MEAL;

            request.setAttribute("mealsTo", mealsTo);
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

    }
}

/*

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
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
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("users", dao.getAllUsers());
        view.forward(request, response);
    }
}
*/

