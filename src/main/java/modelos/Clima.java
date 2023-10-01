package modelos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;
@Builder
@AllArgsConstructor
@Data
public class Clima {
    String localidad;
    String provincia;
    double tempMax;
    LocalTime horaTMax;
    double tempMin;
    LocalTime horaTemMin;
    boolean precipitacion;
    UUID codigo;


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
