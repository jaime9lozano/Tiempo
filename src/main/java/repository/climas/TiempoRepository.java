package repository.climas;
import exceptions.ClimaException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import modelos.Clima;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;


public interface TiempoRepository {
    List<Clima> findByProvincia(String nombre) throws SQLException;
}

