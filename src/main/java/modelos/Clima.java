package modelos;

import java.time.LocalTime;
import java.util.UUID;

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
    public Clima(UUID codigo, String localidad, String provincia, double temMax, double temMin, LocalTime horaTMax, LocalTime horaTemMin, boolean precipitacion) {
        this.codigo=UUID.randomUUID();
    }

    public String getLocalidad() {
        return localidad;
    }

    @Override
    public String toString() {
        return "Clima{" +
                "localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", tempMax=" + tempMax +
                ", horaTMax=" + horaTMax +
                ", tempMin=" + tempMin +
                ", horaTemMin=" + horaTemMin +
                ", precipitacion=" + precipitacion +
                ", codigo=" + codigo +
                '}';
    }

    public String getProvincia() {
        return provincia;
    }

    public double getTempMax() {
        return tempMax;
    }

    public LocalTime getHoraTMax() {
        return horaTMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public LocalTime getHoraTemMin() {
        return horaTemMin;
    }

    public boolean isPrecipitacion() {
        return precipitacion;
    }

    public UUID getCodigo() {
        return codigo;
    }
}
