# Prototype Beta

[Link til GitHub](https://github.com/jreberg98/Mobilprogrammering/ )

## Lagring
For denne innleveringen har det meste av fokuset vært på lagring til DB. Det har vært litt kluss med å laste data, med ting som tar for lang tid å laste og tilsvarende. I tillegg blei det litt kluss med felt og navn i databasen som ble endret. Derimot skal det meste fungere nå.

### Ting som fungerer
* Registrere seg som bruker. Registrers da til FirebaseAuth og noe data til FirebaseFirestore.
* Sende venneforespørsler til andre brukere, må da bruke epost adressen som er brukt til å registrere
  * Mangler validering på om brukeren finnes
  * Mangler sjekk om brukeren har blokkert deg, eller om dere allerede er venner
* Opprette et rom, med utfordringer og andre spillere
* Få en oversikt over alle rom du er med i
* Gå inn i et rom du er med i
  * Diverse data om rommet vises da i UIet
* Registrere en utfordring som er fullført
  * Litt buggete -.-
* Liste opp hvem som leder i et rom