package services.tiempo;

import exceptions.ClimaException;
import modelos.Clima;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TiempoService {

        List<Clima> findAll() throws SQLException;

        List<Clima> findAllByProvincia(String provincia) throws SQLException;

        Optional<Clima> findById(UUID id) throws SQLException;

        Clima save(Clima clima) throws SQLException, ClimaException;

        Clima update(Clima clima) throws SQLException, ClimaException;

        boolean deleteById(UUID id) throws SQLException;

        void deleteAll() throws SQLException;
    }
}
