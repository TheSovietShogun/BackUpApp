package app.dx.dx_deas.app_dx;

public class CChat {

    private  String id ;
    private  String idViaje ;
    private  String idDetalleViaje ;
    private  String fecha ;
    private  String mensaje ;
    private  String comentarioCorto ;
    private  String idUsuario ;
    private  String nombre ;
    private  String editable ;
    private  String tramo ;

    public CChat(String id, String idViaje, String idDetalleViaje, String fecha, String mensaje, String comentarioCorto, String idUsuario, String usuario, String editable, String tramo) {
        this.id = id;
        this.idViaje = idViaje;
        this.idDetalleViaje = idDetalleViaje;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.comentarioCorto = comentarioCorto;
        this.idUsuario = idUsuario;
        this.nombre = usuario;
        this.editable = editable;
        this.tramo = tramo;
    }

    public String getId() {
        return id;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public String getIdDetalleViaje() {
        return idDetalleViaje;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getComentarioCorto() {
        return comentarioCorto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getUsuario() {
        return nombre;
    }

    public String getEditable() {
        return editable;
    }

    public String getTramo() {
        return tramo;
    }
}
