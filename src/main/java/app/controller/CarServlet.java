package app.controller;

import app.model.Car;
import app.repository.CarRepository;
import app.repository.CarRepositoryMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarServlet extends HttpServlet {

    private CarRepository repository = new CarRepositoryMap();
    ObjectMapper mapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String[]> params = req.getParameterMap();

        if (params.isEmpty()) {
            List<Car> cars = repository.getAll();

            resp.setContentType("application/json");

            String json = mapper.writeValueAsString(cars);
            resp.getWriter().write(json);
        } else {
            String idStr = params.get("id")[0];
            Long id = Long.parseLong(idStr);
            Car car = repository.findById(id);
            if (car == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Car not found");
            } else {
                String jsonResponse = mapper.writeValueAsString(car);
                resp.getWriter().write(jsonResponse);
            }
        }


//        cars.forEach(car -> {
//            try {
//                resp.getWriter().write(car.toString() + "\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            System.out.println("doPut method invoked");

            Car car = mapper.readValue(req.getReader(), Car.class);
            System.out.println("Parsed car: " + car);

            String idStr = req.getParameter("id");

            if (idStr == null || idStr.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("ID is required");
                return;
            }

            Long id = Long.parseLong(idStr);
            Car carToUpdate = repository.update(id, car);

            if (carToUpdate == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Car not found");
            } else {
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(carToUpdate));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Internal server error" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");

            if (idStr == null || idStr.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("ID is required");
                return;
            }

            Long id = Long.parseLong(idStr);
            boolean isDeleted = repository.delete(id);


            if (isDeleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Car not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Internal server error" + e.getMessage());
        }
    }
}
