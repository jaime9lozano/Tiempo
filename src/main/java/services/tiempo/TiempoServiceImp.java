package services.tiempo;

import exceptions.ClimaException;
import modelos.Clima;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class TiempoServiceImp extends TiempoService{

    // Para mi cache
    private static final int CACHE_SIZE = 10; // Tamaño de la cache
    // Singleton
    private static TiempoServiceImp instance;
    private final Map<Long, Clima> cache; // Nuestra cache
    private final Logger logger = LoggerFactory.getLogger(TiempoServiceImp.class);
    private final TiempoRepository tiempoRepository;

    private TiempoServiceImpl(TiempoRepository tiempoRepository) {
        this.alumnosRepository = alumnosRepository;
        // Inicializamos la cache con el tamaño y la política de borrado de la misma
        // borramos el más antiguo cuando llegamos al tamaño máximo
        this.cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Clima> eldest) {
                return size() > CACHE_SIZE;
            }
        };
    }


    public static TiempoServiceImp getInstance(TiempoRepository tiempoRepository) {
        if (instance == null) {
            instance = new TiempoServiceImp(tiempoRepository);
        }
        return instance;
    }

    @Override
    public List<Clima> findAll() throws SQLException {
        logger.debug("Obteniendo todos los climas");
        return alumnosRepository.findAll();
    }

    @Override
    public List<Clima> findAllByProvincia(String nombre) throws SQLException {
        logger.debug("Obteniendo todos los climas ordenados por nombre de provincia");

        return tiempoRepository.findByProvincia(nombre);
    }

    @Override
    public Optional<Clima> findById(UUID id) throws SQLException {
        logger.debug("Obteniendo clima por codigo");
        // Buscamos en la cache
        Clima alumno = cache.get(id);
        if (alumno != null) {
            logger.debug("Clima encontrado en cache");
            return Optional.of(alumno);
        } else {
            // Buscamos en la base de datos
            logger.debug("Clima no encontrado en cache, buscando en base de datos");
            var optional = tiempoRepository.findById(id);
            optional.ifPresent(value -> cache.put(id, value));
            return optional;
        }
    }

    @Override
    public Clima save(Clima clima) throws SQLException, ClimaException {
        logger.debug("Guardando clima");
        // Guardamos en la base de datos
        clima = tiempoRepository.save(clima);
        // Guardamos en la cache
        cache.put(clima.getCodigo(), clima);
        return clima;
    }

    @Override
    public Clima update(Clima clima) throws SQLException, ClimaException {
        logger.debug("Actualizando alumno");
        // Actualizamos en la base de datos
        clima = tiempoRepository.update(clima);
        // Actualizamos en la cache
        cache.put(clima.getCodigo(), clima);
        return clima;
    }

    @Override
    public boolean deleteById(UUID id) throws SQLException {
        logger.debug("Borrando clima");
        // Borramos en la base de datos
        var deleted = tiempoRepository.deleteById(id);
        // Borramos en la cache si existe en ella
        if (deleted) {
            cache.remove(id);
        }
        return deleted;
    }

    @Override
    public void deleteAll() throws SQLException {
        logger.debug("Borrando todos los alumnos");
        // Borramos en la base de datos
        tiempoRepository.deleteAll();
        // Borramos en la cache
        cache.clear();
    }
}
