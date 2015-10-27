<#-- @ftlvariable name="bestallning" type="se.skltp.ap.Bestallning" -->
<#-- @ftlvariable name="domanLookup" type="java.util.Map<java.lang.String, se.skltp.ap.services.dto.TjansteDomanDTO>" -->
WebMailForm
/1060\
§
RTP Beställning för driftmiljö ${bestallning.driftmiljo.namn} (${bestallning.driftmiljo.id})
Beställt av ${bestallning.bestallare.namn} ${.now?iso_local}
Beställarens telefon: ${bestallning.bestallare.telefon}
Bäställarens e-post: ${bestallning.bestallare.epost}
Beställarens HSA-ID: <#if bestallning.bestallare.hsaId??>${bestallning.bestallare.hsaId}<#else>(okänt)</#if>
Beställarens roll: <#if bestallning.bestallareRoll??>${bestallning.bestallareRoll}<#else>(ej ifylld)</#if>
Övrig information från beställaren: <#if bestallning.otherInfo??>${bestallning.otherInfo}<#else>(ej ifylld)</#if>

<#--Nät: TODO: move to producent/konsument ??-->
<#--<#list bestallning.nat as nat>-->
    <#--${nat.namn}-->
<#--</#list>-->
<#if bestallning.producentbestallning??>
<#assign producentbestallning = bestallning.producentbestallning>


Producentbeställning
--------------------
tjänsteproducent:
    <#assign tjansteproducent = producentbestallning.tjanstekomponent>
    hsaId: ${tjansteproducent.hsaId}
    beskrivning: ${tjansteproducent.beskrivning}
    huvudansvarig:
    <#if tjansteproducent.huvudansvarigKontakt??>
        <#assign huvudansvarig = tjansteproducent.huvudansvarigKontakt>
        namn: ${huvudansvarig.namn!"-"}
        hsaId: ${huvudansvarig.hsaId!"-"}
        telefon: ${huvudansvarig.telefon!"-"}
        e-post: ${huvudansvarig.epost!"-"}
    <#else>
        (ej angiven)
    </#if>
    teknisk kontakt:
    <#if tjansteproducent.tekniskKontakt??>
        <#assign tekniskkontakt = tjansteproducent.tekniskKontakt>
        namn: ${tekniskkontakt.namn!"-"}
        hsaId: ${tekniskkontakt.hsaId!"-"}
        telefon: ${tekniskkontakt.telefon!"-"}
        e-post: ${tekniskkontakt.epost!"-"}
    <#else>
        (ej angiven)
    </#if>
    funktionsbrevlåda:
    <#if tjansteproducent.tekniskSupportkontakt??>
        <#assign funktionsbrevlada = tjansteproducent.tekniskSupportkontakt>
        telefon: ${funktionsbrevlada.telefon!"-"}
        e-post: ${funktionsbrevlada.epost!"-"}
    <#else>
        (ej angiven)
    </#if>

    producentanslutningar:
    <#if (producentbestallning.producentanslutningar)?has_content>
        <#list producentbestallning.producentanslutningar as producentanslutning>
        [${producentanslutning_index+1}]
            tjänstekontrakt: ${producentanslutning.tjanstekontraktNamnrymd} v${producentanslutning.tjanstekontraktMajorVersion}.${producentanslutning.tjanstekontraktMinorVersion} (${domanLookup[producentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
            RIVTA-profil: ${producentanslutning.rivtaProfil}
            URL: ${producentanslutning.url}
            giltig: ${producentanslutning.giltigFranTid?date?iso_local} - ${producentanslutning.giltigTillTid?date?iso_local}
            logiska adresser som skall läggas till:
            <#if (producentanslutning.nyaLogiskaAdresser)?has_content>
                <#list producentanslutning.nyaLogiskaAdresser as logiskAdress>
                [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                </#list>
            <#else>
                (inga)
            </#if>
        </#list>
    <#else>
        (inga)
    </#if>

    uppdaterade producentanslutningar:
    <#if (producentbestallning.uppdateradProducentanslutningar)?has_content>
        <#list producentbestallning.uppdateradProducentanslutningar as uppdateradProducentanslutning>
        [${uppdateradProducentanslutning_index+1}]
            tjänstekontrakt: ${uppdateradProducentanslutning.tjanstekontraktNamnrymd} v${uppdateradProducentanslutning.tjanstekontraktMajorVersion}.${uppdateradProducentanslutning.tjanstekontraktMinorVersion} (${domanLookup[uppdateradProducentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
            RIVTA-profil: ${uppdateradProducentanslutning.rivtaProfil} (<#if uppdateradProducentanslutning.rivtaProfil == uppdateradProducentanslutning.tidigareRivtaProfil>oförändrad<#else>uppdaterad</#if>)
            URL: ${uppdateradProducentanslutning.url} (<#if uppdateradProducentanslutning.url == uppdateradProducentanslutning.tidigareUrl>oförändrad<#else>uppdaterad</#if>)
            giltig: ${uppdateradProducentanslutning.giltigFranTid?date?iso_local} - ${uppdateradProducentanslutning.giltigTillTid?date?iso_local}
            logiska adresser som skall läggas till:
            <#if (uppdateradProducentanslutning.nyaLogiskaAdresser)?has_content>
                <#list uppdateradProducentanslutning.nyaLogiskaAdresser as logiskAdress>
                [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                </#list>
            <#else>
                (inga)
            </#if>
            nuvarande logiska adresser (endast som referens):
            <#if (uppdateradProducentanslutning.befintligaLogiskaAdresser)?has_content>
                <#list uppdateradProducentanslutning.befintligaLogiskaAdresser as logiskAdress>
                [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                </#list>
            <#else>
                (inga)
            </#if>
            logiska adresser som skall tas bort:
            <#if (uppdateradProducentanslutning.borttagnaLogiskaAdresser)?has_content>
                <#list uppdateradProducentanslutning.borttagnaLogiskaAdresser as logiskAdress>
                [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                </#list>
            <#else>
                (inga)
            </#if>
        </#list>
    <#else>
        (inga)
    </#if>
</#if>
<#if (bestallning.konsumentbestallningar)?has_content>


Konsumentbeställningar
----------------------
<#list bestallning.konsumentbestallningar as konsumentbestallning>
[${konsumentbestallning_index+1}]
    tjänstekonsument:
        <#assign tjanstekonsument = konsumentbestallning.tjanstekomponent>
        hsaId: ${tjanstekonsument.hsaId}
        beskrivning: ${tjanstekonsument.beskrivning}
        huvudansvarig:
        <#if tjanstekonsument.huvudansvarigKontakt??>
            <#assign huvudansvarig = tjanstekonsument.huvudansvarigKontakt>
            namn: ${huvudansvarig.namn!"-"}
            hsaId: ${huvudansvarig.hsaId!"-"}
            telefon: ${huvudansvarig.telefon!"-"}
            e-post: ${huvudansvarig.epost!"-"}
        <#else>
            (ej angiven)
        </#if>
        teknisk kontakt:
        <#if tjanstekonsument.tekniskKontakt??>
            <#assign tekniskkontakt = tjanstekonsument.tekniskKontakt>
            namn: ${tekniskkontakt.namn!"-"}
            hsaId: ${tekniskkontakt.hsaId!"-"}
            telefon: ${tekniskkontakt.telefon!"-"}
            e-post: ${tekniskkontakt.epost!"-"}
        <#else>
            (ej angiven)
        </#if>
        funktionsbrevlåda:
        <#if tjanstekonsument.tekniskSupportKontakt??>
            <#assign funktionsbrevlada = tjanstekonsument.tekniskSupportKontakt>
            telefon: ${funktionsbrevlada.telefon!"-"}
            e-post: ${funktionsbrevlada.epost!"-"}
        <#else>
            (ej angiven)
        </#if>

        konsumentanslutningar:
        <#if (konsumentbestallning.konsumentanslutningar)?has_content>
            <#list konsumentbestallning.konsumentanslutningar as konsumentanslutning>
            [${konsumentanslutning_index+1}]
                tjänstekontrakt: ${konsumentanslutning.tjanstekontraktNamnrymd} v${konsumentanslutning.tjanstekontraktMajorVersion}.${konsumentanslutning.tjanstekontraktMinorVersion} (${domanLookup[konsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
                giltig: ${konsumentanslutning.giltigFranTid?date?iso_local} - ${konsumentanslutning.giltigTillTid?date?iso_local}
                logiska adresser som skall läggas till:
                <#if (konsumentanslutning.nyaLogiskaAdresser)?has_content>
                    <#list konsumentanslutning.nyaLogiskaAdresser as logiskAdress>
                    [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                    </#list>
                <#else>
                    (inga)
                </#if>
            </#list>
        <#else>
            (inga)
        </#if>

        uppdaterade konsumentanslutningar:
        <#if (konsumentbestallning.uppdateradKonsumentanslutningar)?has_content>
            <#list konsumentbestallning.uppdateradKonsumentanslutningar as uppdateradKonsumentanslutning>
            [${uppdateradKonsumentanslutning_index+1}]
                tjänstekontrakt: ${uppdateradKonsumentanslutning.tjanstekontraktNamnrymd} v${uppdateradKonsumentanslutning.tjanstekontraktMajorVersion}.${uppdateradKonsumentanslutning.tjanstekontraktMinorVersion} (${domanLookup[uppdateradKonsumentanslutning.tjanstekontraktNamnrymd].svensktKortNamn})
                giltig: ${uppdateradKonsumentanslutning.giltigFranTid?datetime?iso_local} - ${uppdateradKonsumentanslutning.giltigTillTid?datetime?iso_local}
                logiska adresser som skall läggas till:
                <#if (uppdateradKonsumentanslutning.nyaLogiskaAdresser)?has_content>
                    <#list uppdateradKonsumentanslutning.nyaLogiskaAdresser as logiskAdress>
                    [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                    </#list>
                <#else>
                    (inga)
                </#if>
                nuvarande logiska adresser (endast som referens):
                <#if (uppdateradKonsumentanslutning.befintligaLogiskaAdresser)?has_content>
                    <#list uppdateradKonsumentanslutning.befintligaLogiskaAdresser as logiskAdress>
                    [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                    </#list>
                <#else>
                    (inga)
                </#if>
                logiska adresser som skall tas bort:
                <#if (uppdateradKonsumentanslutning.borttagnaLogiskaAdresser)?has_content>
                    <#list uppdateradKonsumentanslutning.borttagnaLogiskaAdresser as logiskAdress>
                    [${logiskAdress_index+1}] ${logiskAdress.hsaId} - ${logiskAdress.namn}
                    </#list>
                <#else>
                    (inga)
                </#if>
            </#list>
        <#else>
            (inga)
        </#if>
</#list>
</#if>
§
användare: ¤${(bestallning.bestallare.hsaId)?keep_after_last("-")}¤
£RTP£
ñ${bestallning.bestallare.epost}ñ
ÿsupport@callistaenterprise.seÿ
æRTPæ
