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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;



public class MealController extends HttpServlet {

    private static final Logger log = getLogger(MealController.class);

    private final static String INSERT_OR_EDIT = "/meal.jsp";
    private final static String LIST_MEAL = "/meals.jsp";
    private CopyOnWriteArrayList<Meal> meals;
    private AtomicInteger id;

    @Override
    public void init() {

        Object meals = getServletContext().getAttribute("meals");

        if ( !(meals instanceof CopyOnWriteArrayList)) {

            throw new IllegalStateException("You're repo does not initialize!");
        } else {

            this.meals = (CopyOnWriteArrayList<Meal>) meals;
        }
        Object id=getServletContext().getAttribute("id");
        if (!(id instanceof AtomicInteger)) {

            throw new IllegalStateException("You're repo does not initialize!");
        } else {

            this.id = (AtomicInteger) id;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");


        List<MealTo> mealsTo=MealsUtil
                .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        if(action==null || action.equalsIgnoreCase("listMeal")){
            forward = LIST_MEAL;

            request.setAttribute("mealsTo", mealsTo);
        } else if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            meals.remove(mealId-1);

            //mealsTo=MealsUtil
             //       .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            //forward = LIST_MEAL;
            response.sendRedirect("MealController?action=listMeal");
            log.debug("redirect to MealController?action=listMeal");
            //request.setAttribute("mealsTo", mealsTo);
            log.debug("Set attribute mealsTo for meals.jsp");
            return;
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = meals.get(mealId-1);
            request.setAttribute("meal", meal);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
       log.debug("forward ");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal=new Meal();
        String mealid = request.getParameter("mealId");
        DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if(mealid == null || mealid.isEmpty()) {
            meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime"), DATEFORMATTER),//request.getParameter("dateTime),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    id.get());
        meals.add(meal);
        } else{
            meal.setId(Integer.parseInt(request.getParameter("mealId")));
            meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime"), DATEFORMATTER));
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));
            Meal mealFind=meals.stream()
                    .filter(s->s.getId()==Integer.parseInt(request.getParameter("mealId")))
                            .findAny()
                    .get();

            meals.set(meals.indexOf(mealFind),meal);

        }
        List<MealTo> mealsTo=MealsUtil
                .filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("mealsTo", mealsTo);
        view.forward(request, response);
    }

}
