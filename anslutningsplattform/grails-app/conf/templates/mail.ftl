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
<#if bestallning.producentbestallning??>
Nät: <#if bestallning.producentbestallning.tjanstekomponent.nat??>${bestallning.producentbestallning.tjanstekomponent.nat.namn}<#else>(inget nät valt)</#if>
<#elseif bestallning.konsumentbestallningar?? && bestallning.konsumentbestallningar?size == 1>
Nät: <#if bestallning.konsumentbestallningar[0].tjanstekomponent.nat??>${bestallning.konsumentbestallningar[0].tjanstekomponent.nat.namn}<#else>(inget nät valt)</#if>
</#if>
Övrig information från beställaren:
<#if bestallning.otherInfo??>${bestallning.otherInfo}<#else>(ej ifylld)</#if>
.
<#if bestallning.producentbestallning??>
<#assign producentbestallning = bestallning.producentbestallning>
.
-------------------------------------------------------
Producentbeställning
-------------------------------------------------------
Tjänsteproducent:
    <#assign tjansteproducent = producentbestallning.tjanstekomponent>
.  hsaId: ${tjansteproducent.hsaId}
.  beskrivning: ${tjansteproducent.beskrivning}
.
.  nya producentanslutningar:
------------------------------------------
    <#if (producentbestallning.producentanslutningar)?has_content>
        <#list producentbestallning.producentanslutningar as producentanslutning>
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
.  uppdaterade producentanslutningar:
------------------------------------------
    <#if (producentbestallning.uppdateradProducentanslutningar)?has_content>
        <#list producentbestallning.uppdateradProducentanslutningar as uppdateradProducentanslutning>
. .  [${uppdateradProducentanslutning_index+1}] ${uppdateradProducentanslutning.tjanstekontraktNamnrymd} v${uppdateradProducentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[uppdateradProducentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. . .  RIVTA-profil: ${uppdateradProducentanslutning.rivtaProfil} (<#if uppdateradProducentanslutning.rivtaProfil == uppdateradProducentanslutning.tidigareRivtaProfil>oförändrad<#else>uppdaterad</#if>)
. . .  URL: ${uppdateradProducentanslutning.url} (<#if uppdateradProducentanslutning.url == uppdateradProducentanslutning.tidigareUrl>oförändrad<#else>uppdaterad</#if>)
. . .  logiska adresser som ska läggas till:
            <#if (uppdateradProducentanslutning.nyaLogiskaAdresser)?has_content>
                <#list uppdateradProducentanslutning.nyaLogiskaAdresser as logiskAdress>
. . . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                </#list>
            <#else>
. . . .  (inga)
            </#if>
. . .  logiska adresser som ska tas bort:
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
</#if>
<#if bestallning.konsumentbestallningar?has_content>
.
.
-------------------------------------------------------
Konsumentbeställningar
-------------------------------------------------------
<#list bestallning.konsumentbestallningar as konsumentbestallning>
Tjänstekonsument ${konsumentbestallning_index+1}
        <#assign tjanstekonsument = konsumentbestallning.tjanstekomponent>
.  hsaId: ${tjanstekonsument.hsaId}
.  beskrivning: ${tjanstekonsument.beskrivning}
.
.  konsumenten ska ha behörighet på följande tjänstekontrakt för följande logiska adresser:
    <#if (konsumentbestallning.konsumentanslutningar)?has_content>
        <#list konsumentbestallning.konsumentanslutningar as konsumentanslutning>
. .  [${konsumentanslutning_index+1}] ${konsumentanslutning.tjanstekontraktNamnrymd} v${konsumentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[konsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. . .  logiska adresser som ska läggas till:
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
.  konsumenten ska ha UPPDATERAD behörighet på följande tjänstekontrakt för följande logiska adresser:
    <#if (konsumentbestallning.uppdateradKonsumentanslutningar)?has_content>
        <#list konsumentbestallning.uppdateradKonsumentanslutningar as konsumentanslutning>
. .  [${konsumentanslutning_index+1}] ${konsumentanslutning.tjanstekontraktNamnrymd} v${konsumentanslutning.tjanstekontraktMajorVersion}.x (${domanLookup[konsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
. . .  logiska adresser som ska läggas till:
            <#if (konsumentanslutning.nyaLogiskaAdresser)?has_content>
                <#list konsumentanslutning.nyaLogiskaAdresser as logiskAdress>
. . . .  [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                </#list>
            <#else>
. . . .  (inga)
            </#if>
. . .  logiska adresser som ska tas bort:
            <#if (konsumentanslutning.borttagnaLogiskaAdresser)?has_content>
                <#list konsumentanslutning.borttagnaLogiskaAdresser as logiskAdress>
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
.
-------------------------------------------------------
Kontaktinformation
-------------------------------------------------------
<#if bestallning.producentbestallning??>
<#assign tjanstekomponent = bestallning.producentbestallning.tjanstekomponent>
tjänsteproducent:
.  hsaId: ${tjanstekomponent.hsaId}
.  beskrivning: ${tjanstekomponent.beskrivning}
.  huvudansvarig:
<#if tjanstekomponent.huvudansvarigKontakt??>
<#assign huvudansvarig = tjanstekomponent.huvudansvarigKontakt>
. .  namn: ${huvudansvarig.namn!"-"}
. .  hsaId: ${huvudansvarig.hsaId!"-"}
. .  telefon: ${huvudansvarig.telefon!"-"}
. .  e-post: ${huvudansvarig.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  teknisk kontakt:
<#if tjanstekomponent.tekniskKontakt??>
<#assign tekniskkontakt = tjanstekomponent.tekniskKontakt>
. .  namn: ${tekniskkontakt.namn!"-"}
. .  hsaId: ${tekniskkontakt.hsaId!"-"}
. .  telefon: ${tekniskkontakt.telefon!"-"}
. .  e-post: ${tekniskkontakt.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  funktionsbrevlåda:
<#if tjanstekomponent.tekniskSupportkontakt??>
<#assign funktionsbrevlada = tjanstekomponent.tekniskSupportkontakt>
. .  telefon: ${funktionsbrevlada.telefon!"-"}
. .  e-post: ${funktionsbrevlada.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.
</#if>
<#if bestallning.konsumentbestallningar?has_content>
<#list bestallning.konsumentbestallningar as konsumentbestallning>
<#assign tjanstekomponent = konsumentbestallning.tjanstekomponent>
tjänstekonsument:
.  hsaId: ${tjanstekomponent.hsaId}
.  beskrivning: ${tjanstekomponent.beskrivning}
.  huvudansvarig:
<#if tjanstekomponent.huvudansvarigKontakt??>
<#assign huvudansvarig = tjanstekomponent.huvudansvarigKontakt>
. .  namn: ${huvudansvarig.namn!"-"}
. .  hsaId: ${huvudansvarig.hsaId!"-"}
. .  telefon: ${huvudansvarig.telefon!"-"}
. .  e-post: ${huvudansvarig.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  teknisk kontakt:
<#if tjanstekomponent.tekniskKontakt??>
<#assign tekniskkontakt = tjanstekomponent.tekniskKontakt>
. .  namn: ${tekniskkontakt.namn!"-"}
. .  hsaId: ${tekniskkontakt.hsaId!"-"}
. .  telefon: ${tekniskkontakt.telefon!"-"}
. .  e-post: ${tekniskkontakt.epost!"-"}
<#else>
. .  (ej angiven)
</#if>
.  funktionsbrevlåda:
<#if tjanstekomponent.tekniskSupportkontakt??>
<#assign funktionsbrevlada = tjanstekomponent.tekniskSupportkontakt>
. .  telefon: ${funktionsbrevlada.telefon!"-"}
. .  e-post: ${funktionsbrevlada.epost!"-"}
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
