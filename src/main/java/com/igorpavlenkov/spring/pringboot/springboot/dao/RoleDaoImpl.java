package com.igorpavlenkov.spring.pringboot.springboot.dao;

import com.igorpavlenkov.spring.pringboot.springboot.model.Role;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteRoleById(Long id) {
        Role role = getRoleById(id);
        entityManager.remove(role);
        System.out.println("Пользователь удален " + role);
    }

    public Role getRoleByName(String name) {
        return entityManager.unwrap(Session.class).createQuery("from Role where name = '" + name + "'", Role.class).getSingleResult();
    }


    public Role getRoleById(Long id) {
        return entityManager.unwrap(Session.class).createQuery("from Role where id = '" + id + "'", Role.class).getSingleResult();
    }

    public void createRoles(String roleName) {
        entityManager.persist(new Role(roleName));
    }
}
