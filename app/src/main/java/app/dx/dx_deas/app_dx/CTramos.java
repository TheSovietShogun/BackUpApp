package app.dx.dx_deas.app_dx;


public class CTramos {

    private String nombre ;
    private String horaCompromiso ;
    private String estatus ;
    private String secuencia ;
    private String fechaEntrada ;
    private String fechaSalida ;
    private String idDetalleViaje ;

    public CTramos(String nombre, String horaCompromiso, String estatus, String secuencia, String fechaEntrada, String fechaSalida,String idDetalleViaje) {
        this.nombre = nombre;
        this.horaCompromiso = horaCompromiso;
        this.estatus = estatus;
        this.secuencia = secuencia;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.idDetalleViaje = idDetalleViaje;
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

    public String getIdDetalleViaje() {
        return idDetalleViaje;
    }

    @Override
    public String toString() {
        return this. nombre + "\n" ;
    }



}
