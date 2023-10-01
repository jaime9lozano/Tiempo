package repository.climas;
import exceptions.ClimaException;
import exceptions.ClimaNoEncontradoException;
import exceptions.ClimaNoAlmacenadoException;

import modelos.Clima;
import services.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public class TiempoRepositoryImpl implements TiempoRepository{

// Esta es la clase que se encarga de la persistencia de los alumnos

    // Singleton
    private static TiempoRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(TiempoRepositoryImpl.class);
    // Base de datos
    private final DatabaseManager db;

    private TiempoRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }


    public static TiempoRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new TiempoRepositoryImpl(db);
        }
        return instance;
    }

    @Override
    public List<Clima> findAll() throws SQLException {
        logger.debug("Obteniendo todos los climas");
        var query = "SELECT * FROM TIEMPOS";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            var lista = new ArrayList<Clima>();
            while (rs.next()) {
                Clima c= Clima.builder()
                        .codigo(rs.getObject("codigo", UUID.class))
                        .localidad(rs.getString("localidad")
                        .provincia(rs.getString("provincia")
                        .tempMax(rs.getDouble("tempMax"))
                        .tempMin(rs.getDouble("tempMin"))
                        .horaTMax(rs.getObject("horaTMax"), LocalDateTime.class)
                        .horaTemMin(rs.getObject("horaTemMin"), LocalDateTime.class)
                        .precipitacion(rs.getBoolean("precipitacion"))
                        .build();
                   lista.add(c);
            }

            return lista;
        }
    }

    @Override
    public List<Clima> findByProvincia(String nombre) throws SQLException {
        logger.debug("Obteniendo todos los alumnos por nombre que contenga: " + nombre);
        // Vamos a usar Like para buscar por nombre
        String query = "SELECT * FROM TIEMPOS WHERE provincia LIKE ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, "%" + nombre + "%");
            var rs = stmt.executeQuery();
            var lista = new ArrayList<Clima>();
            while (rs.next()) {
                Clima c = Clima.builder()
                        .codigo(rs.getObject("codigo", UUID.class))
                        .localidad(rs.getString("localidad")
                        .provincia(rs.getString("provincia")
                        .tempMax(rs.getDouble("tempMax"))
                        .tempMin(rs.getDouble("tempMin"))
                        .horaTMax(rs.getObject("horaTMax"), LocalDateTime.class)
                        .horaTemMin(rs.getObject("horaTemMin"), LocalDateTime.class)
                        .precipitacion(rs.getBoolean("precipitacion"))
                        .build();

                lista.add(c);
            }
            return lista;
        }
    }

    @Override
    public Optional<Clima> findById(long id) throws SQLException {
        logger.debug("Obteniendo el clima con id: " + id);
        String query = "SELECT * FROM TIEMPOS WHERE id = ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            Optional<Clima> cli = Optional.empty();
            while (rs.next()) {
                cli = Optional.of(Clima.builder()
                        .codigo(rs.getObject("codigo", UUID.class))
                        .localidad(rs.getString("localidad")
                                .provincia(rs.getString("provincia")
                                .tempMax(rs.getDouble("tempMax"))
                                .tempMin(rs.getDouble("tempMin"))
                                .horaTMax(rs.getObject("horaTMax"), LocalDateTime.class)
                                .horaTemMin(rs.getObject("horaTemMin"), LocalDateTime.class)
                                .precipitacion(rs.getBoolean("precipitacion"))
                                .build();
            }
            return cli;
        }
    }

    @Override
    public Clima save(Clima clima) throws SQLException, ClimaNoAlmacenadoException {
        logger.debug("Guardando el tiempo: " + clima);
        String query = "INSERT INTO Tiempos (localidad, provincia, tempMax, horaTMax, tempMin, horaTemMin, precipitacion, codigo) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setString(1, clima.getLocalidad());
            stmt.setString(2, clima.getProvincia());
            stmt.setDouble(3, clima.getTempMax());
            stmt.setObject(4, clima.getHoraTMax());
            stmt.setDouble(5, clima.getTempMin());
            stmt.setObject(6, clima.getHoraTemMin());
            stmt.setBoolean(7, clima.isPrecipitacion());
            stmt.setObject(8, clima.getCodigo());
            var res = stmt.executeUpdate();
            // Ahora puedo obtener la clave

        }
        return clima;
    }

    @Override
    public Clima update(Clima clima) throws SQLException, ClimaNoEncotradoException {
        logger.debug("Actualizando el alumno: " + clima);
        String query = "UPDATE TIEMPOS SET localidad =?, provincia=?, tempMax =?, horaTempMax=?, tempMin=?, horaTemMin=?, precipitacion=?, codigo=? WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            // Vamos a crear los datos de la consultaue necesitamos para insertar automaticos aunque los crea la base de datos
            clima.setUpdatedAt(LocalDateTime.now());
            stmt.setString(1, clima.getLocalidad());
            stmt.setString(2, clima.getProvincia());
            stmt.setDouble(3, clima.getTempMax());
            stmt.setObject(4, clima.getHoraTMax());
            stmt.setDouble(5, clima.getTempMin());
            stmt.setObject(6, clima.getHoraTemMin());
            stmt.setBoolean(7, clima.isPrecipitacion());
            stmt.setObject(8, clima.getCodigo());


            var res = stmt.executeUpdate();
            if (res > 0) {
                logger.debug("Tiempo actualizado");
            } else {
                logger.error("Tiempo no actualizado al no encontrarse en la base de datos con id: " + clima.getId());
                throw new ClimaNoEncotradoException("Alumno/a no encontrado con id: " + clima.getId());
            }
        }
        return clima;
    }


    @Override
    public boolean deleteById(Long id) throws SQLException {
        logger.debug("Borrando el clima con id: " + id);
        String query = "DELETE FROM TIEMPOS WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, id);
            var res = stmt.executeUpdate();
            return res > 0;
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        logger.debug("Borrando todos los tiempos");
        String query = "DELETE FROM TIEMPOS";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.executeUpdate();
        }
    }
}