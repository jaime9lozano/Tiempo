package modelos;

import java.time.LocalTime;

public class Clima {
    String localidad;
    String provincia;
    double tempMax;
    LocalTime horaTMax;
    double tempMin;
    LocalTime horaTemMin;
    boolean precipitacion;
    UUID codigo;
// he tenido que crear el constructor para que el CRUD me vaya, si quieres o sabes
    //como hacerlo no dudes en cambiarlo
    public Clima(UUID codigo, String localidad, String provincia, double temMax, double temMin, Time horaTMax, Time horaTemMin, boolean precipitacion) {
    }
}
