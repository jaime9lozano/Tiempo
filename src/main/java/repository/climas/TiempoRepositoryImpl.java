package repository.climas;
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

    public Optional<Clima> findById(UUID id) throws SQLException {
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
    public Clima update(Clima clima) throws SQLException, ClimaNoAlmacenadoException {
        logger.debug("Actualizando el alumno: " + clima);
        String query = "UPDATE TIEMPOS SET localidad =?, provincia=?, tempMax =?, horaTempMax=?, tempMin=?, horaTemMin=?, precipitacion=?, codigo=? WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
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
            if (res > 0) {
                logger.debug("Tiempo actualizado");
            } else {
                logger.error("Tiempo no actualizado al no encontrarse en la base de datos con id: " + clima.getId());
                throw new ClimaNoAlmacenadoException("clima no encontrado con id: " + clima.getId());
            }
        }
        return clima;
    }


    public boolean deleteById(UUID id) throws SQLException {
        logger.debug("Borrando el clima con id: " + id);
        String query = "DELETE FROM TIEMPOS WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, Long.parseLong("id"));
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
    public void maxMin() throws SQLException{
        double max=0;
        double min=0;
        logger.debug("Temperatura maxima y minima");
        String query = "SELECT MAX(tempMax)) FROM TIEMPOS";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()){
               max= rs.getDouble("tempMax");
            }
        }
        query = "SELECT MIN(tempMin)) FROM TIEMPOS";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()){
                min= rs.getDouble("tempMin");
            }
        }
        System.out.println("El maximo y el minimo son: "+max+","+min);
    }
    public void precmedia(){
        logger.debug("Precipitacion media por provincia");
        String query = "SELECT provincia, AVG(CASE WHEN precipitacion = TRUE THEN 1 ELSE 0 END) AS precipitacion_media FROM TIEMPOS GROUP BY provincia";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()){
                String provincia = rs.getString("provincia");
                double precipitacionMedia = rs.getDouble("precipitacion_media");

                System.out.println("Provincia: " + provincia + ", Precipitación Media: " + precipitacionMedia);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void precmax(){
        logger.debug("Precipitacion maxima por provincia");
        String query = "SELECT provincia, MAX(CASE WHEN precipitacion = TRUE THEN 1 ELSE 0 END) AS precipitacion_maxima " +
                "FROM TIEMPOS " +
                "GROUP BY provincia " +
                "ORDER BY precipitacion_maxima DESC " +
                "LIMIT 1";;
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()){
                String provinciaConMaximaPrecipitacion = rs.getString("provincia");
                double precipitacionMaxima = rs.getDouble("precipitacion_maxima");

                System.out.println("Provincia con Precipitación Máxima: " + provinciaConMaximaPrecipitacion);
                System.out.println("Precipitación Máxima: " + precipitacionMaxima);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void precLocal(){
        logger.debug("localidades con precipitacion");
        String query = "SELECT provincia, GROUP_CONCAT(localidad, ', ') AS localidades_con_precipitacion " +
                "FROM TIEMPOS " +
                "WHERE precipitacion = TRUE " +
                "GROUP BY provincia";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()){
                String provincia = rs.getString("provincia");
                String localidadesConPrecipitacion = rs.getString("localidades_con_precipitacion");

                System.out.println("Provincia: " + provincia);
                System.out.println("Localidades con Precipitación: " + localidadesConPrecipitacion);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void localprec(){
        logger.debug("localidades donde mas llovio");
        String query = "SELECT localidad, COUNT(*) AS cantidad_lluvia " +
                "FROM tabla_precipitacion " +
                "WHERE precipitacion = TRUE " +
                "GROUP BY localidad " +
                "ORDER BY cantidad_lluvia DESC " +
                "LIMIT 1";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()){
                String localidadConMasLluvia = rs.getString("localidad");
                int cantidadLluvia = rs.getInt("cantidad_lluvia");

                System.out.println("Localidad con Más Lluvia: " + localidadConMasLluvia);
                System.out.println("Cantidad de Lluvia: " + cantidadLluvia);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void provincia(String provincia){
        logger.debug("provincia de madrid");
        String query = "SELECT fecha, temperatura_maxima, temperatura_minima, precipitacion  FROM datos_climaticos WHERE provincia = ? ORDER BY fecha";
        try (var connection = db.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, provincia);
                    var rs = stmt.executeQuery();
                    while (rs.next()) {
                        String fecha = rs.getString("fecha");
                        double temperaturaMaxima = rs.getDouble("temperatura_maxima");
                        double temperaturaMinima = rs.getDouble("temperatura_minima");
                        boolean precipitacion = rs.getBoolean("precipitacion");
                        System.out.println("Fecha: " + fecha);
                        System.out.println("Temperatura Máxima: " + temperaturaMaxima);
                        System.out.println("Temperatura Mínima: " + temperaturaMinima);
                        System.out.println("Precipitación: " + (precipitacion ? "Sí" : "No"));
                    }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}