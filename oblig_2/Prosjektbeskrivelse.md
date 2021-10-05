# Odsen

## Beskrivelse
Denne appen er ment for å grupper av folk, jeg ser for meg mellom 3 og 10 folk. Det vil fungere som et spill man spiller seg imellom. Tanken er at man lager "rom" med de man vil spille med. Deretter lager man utfordriger til hverande, som blir fordelt avhengig av modus man velger.

### Set mode
I denne modusen er tanken at man får et sett med utfordringer, og den som først fullfører alle vinner. Hvis man er 5 stykker, lager man da 4 utfordringer i stigende vanseklighetsgrad. Deretter blir det fordelt tilfeldig på de andre, så alle vil ende opp med 4 utfordringer. Dersom man vil ha flere utfordringer med en gang kan man lage flere serier med utfordringer.

### Loose mode
Her blir alle utfrodringene tilgjengelig for alle, og det er førsteman til mølla med å fulføre de. Dersom en har fullført en utfordring vil ikke de andre kunne fullføre den. I tillegg kan man ikke fullføre sine egne utforminger. Hver utfordring vil da han en score avhengig av hvor vanskelig den er. Den med høyest score vil da vinne når man er ferdig.

### Validering av utfordringer
For å få en utfordring godkjent kan man enten velge om man stoler på hverandre, altså at man ikke har noen form for validering. Dette passer best for utfordringer som påvirker noen i gruppa, så noen på den måten ser det.

Alternativt kan man ha en intern validering. Da kan man for eksempel tar bilde av at man har fullført en utfordring, eller en annen kan være med og se på utfordringen bli fullført. For eksempel å skrive noe med tusj i ansiktet og gå på butikken kan man ta bilde av, mens å løpe naken over en bruk kan man få noen med på å se på.

## Krav
For at appen skal kunne brukes så er det visse ting som må fungere, mens andre ting er mindre viktig. Siden ikke alle krav er like viktige er det derfor noen ting man må prioritere mer en andre. Derfor lager jeg 3 lister med krav ettersom hvor viktige de er.

### Må
Det er krav som må være på plass for at appen i det hele tatt skal fungere. Uten disse kravene vil appen ikke fungere. Jo flere av disse kravene som blir fullført, jo mer av appen vil fungere.

* Kommunikasjon med server / andre enheter
* Lage et rom
  * Gi tilgang til andre brukere til rommet
* Vise utfordringer som er i et rom man er med i selv
* Registrere en utfordring som fullført
* Se hvem andre som er med i et rom
* Se hvem som har fullført en utfordring

### Bør
Her er krevene som ikke er viktige for kjernefunksjonaliteten, men som tilbyr funksjonalitet likevell.

* Legge til utfordringer etter et rom er opprettet
* Se hvem som leder i et rom
* Kunne bytte mellom flere rom
* Avslutte et rom når man er ferdige
  * Se et rom som er avsluttet, både resultater og utfordringer
* Logge inn

### Kan
Kravene som er i denne kategorien er krav som ikke kommer til å bli prioritert i første omgang. Dette er fordi de ikke gir noen stor nytteverdi til appen.

* Mørkt tema istedenfor
* Verifisering av utfordringer
  * Laste opp bilder / filer for å dokumentere egne
  * Se andres bilder / filer for å godkjenne utfordring
* Logge inn på flere ulike enheter
* Legge til venner med QR kode
* Få varsel når noen fullfører en utfordring
* Få varsel når et rom avsluttes
  

### Ikke-funksjonelle krav
I tillegg er det enkelte krav som ikke har noe med selve appen å gjøre. Disse kravene er viktige for at appen skal fungere, selv om de ikke er like direkte knyttet til appen som de andre kravene.
* Server med database

## Design
I utgangspunktet er appen tenkt å være i portrett modus. Grunnen til dette er at appen er tenkt til å brukes i korte intervaller. I tillegg bruker de fleste mobilen stående, så det blir derfor designet for flest mulig. Bildene som kommer under er tenkt til den "ferdige" applikasjonen, som støtter alle kravene.

Dette er forsiden man kommer til, dersom man er logget inn. Her får man vite hvem man er logget inn som, og 4 ulikebokser man bruker til å gå videre. I tillegg er det en mulighet til å logge av fra enheten.

Siden brukere må innom denne siden hver gang man skal inn på en av de andre sidene vil også dette fungere som en meny. Etter at man har gått inn på en av sidene kan man da komme tilbake hit ved tilbakeknappen man har på enheten sin.

![Forside med 4 bokser for å gå videre](bilder/forside.png)

Kommer hit etter å ha valgt et rom fra forsiden. Navnet på rommet kommer øverst, og så antallet i rommet. Antallet kan man klikke på for å se en liste med navnene. Deretter kommer en liste med de som leder i et rom, sammen med antall utfordringer vedkommende har fullført. Til slutt er det en liste over gjennværende utfordringer.

![Oversiktside i et rom, med leaderboard og andre utfordringer](bilder/startet_rom.png)

Siden har øverst en mulighet for å legge til andre brukere som venner, via brukernavn. I tillegg kommer en QR kode som kan brukes av noen andre for å kunne legge deg til som venn. Nederst er det en liste over alle vennene du har i appen.

I tillegg er det en linje der det står at man har fått venneforespørsler, dersom man har fått det. Da får man opp en oversikt over alle forespørsler man ikke har bekreftet/avvist i kronologisk rekkefølge.

![Administrasjonsside for venner](bilder/venner.png)

Via denne skjermen kan man opprette nye rom. Fra øverst til nederst starter man med å velge navn på rommet. Deretter velger man hvem som skal være med i rommet, enten via søk eller via å krysse av fra en liste med de man spiller mest med. Etter det velger man når rommet avsluttes, markert med grønn boks. Man må i tillegg oppdatere "X" til enten et tall eller en dato.

Man må også legge til utfordringer til rommet, og sette en vanskelighetsgrad på dem. Brukeren legger da inn en tekst til å beskrive utfordringen, og velger mellom "Lett", "Middels" og "Vanskelig". Vanskelighetsgraden på utfordringer i en gruppe vil justere seg innad i gruppen etterhvert som deltakerne merker hva de synes er lett og vanskelig. Når man er ferdig har man en knapp til det nederst.

![Skjermen man kommer til når man skal opprette et nytt rom](bilder/lag_nytt_rom.png)

For alle tidligere fullførte rom er tanken at man skal kunne se hvor bra eller dårlig man har gjort det sammenlignet med de andre i rommet. Derfor har man en samleside for alle tidligere rom, der man kan scrolle gjennom en liste der navnet på rommet og din rangering står. Derfra kan man velge et for å se hvem som har gjort hvilke utfordringer, og den samlede resultatlista.

![Liste der man kan gå inn på tidligere fullførte rom](bilder/ferdige_rom.png)

## Systemdesign

### Service og fragment
Tanken med appen er at den skal brukes aktivt i kortere perioder. Derfor vil det meste av appen ikke trenger å kjøre i bakgrunnen når appen lukkes. I tillegg er appen frittstående, som igjen gjør at den ikke trenger å kommunisere med andre apper. Derfor vil det for det meste av appen kun være en Activety.

Appen skal kunne gi varsler for når andre har fullført en utfordring og når rom avsluttes. Derfor må appen ha en kobling mo backend når appen er lukket for å sjekke etter varsler brukeren skal få. Til dette trengs det en Service. Alternativt kunne man brukt et Fragment, men Service vil kunne overleve bedre som en bakgrunnsprosess.

### Lagring av data
Appen vil inneholde både data om rom og om brukeren. Det meste av dataen skal i tillegg deles med andre brukere, som nødvendigvis ikke er i nærheten. Derfor vil man måtte ha data eksternt. Til dette tenker jeg å bruke firebase, ettersom det ser ganske greit ut å koble sammen. I tillegg vil firebase også kunne brukes til brukerhåndtering.

### Brukerhåndtering
Siden en bruker skal kunne bruke appen på flere enheter som samme bruker, vil man måtte ha brukerhåndtering. I tillegg til at det er flere brukere som konkurerer mot hverandre i et rom vil det også være nødvendig med brukerhåndtering for å kunne skille de ulike brukerne fra hverandre.

## Hovedkomponenter
Hvert skjermbilde vil være en egen komponent, med tillhørende funksjonalitet. Det vil si at det er behov for 5 skjermer i appen. I tillegg må brukeren være logget inn for å bruke appen, som vil si at det første gangen man åpner appen må være en innloggingsside. Innloggingen må i tillegg komme opp hver gang en bruker logger ut, så brukeren kan logge inn som en annen bruker.

Utenom brukerhåndtering bør alle krav kunne dekkes av de andre skjermene. Appen blir da forholdsvis liten ettersom det bare er 4 skjermer å legge innhold på, siden den første skjermen i praksis bare er til navigasjon.

## Resurser
### Bilder
Selve appen trenger ikke bilder, ettersom det bare er tekstelig innhold. Eneste stedet det kunne vært naturlig å ha bilder er som bakgrunn på det forskjellige skjermene, og at det eventuelt går å laste opp et eget bilde til hvert rom. Utenfor appen bør det være et bilde til logo for appen. Det er ikke viktig for funksjonaliteten, men gir et bedre inntrykk av appen.

### Lyd
Lyd vil heller ikke ha noen spesiell nytte i appen. Eventuelt kunne man hatt en lyd for å "feire" at man har fullført en utfordring. Derimot vil det være naturlig med en lyd for å varsle en bruker om at noen har fullført en utfordring mens man er ute av appen. Altså en lyd som kommer sammen med push varselet man får.

### Klasser
Hver skjerm trenger en klasse til å ha funksjonaliteten som trengs. I tillegg er hvert rom en klasse som holder på data om rommet. I tillegg bør utfordringer være en egen klasse, siden de også har flere data.

Det kan også være aktuelt med en egen klasse for å kommunisere med database. Siden de ulike skjermene gjør forskjellige ting vil det være lite overlapping i hva de gjør. Derfor kan man også plassere database kommunikasjon i klassene til de aktuelle skjermene.

### Eksternt
Det eneste appen trenger som er eksternt er database og brukerhåndtering. Ettersom appen ikke bruker annet eksternt vil appen trenge lite nettverk, som også kan være begrensende på for eksempel mobiler.

## Utfordringer
De største utfrodringene for appen ser jeg for meg er brukerhåndtering og å koble appen mot databasen. Utenom ser jeg for meg at det kommer til å ta tid å justere alt innholdet på skjermene for å få alt til å ryddig ut. Utover det så bør det meste gå greit nok.