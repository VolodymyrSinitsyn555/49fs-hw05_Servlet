package app.repository;

import app.model.Car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRepositoryMap implements CarRepository {

    private Map<Long, Car> database = new HashMap<>();
    private long currentId;

    public CarRepositoryMap() {
        initData();
    }
    private void initData() {
        save(new Car("VW", new BigDecimal(150000), 2015));
        save(new Car("Mazda", new BigDecimal(300000), 2023));
        save(new Car("Ford", new BigDecimal(400000), 2024));
        save(new Car("ZaZ", new BigDecimal(900), 1991));
    }

    @Override
    public List<Car> getAll() {
//        return database.values().stream().toList();
        return new ArrayList<>(database.values());
    }

    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(car.getId(), car);
        return car;
    }

    @Override
    public Car findById(long id) {
        return database.getOrDefault(id, null);
    }

    @Override
    public Car update(Long id, Car car) {
        if(!database.containsKey(id)) {
            return null;
        }
        car.setId(id);
        database.put(id, car);
        return car;
    }

    @Override
    public boolean delete(Long id) {
        if (database.containsKey(id)) {
            database.remove(id);
            return true;
        }
        return false;


    }
}
