package services.tiempo;

import exceptions.ClimaException;
import modelos.Clima;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.climas.TiempoRepository;
import repository.climas.TiempoRepositoryImpl;

import java.sql.SQLException;
import java.util.*;

public class TiempoServiceImp implements TiempoService{
    private static TiempoServiceImp instance;
    private final Logger logger = LoggerFactory.getLogger(TiempoServiceImp.class);
    private TiempoRepositoryImpl tiempoRepository;
    private TiempoServiceImp(TiempoRepository tiempoRepository) throws SQLException {
      run();
    }
private void run() throws SQLException {
    MaxMin();
    precmed();
    precmax();
    mediloc();
    localprec();
    provincia("Madrid");
}
private void provincia(String provincia){
    tiempoRepository.provincia(provincia);
}
private void localprec(){
        tiempoRepository.localprec();
}
private void mediloc(){
        tiempoRepository.precLocal();
}
private void precmax(){
        tiempoRepository.precmax();
}
private void MaxMin() throws SQLException {
        tiempoRepository.maxMin();
}
private void precmed(){
        tiempoRepository.precmedia();
}
    public static TiempoServiceImp getInstance(TiempoRepository tiempoRepository) {
        if (instance == null) {
            instance = new TiempoServiceImp(tiempoRepository);
        }
        return instance;
    }

    @Override
    public List<Clima> findAll() throws SQLException {
        logger.debug("Obteniendo todos los repository.climas");
        return tiempoRepository.findAll();
    }

    @Override
    public List<Clima> findAllByProvincia(String nombre) throws SQLException {
        logger.debug("Obteniendo todos los repository.climas ordenados por nombre de provincia");

        return tiempoRepository.findByProvincia(nombre);
    }

    @Override
    public Optional<Clima> findById(UUID id) throws SQLException {
        logger.debug("Obteniendo clima por codigo");

            // Buscamos en la base de datos
            logger.debug("Clima no encontrado en cache, buscando en base de datos");
            var optional = tiempoRepository.findById(id);
            return optional;
        }


    @Override
    public Clima save(Clima clima) throws SQLException, ClimaException {
        logger.debug("Guardando clima");
        // Guardamos en la base de datos
        clima = tiempoRepository.save(clima);
        return clima;
    }

    @Override
    public Clima update(Clima clima) throws SQLException, ClimaException {
        logger.debug("Actualizando alumno");
        clima = tiempoRepository.update(clima);
        return clima;
    }

    @Override
    public boolean deleteById(UUID id) throws SQLException {
        logger.debug("Borrando clima");

        var deleted = tiempoRepository.deleteById(id);
        return deleted;
    }

    @Override
    public void deleteAll() throws SQLException {
        logger.debug("Borrando todos los alumnos");
        tiempoRepository.deleteAll();

    }
}
