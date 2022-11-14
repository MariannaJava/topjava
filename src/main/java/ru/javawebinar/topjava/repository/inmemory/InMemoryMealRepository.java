package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;

public class InMemoryMealRepository implements MealRepository {


    private final Map<Integer,Map<Integer, Meal>> usersMealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, USER_ID));
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500), ADMIN_ID);

    }

    @Override
    public Meal save(Meal meal,int userId) {
        HashMap<Integer,Meal> meals=usersMealsMap.get(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id,int userId) {

        HashMap<Integer,Meal> meals=usersMealsMap.get(userId);
        return meals.remove(id) != null;
    }

    @Override
    public Meal get(int id,int userId) {
        HashMap<Integer,Meal> meals=usersMealsMap.get(userId);

        return meals.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {

        HashMap<Integer,Meal> meals=usersMealsMap.get(userId);
        return meals.values();
    }
}

