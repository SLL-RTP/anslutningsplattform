<#-- @ftlvariable name="bestallning" type="se.skltp.ap.Bestallning" -->
<#-- @ftlvariable name="domanLookup" type="java.util.Map<java.lang.String, se.skltp.ap.services.dto.TjansteDomanDTO>" -->
WebMailForm
/1060\
§
.
RTP Beställning för driftmiljö ${bestallning.driftmiljo.namn} (${bestallning.driftmiljo.id})
.
Beställt av ${bestallning.bestallare.namn} ${.now?iso_local}
Beställarens telefon: ${bestallning.bestallare.telefon}
Beställarens e-post: ${bestallning.bestallare.epost}
Beställarens HSA-ID: <#if bestallning.bestallare.hsaId??>${bestallning.bestallare.hsaId}<#else>(okänt)</#if>
Beställarens roll: <#if bestallning.bestallareRoll??>${bestallning.bestallareRoll}<#else>(ej ifylld)</#if>
.
Övrig information från beställaren:
<#if bestallning.otherInfo??>${bestallning.otherInfo}<#else>(ej ifylld)</#if>
.
.
-------------------------------------------------------
Producentbeställning
-------------------------------------------------------
<#assign producentbestallning = bestallning.producentbestallning>
Tjänsteproducent:
<#assign tjansteproducent = producentbestallning.tjanstekomponent>
.  HSA-Id: ${tjansteproducent.hsaId}
.  Namn på system: ${tjansteproducent.beskrivning!"-"}
.  Namn på e-tjänst: ${producentbestallning.namnPaEtjanst!"-"}
.
.  Nya producentanslutningar:
------------------------------------------
<#if (producentbestallning.producentanslutningar)?has_content>
<#list producentbestallning.producentanslutningar?sort_by("tjanstekontraktNamnrymd") as producentanslutning>
. .  [${producentanslutning_index+1}] ${producentanslutning.tjanstekontraktNamnrymd} v${producentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[producentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. .  RIVTA-profil: ${producentanslutning.rivtaProfil}
. .  URL: ${producentanslutning.url}
. .  logiska adresser som ska läggas till:
<#if (producentanslutning.nyaLogiskaAdresser)?has_content>
<#list producentanslutning.nyaLogiskaAdresser as logiskAdress>
. . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
</#list>
<#else>
. . .  (inga)
</#if>
.
</#list>
<#else>
. .  (inga)
</#if>
.
.  Uppdaterade producentanslutningar:
------------------------------------------
<#if (producentbestallning.uppdateradProducentanslutningar)?has_content>
<#list producentbestallning.uppdateradProducentanslutningar?sort_by("tjanstekontraktNamnrymd") as uppdateradProducentanslutning>
. .  [${uppdateradProducentanslutning_index+1}] ${uppdateradProducentanslutning.tjanstekontraktNamnrymd} v${uppdateradProducentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[uppdateradProducentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. . .  RIVTA-profil: ${uppdateradProducentanslutning.rivtaProfil} (<#if uppdateradProducentanslutning.rivtaProfil == uppdateradProducentanslutning.tidigareRivtaProfil>oförändrad<#else>uppdaterad</#if>)
. . .  URL: ${uppdateradProducentanslutning.url} (<#if uppdateradProducentanslutning.url == uppdateradProducentanslutning.tidigareUrl>oförändrad<#else>uppdaterad</#if>)
. . .  Logiska adresser som ska läggas till:
<#if (uppdateradProducentanslutning.nyaLogiskaAdresser)?has_content>
<#list uppdateradProducentanslutning.nyaLogiskaAdresser as logiskAdress>
. . . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
</#list>
<#else>
. . . .  (inga)
</#if>
. . .  Logiska adresser som ska tas bort:
<#if (uppdateradProducentanslutning.borttagnaLogiskaAdresser)?has_content>
<#list uppdateradProducentanslutning.borttagnaLogiskaAdresser as logiskAdress>
. . . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
</#list>
<#else>
. . . .  (inga)
</#if>
.
</#list>
<#else>
. .  (inga)
</#if>
<#if bestallning.konsumentbestallningar?has_content>
.
.
-------------------------------------------------------
Beställningar av anropsbehörighet / etablering av samverkan
-------------------------------------------------------
<#list bestallning.konsumentbestallningar as konsumentbestallning>
Tjänstekonsument ${konsumentbestallning_index+1}
<#assign tjanstekonsument = konsumentbestallning.tjanstekomponent>
.  HSA-Id: ${tjanstekonsument.hsaId}
.  Namn på system: ${tjanstekonsument.beskrivning}
.
.  Konsumenten ska ha behörighet på följande tjänstekontrakt för följande logiska adresser:
<#if (konsumentbestallning.konsumentanslutningar)?has_content>
<#list konsumentbestallning.konsumentanslutningar?sort_by("tjanstekontraktNamnrymd") as konsumentanslutning>
. .  [${konsumentanslutning_index+1}] ${konsumentanslutning.tjanstekontraktNamnrymd} v${konsumentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[konsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. . .  Logiska adresser som ska läggas till:
<#if (konsumentanslutning.nyaLogiskaAdresser)?has_content>
<#list konsumentanslutning.nyaLogiskaAdresser as logiskAdress>
. . . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
</#list>
<#else>
. . . .  (inga)
</#if>
.
</#list>
<#else>
. . .  (inga)
</#if>
.
</#list>
</#if>
-------------------------------------------------------
System- och kontaktinformation
-------------------------------------------------------
<#assign tjanstekomponent = bestallning.producentbestallning.tjanstekomponent>
tjänsteproducent:
.  Organisation: ${tjanstekomponent.organisation!"-"}
.  HSA-Id: ${tjanstekomponent.hsaId}
.  Namn på system: ${tjanstekomponent.beskrivning!"-"}
.  Nät: <#if tjanstekomponent.nat??>${tjanstekomponent.nat.namn}<#else>(inget nät valt)</#if>
.  IP-adress vid anrop till systemet (systemet är producent): ${tjanstekomponent.producentIpadress!"-"}
.  DNS-namn: ${tjanstekomponent.producentDnsNamn!"-"}
.  URL till övervakningstjänst: ${tjanstekomponent.pingForConfigurationURL!"-"}
.
.  Huvudansvarig:
<#if tjanstekomponent.huvudansvarigKontakt??>
<#assign kontaktperson = tjanstekomponent.huvudansvarigKontakt>
. .  Namn: ${kontaktperson.namn!"-"}
. .  HSA-Id: ${kontaktperson.hsaId!"-"}
. .  Telefon: ${kontaktperson.telefon!"-"}
. .  E-post: ${kontaktperson.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  Teknisk kontakt:
<#if tjanstekomponent.tekniskKontakt??>
<#assign kontaktperson = tjanstekomponent.tekniskKontakt>
. .  Namn: ${kontaktperson.namn!"-"}
. .  HSA-Id: ${kontaktperson.hsaId!"-"}
. .  Telefon: ${kontaktperson.telefon!"-"}
. .  E-post: ${kontaktperson.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  Funktionsbrevlåda:
<#if tjanstekomponent.tekniskSupportkontakt??>
<#assign funktionsbrevlada = tjanstekomponent.tekniskSupportkontakt>
. .  Telefon: ${funktionsbrevlada.telefon!"-"}
. .  E-post: ${funktionsbrevlada.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.
<#if bestallning.konsumentbestallningar?has_content>
<#list bestallning.konsumentbestallningar as konsumentbestallning>
<#assign tjanstekomponent = konsumentbestallning.tjanstekomponent>
Tjänstekonsument ${konsumentbestallning_index+1}:
.  Organisation: ${tjanstekomponent.organisation!"-"}
.  HSA-Id: ${tjanstekomponent.hsaId}
.  Namn på system: ${tjanstekomponent.beskrivning}
.  Nät: <#if tjanstekomponent.nat??>${tjanstekomponent.nat.namn}<#else>(inget nät valt)</#if>
.  IP-adress vid anrop från systemet (systemet är konsument): ${tjanstekomponent.konsumentIpadress!"-"}

.  Huvudansvarig:
<#if tjanstekomponent.huvudansvarigKontakt??>
<#assign kontaktperson = tjanstekomponent.huvudansvarigKontakt>
. .  Namn: ${kontaktperson.namn!"-"}
. .  HsaId: ${kontaktperson.hsaId!"-"}
. .  Telefon: ${kontaktperson.telefon!"-"}
. .  E-post: ${kontaktperson.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  Teknisk kontakt:
<#if tjanstekomponent.tekniskKontakt??>
<#assign kontaktperson = tjanstekomponent.tekniskKontakt>
. .  Namn: ${kontaktperson.namn!"-"}
. .  HsaId: ${kontaktperson.hsaId!"-"}
. .  Telefon: ${kontaktperson.telefon!"-"}
. .  E-post: ${kontaktperson.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  Funktionsbrevlåda:
<#if tjanstekomponent.tekniskSupportkontakt??>
<#assign funktionsbrevlada = tjanstekomponent.tekniskSupportkontakt>
. .  Telefon: ${funktionsbrevlada.telefon!"-"}
. .  E-post: ${funktionsbrevlada.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.
</#list>
</#if>
.
§
användare: ¤${(bestallning.bestallare.hsaId)?keep_after_last("-")}¤
£RTP£
ñ${bestallning.bestallare.epost}ñ
ÿsupport@callistaenterprise.seÿ
æRTPæ
