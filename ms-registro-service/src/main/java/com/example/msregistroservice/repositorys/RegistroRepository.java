package com.example.msregistroservice.repositorys;

import com.example.msregistroservice.models.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro, Long> {
}
