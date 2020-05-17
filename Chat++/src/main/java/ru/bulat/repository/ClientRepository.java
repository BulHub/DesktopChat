package ru.bulat.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ClientRepository implements CrudRepository{

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public Object find(String email) {
        return null;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public void update(Object entity) {

    }
}
