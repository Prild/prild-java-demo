package com.prild.cloud.microservicesimpleprovideruser.dao;

import com.prild.cloud.microservicesimpleprovideruser.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long>{

}
