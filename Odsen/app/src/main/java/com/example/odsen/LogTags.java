package com.example.odsen;

// Egen klasse for alle logtagger. Tanken er å gjøre det lettere å endre en tag dersom det skulle vært behov for det
// Når det logges brukes en Tag som kategori, istedenfor hvor i appen Log trigges fra. Derfor må det i log beskjed komme fram hvor loggen kommer.
public class LogTags {
    // Ukategorisert
    public static final String NAVIGATION = "Navigering";

    // Database
    public static final String STARTING_DB = "Instans av DB";

    // Lasting av data
    public static final String USER_LOGGING = "Innlogging";
    public static final String LOADING_DATA = "Laster inn data";

    // Input
    public static final String ANY_INPUT = "Input";

    // Ting som ikke skal/bør/må skje
    public static final String ILLEGAL_INPUT = "Input hack";
}
