package com.tmgreyhat.esbobi.repositories;

import com.tmgreyhat.esbobi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
