<#-- @ftlvariable name="tjanstekomponent" type="se.skltp.ap.Tjanstekomponent" -->
<#-- @ftlvariable name="bestallare" type="se.skltp.ap.services.dto.domain.PersonkontaktDTO" -->
WebMailForm
/1060\
§
.
.
Beställt av ${bestallare.namn!'-'} ${.now?iso_local}
Beställarens telefon: ${bestallare.telefon!'-'}
Beställarens e-post: ${bestallare.epost!'-'}
Beställarens HSA-ID: <#if bestallare.hsaId??>${bestallare.hsaId}<#else>(okänt)</#if>
.
Övrig information från beställaren:
${tjanstekomponent.otherInfo!'-'}
.
.
-------------------------------------------------------
Information om systeminstans
-------------------------------------------------------
.  HSA-Id: ${tjanstekomponent.hsaId} (Ny instans)
.  Organisation: ${tjanstekomponent.organisation!'-'}
.  Namn på system: ${tjanstekomponent.beskrivning!'-'}
.  Url till övervakningstjänst: ${tjanstekomponent.pingForConfigurationURL!'-'}
.  IP-adress vid anrop till systemet (systemet är producent): ${tjanstekomponent.producentIpadress!'-'}
.  Dns-namn: ${tjanstekomponent.producentDnsNamn!'-'}
.  IP-adress vid anrop från systemet (systemet är konsument): ${tjanstekomponent.konsumentIpadress!'-'}
<#if tjanstekomponent.nat??>
.  Nätverk: ${tjanstekomponent.nat.namn} (${tjanstekomponent.nat.id})
<#else>
.  Nätverk: -
</#if>
.
.
-------------------------------------------------------
Kontaktuppgifter
-------------------------------------------------------
<#assign kontakt = tjanstekomponent.huvudansvarigKontakt>
.  Huvudansvarig: ${kontakt.namn!'-'}
.  E-postadress: ${kontakt.epost!'-'}
.  Telefon: ${kontakt.telefon!'-'}
. 
<#assign kontakt = tjanstekomponent.tekniskKontakt>
.  Teknisk kontaktperson: ${kontakt.namn!'-'}
.  E-postadress: ${kontakt.epost!'-'}
.  Telefon: ${kontakt.telefon!'-'}
.
.  Teknisk support, funktionsbrevlåda: ${tjanstekomponent.tekniskSupportkontakt.epost!'-'}
.  Teknisk support, telefon: ${tjanstekomponent.tekniskSupportkontakt.telefon!'-'}
. 
.
§
användare: ¤${(bestallare.hsaId)?keep_after_last("-")}¤
£RTP£
ñ${bestallare.epost!'epost_okand'}ñ
ÿÿ
æRTPæ