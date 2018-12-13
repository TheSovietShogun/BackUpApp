package app.dx.dx_deas.app_dx;


public class CTramos {

    private String nombre ;
    private String horaCompromiso ;
    private String estatus ;
    private String secuencia ;
    private String fechaEntrada ;
    private String fechaSalida ;

    public CTramos(String nombre, String horaCompromiso, String estatus, String secuencia, String fechaEntrada, String fechaSalida) {
        this.nombre = nombre;
        this.horaCompromiso = horaCompromiso;
        this.estatus = estatus;
        this.secuencia = secuencia;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHoraCompromiso() {
        return horaCompromiso;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    @Override
    public String toString() {
        return this. nombre + "\n" ;
    }



}
