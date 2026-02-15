package com.voll.api.domain.medico;

public enum Especialidad {
    ORTOPEDIA("ortopedia"),
    CARDIOLOGÍA("cardiología"),
    GINECOLOGÍA("ginecología"),
    DERMATOLOGÍA("dermatología");

    private String especialidad;

    Especialidad(String text) {
        this.especialidad = text;
    }

    public static Especialidad fromFront(String text) {
        for (Especialidad especialidad : Especialidad.values()) {
            if (especialidad.especialidad.equalsIgnoreCase(text)) {
                return especialidad;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
