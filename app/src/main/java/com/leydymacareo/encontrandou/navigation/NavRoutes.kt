package com.leydymacareo.encontrandou

object NavRoutes {
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val AccountCreated = "account_created"
    const val UserHome = "user_home"
    const val EncargadoHome = "encargado_home"
    const val UserHelp = "ayuda_usuario"
    const val UserProfile = "perfil_usuario"
    const val DetalleSolicitud = "detalle_solicitud/{solicitudId}/{rol}"
    const val NuevaSolicitud = "nueva_solicitud"
    const val EncargadoSolicitudes = "encargado_solicitudes"
    const val EncargadoAjustes = "encargado_ajustes"
    const val EncargadoProfile = "perfil_encargado"
    const val NuevoObjeto = "nuevo_objeto"
    const val DetalleObjeto = "detalle_objeto/{objetoId}"
    const val DetalleSolicitudEncargado = "detalle_solicitud_encargado/{solicitudId}"
    const val DetalleObjetoSeleccionable = "detalle_objeto_para_asignar/{solicitudId}/{objetoId}"

    fun detalleObjetoParaAsignarRoute(solicitudId: String, objetoId: String) =
        "detalle_objeto_para_asignar/$solicitudId/$objetoId"
}