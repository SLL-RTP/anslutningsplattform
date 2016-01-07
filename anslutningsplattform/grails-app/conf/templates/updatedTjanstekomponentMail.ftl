<#-- @ftlvariable name="oldTjanstekomponent" type="se.skltp.ap.Tjanstekomponent" -->
<#-- @ftlvariable name="newTjanstekomponent" type="se.skltp.ap.Tjanstekomponent" -->
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
(fältet finns ej)
.
.
-------------------------------------------------------
Information om systeminstans
-------------------------------------------------------
.  HSAid: ${newTjanstekomponent.hsaId}
.  Organisation: ${newTjanstekomponent.organisation!'-'} ${(newTjanstekomponent.organisation?? != oldTjanstekomponent.organisation??)?then('(uppdaterad)', '')}
.  Namn på system: ${newTjanstekomponent.beskrivning!'-'} ${(newTjanstekomponent.beskrivning != oldTjanstekomponent.beskrivning)?then('(uppdaterad)', '')}
.  Url till övervakningstjänst: ${newTjanstekomponent.pingForConfigurationURL!'-'} ${(newTjanstekomponent.pingForConfigurationURL != oldTjanstekomponent.pingForConfigurationURL)?then('(uppdaterad)', '')}
.  IP-adress vid anrop till systemet (systemet är producent): ${newTjanstekomponent.producentIpadress!'-'} ${(newTjanstekomponent.producentIpadress?? != oldTjanstekomponent.producentIpadress??)?then('(uppdaterad)', '')}
.  Dns-namn: ${newTjanstekomponent.producentDnsNamn!'-'} ${(newTjanstekomponent.producentDnsNamn?? != oldTjanstekomponent.producentDnsNamn??)?then('(uppdaterad)', '')}
.  IP-adress vid anrop från systemet (systemet är konsument): ${newTjanstekomponent.konsumentIpadress!'-'} ${(newTjanstekomponent.konsumentIpadress?? != oldTjanstekomponent.konsumentIpadress??)?then('(uppdaterad)', '')}
<#if newTjanstekomponent.nat?? && oldTjanstekomponent.nat??>
.  Nätverk: ${newTjanstekomponent.nat.namn} (${newTjanstekomponent.nat.id}) ${(newTjanstekomponent.nat.id != oldTjanstekomponent.nat.id)?then('(uppdaterad)', '')}
<#elseif newTjanstekomponent.nat??>
.  Nätverk: ${newTjanstekomponent.nat.namn} (${newTjanstekomponent.nat.id}) (uppdaterad) <#-- inget tidigare nät -->
<#else>
.  Nätverk: -
</#if>
.
.
-------------------------------------------------------
Kontaktuppgifter
-------------------------------------------------------
<#assign kontakt = newTjanstekomponent.huvudansvarigKontakt>
.  Huvudansvarig: ${kontakt.namn!'-'}
.  E-postadress: ${kontakt.epost!'-'}
.  Telefon: ${kontakt.telefon!'-'}
. 
<#assign kontakt = newTjanstekomponent.tekniskKontakt>
.  Teknisk kontaktperson: ${kontakt.namn!'-'}
.  E-postadress: ${kontakt.epost!'-'}
.  Telefon: ${kontakt.telefon!'-'}
.
.  Teknisk support, funktionsbrevlåda: ${newTjanstekomponent.tekniskSupportkontakt.epost!'-'}
.  Teknisk support, telefon: ${newTjanstekomponent.tekniskSupportkontakt.telefon!'-'}
. 
.
§
användare: ¤${(bestallare.hsaId)?keep_after_last("-")}¤
£RTP£
ñ${bestallare.epost!'epost_okand'}ñ
ÿsupport@callistaenterprise.seÿ
æRTPæ