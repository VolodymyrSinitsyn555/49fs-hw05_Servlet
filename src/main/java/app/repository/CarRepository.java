package app.repository;

import app.model.Car;

import java.util.List;

public interface CarRepository {

    List<Car> getAll();

    Car save(Car car);

    Car findById(long id);

    Car update(Long id, Car car);

    boolean delete(Long id);
}
