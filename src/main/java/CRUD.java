import modelos.Clima;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

    public class CRUD {
        private static CRUD instance;
        private Connection connection;

        private CRUD(Connection connection) {
            this.connection = connection;
        }

        public static synchronized CRUD getInstance(Connection connection) {
            if (instance == null) {
                instance = new CRUD(connection);
            }
            return instance;
        }
        //MANITO COMPRUEBA SI ESTA BIEN, CREO QUE LO ESTÁ 
        public List<Clima> findAll() throws SQLException {
            List<Clima> tiempos = new ArrayList<>();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM tiempos");

            while (resultSet.next()) {
                tiempos.add(new Clima(
                        UUID.fromString(resultSet.getString("codigo")),
                        resultSet.getString("localidad"),
                        resultSet.getString("provincia"),
                        resultSet.getDouble("tempMax"),
                        resultSet.getDouble("tempMin"),
                        resultSet.getTime("horaTMax"),
                        resultSet.getTime("horaTemMin"),
                        resultSet.getBoolean("precipitacion")
                ));
            }
           return tiempos;
        }

        public Optional<Clima> findById(UUID id) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tiempos WHERE id = ?");
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Clima(
                        UUID.fromString(resultSet.getString("codigo")),
                        resultSet.getString("localidad"),
                        resultSet.getString("provincia"),
                        resultSet.getDouble("tempMax"),
                        resultSet.getDouble("tempMin"),
                        resultSet.getTime("horaTMax"),
                        resultSet.getTime("horaTemMin"),
                        resultSet.getBoolean("precipitacion")
                ));
            } else {
                return Optional.empty();
            }
        }

        public void create(Clima clima) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tiempos (id, localidad, provincia, temMax, horaTMax, temMin, horaTemMin, precipitacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, clima.getCodigo().toString());
            statement.setString(2, clima.getLocalidad());
            statement.setString(3, clima.getProvincia());
            statement.setDouble(4, clima.getTempMax());
            statement.setString(5,clima.getHoraTMax().toString());
            statement.setDouble(6, clima.getTempMin());
            statement.setString(7,clima.getHoraTemMin().toString());
            statement.setBoolean(8,clima.isPrecipitacion());
            statement.executeUpdate();
        }

        public void update(Clima clima) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("UPDATE tiempos SET  localidad = ?, provcincia = ? temMax = ?, horaTMax = ?, temMin = ?, horaTemMin = ?, precipitacion = ? WHERE codigo = ?");
            statement.setString(2, clima.getLocalidad());
            statement.setString(3, clima.getProvincia());
            statement.setDouble(4, clima.getTempMax());
            statement.setString(5,clima.getHoraTMax().toString());
            statement.setDouble(6, clima.getTempMin());
            statement.setString(7,clima.getHoraTemMin().toString());
            statement.setBoolean(8,clima.isPrecipitacion());
            statement.executeUpdate();
        }

        public void delete(UUID codigo) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tiempos WHERE codigo = ?");
            statement.setString(1, codigo.toString());
            statement.executeUpdate();
        }
    }
