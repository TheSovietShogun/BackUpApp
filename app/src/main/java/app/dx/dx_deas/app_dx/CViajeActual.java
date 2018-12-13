package app.dx.dx_deas.app_dx;

public class CViajeActual {
    private String  idViaje;
    private String  folioViaje;
    private String  tracking;
    private String  idCliente;
    private String  idRuta;
    private String  nombreRuta;
    private String  secuencia;
    private String  idDetalleViaje;
    private String  idDetalleViajeDestino;
    private String  idOperador;
    private String  idOperador2;
    private String  idRemolquePrincipal;
    private String  nombreTramo;
    private String  direccionTramo;
    private String  descripcionCarga;
    private String  descripcionTipoMovimiento;
    private String  nombreEstatus;
    private String  nombreUnidad;
    private String  nombreOperador;
    private String  nombreOperador2;
    private String  nombreRemolquePrincipal;
    private String  nombreDestino;
    private String  latitudDestino;
    private String  longitudDestino;
    private String  fechaVentana;
    private String  eta;

    public CViajeActual(String idViaje, String folioViaje, String tracking, String idCliente, String idRuta, String nombreRuta, String secuencia, String idDetalleViaje, String idDetalleViajeDestino, String idOperador, String idOperador2, String idRemolquePrincipal, String nombreTramo, String direccionTramo, String descripcionCarga, String descripcionTipoMovimiento, String nombreEstatus, String nombreUnidad, String nombreOperador, String nombreOperador2, String nombreRemolquePrincipal, String nombreDestino, String latitudDestino, String longitudDestino, String fechaVentana, String eta) {
        this.idViaje = idViaje;
        this.folioViaje = folioViaje;
        this.tracking = tracking;
        this.idCliente = idCliente;
        this.idRuta = idRuta;
        this.nombreRuta = nombreRuta;
        this.secuencia = secuencia;
        this.idDetalleViaje = idDetalleViaje;
        this.idDetalleViajeDestino = idDetalleViajeDestino;
        this.idOperador = idOperador;
        this.idOperador2 = idOperador2;
        this.idRemolquePrincipal = idRemolquePrincipal;
        this.nombreTramo = nombreTramo;
        this.direccionTramo = direccionTramo;
        this.descripcionCarga = descripcionCarga;
        this.descripcionTipoMovimiento = descripcionTipoMovimiento;
        this.nombreEstatus = nombreEstatus;
        this.nombreUnidad = nombreUnidad;
        this.nombreOperador = nombreOperador;
        this.nombreOperador2 = nombreOperador2;
        this.nombreRemolquePrincipal = nombreRemolquePrincipal;
        this.nombreDestino = nombreDestino;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.fechaVentana = fechaVentana;
        this.eta = eta;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public String getFolioViaje() {
        return folioViaje;
    }

    public String getTracking() {
        return tracking;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public String getIdDetalleViaje() {
        return idDetalleViaje;
    }

    public String getIdDetalleViajeDestino() {
        return idDetalleViajeDestino;
    }

    public String getIdOperador() {
        return idOperador;
    }

    public String getIdOperador2() {
        return idOperador2;
    }

    public String getIdRemolquePrincipal() {
        return idRemolquePrincipal;
    }

    public String getNombreTramo() {
        return nombreTramo;
    }

    public String getDireccionTramo() {
        return direccionTramo;
    }

    public String getDescripcionCarga() {
        return descripcionCarga;
    }

    public String getDescripcionTipoMovimiento() {
        return descripcionTipoMovimiento;
    }

    public String getNombreEstatus() {
        return nombreEstatus;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public String getNombreOperador() {
        return nombreOperador;
    }

    public String getNombreOperador2() {
        return nombreOperador2;
    }

    public String getNombreRemolquePrincipal() {
        return nombreRemolquePrincipal;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public String getLatitudDestino() {
        return latitudDestino;
    }

    public String getLongitudDestino() {
        return longitudDestino;
    }

    public String getFechaVentana() {
        return fechaVentana;
    }

    public String getEta() {
        return eta;
    }
}
