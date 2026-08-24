package com.brightsmile.backend.cita;

//Enum que define los posibles estados de uan cita
//Un enum es un tipo especial que solo puede tener valores predefinidos
//Evita errores como escribir "pendiente" en vez de "PENDIENTE"

public enum EstadoCita {

    //Cita recien creada - esperando confirmacion
    PENDIENTE,

    //El paciente asistió y se completó la consulta
    COMPLETADA,

    //El paciente o la clinica canceló la cita+
    CANCELADA


}
