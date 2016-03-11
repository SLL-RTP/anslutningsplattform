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
Beställningar av adresseringsbehörighet / etablering av samverkan
-------------------------------------------------------
<#assign konsumentbestallning = bestallning.konsumentbestallningar[0]>
Tjänstekonsument:
<#assign tjanstekonsument = konsumentbestallning.tjanstekomponent>
HSA-Id: ${tjanstekonsument.hsaId}
Namn på system: ${tjanstekonsument.beskrivning!"-"}
Namn på e-tjänst: ${konsumentbestallning.namnPaEtjanst!"-"}
.
Konsumenten ska ha behörighet på följande tjänstekontrakt för följande logiska adresser (Ny konsument):
<#if (konsumentbestallning.konsumentanslutningar)?has_content>
<#list konsumentbestallning.konsumentanslutningar?sort_by("tjanstekontraktNamnrymd") as konsumentanslutning>
.  [${konsumentanslutning_index+1}] ${konsumentanslutning.tjanstekontraktNamnrymd} v${konsumentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[konsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. .  Logiska adresser som ska läggas till:
<#if (konsumentanslutning.nyaLogiskaAdresser)?has_content>
<#list konsumentanslutning.nyaLogiskaAdresser as logiskAdress>
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
Konsumenten ska ha UPPDATERAD behörighet på följande tjänstekontrakt för följande logiska adresser:
<#if (konsumentbestallning.uppdateradKonsumentanslutningar)?has_content>
<#list konsumentbestallning.uppdateradKonsumentanslutningar?sort_by("tjanstekontraktNamnrymd") as konsumentanslutning>
.  [${konsumentanslutning_index+1}] ${konsumentanslutning.tjanstekontraktNamnrymd} v${konsumentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[konsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. .  Logiska adresser som ska läggas till:
<#if (konsumentanslutning.nyaLogiskaAdresser)?has_content>
<#list konsumentanslutning.nyaLogiskaAdresser as logiskAdress>
. . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
</#list>
<#else>
. . .  (inga)
</#if>
. .  Logiska adresser som ska tas bort:
<#if (konsumentanslutning.borttagnaLogiskaAdresser)?has_content>
<#list konsumentanslutning.borttagnaLogiskaAdresser as logiskAdress>
. . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
</#list>
<#else>
. . .  (inga)
</#if>
.
</#list>
<#else>
. . .  (inga)
</#if>
.
.
-------------------------------------------------------
System- och kontaktinformation
-------------------------------------------------------
<#assign tjanstekomponent = tjanstekonsument>
tjänstekonsument:
.  Organisation: ${tjanstekomponent.organisation!"-"}
.  HSA-Id: ${tjanstekomponent.hsaId}
.  Namn på system: ${tjanstekomponent.beskrivning!"-"}
.  Nät: <#if tjanstekomponent.nat??>${tjanstekomponent.nat.namn}<#else>(inget nät valt)</#if>
.  IP-adress vid anrop från systemet (systemet är konsument): ${tjanstekomponent.konsumentIpadress!"-"}
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
.
§
användare: ¤${(bestallning.bestallare.hsaId)?keep_after_last("-")}¤
£RTP£
ñ${bestallning.bestallare.epost}ñ
ÿÿ
æRTPæ